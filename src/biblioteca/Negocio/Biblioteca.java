package biblioteca.Negocio;

import biblioteca.Enum.EnumStatusItem;
import biblioteca.Enum.Permissao;
import biblioteca.Enum.StatusEmprestimo;
import biblioteca.Enum.StatusMulta;
import biblioteca.Enum.StatusReserva;
import biblioteca.Excecoes.ItemNaoEncontradoException;
import biblioteca.repositorios.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Biblioteca {

    private Repositorio<Livro> repositorioLivro;
    private Repositorio<Membro> repositorioMembro;
    private Repositorio<Funcionario> repositorioFuncionario;
    
    private List<Membro> membros;
    private List<Funcionario> funcionarios;
    private List<ItemDoAcervo> acervo;
    private List<Emprestimo> emprestimos;
    private List<Reserva> reservas;
    private List<Setor> setores;
    private List<Multa> multas;
    private Caixa caixaAtual;
    private Funcionario funcionarioLogado;

    private static Biblioteca instancia;
   
    private Biblioteca() {

        this.repositorioLivro = new RepositorioLivroCSV();
        this.repositorioMembro = new RepositorioMembroCSV();
        this.repositorioFuncionario = new RepositorioFuncionarioCSV();

        this.membros = new ArrayList<>(repositorioMembro.carregar());
        this.funcionarios = new ArrayList<>(repositorioFuncionario.carregar());
        this.acervo = new ArrayList<>(repositorioLivro.carregar());
        this.emprestimos = new ArrayList<>();
        this.reservas = new ArrayList<>();
        this.setores = new ArrayList<>();
        this.multas = new ArrayList<>();

    }

    public static Biblioteca getInstance() {
        if (instancia == null) {
            instancia = new Biblioteca();
        }
        return instancia;
    }

    //==== Login/Logout ====
    public void login(Funcionario f) {
        this.funcionarioLogado = f;
        System.out.println("Funcionário logado: " + f.getLogin());
    }

    public void logout() {
        if (funcionarioLogado != null) {
            System.out.println("Funcionário " + funcionarioLogado.getLogin() + " deslogado.");
            this.funcionarioLogado = null;
        }
    }

    public Funcionario getFuncionarioLogado() {
        return this.funcionarioLogado;
    }

    // ===== Cadastro e busca de itens =====
    public void cadastrarLivro(Livro livro) {
        if (funcionarioLogado == null) {
            System.out.println("Nenhum funcionário logado. Operação não permitida.");
            return;
        }
        if (funcionarioLogado.getPermissao() != Permissao.ADMINISTRADOR) {
            System.out.println("Permissão insuficiente para cadastrar livros.");
            return;
        }

        this.acervo.add(livro);
        repositorioLivro.salvar(livro);
        System.out.println("Livro cadastrado: " + livro.getTitulo());
    }

    public void adicionarMaisCopias(ItemDoAcervo item, int quantidade) throws ItemNaoEncontradoException {
    ItemDoAcervo itemNoAcervo = this.acervo.stream()
        .filter(i -> i.getTitulo().equals(item.getTitulo()))
        .findFirst()
        .orElseThrow(() -> new ItemNaoEncontradoException("Item não encontrado no acervo."));

        itemNoAcervo.setQuantidade(itemNoAcervo.getQuantidade() + quantidade);

    }
    
    public boolean removerItem(ItemDoAcervo item) {
        return this.acervo.remove(item);
    } 
    
    public void adicionarItem(ItemDoAcervo item, int quantidade) {
        item.setQuantidade(quantidade);
        this.acervo.add(item);
    }

    public List<ItemDoAcervo> buscarItemPorTitulo(String titulo) {
        return this.acervo.stream()
                .filter(item -> item.getTitulo().equalsIgnoreCase(titulo))
                .collect(Collectors.toList());
    }

    public List<Livro> buscarLivroPorAutor(String nomeAutor) {
        return this.acervo.stream()
                .filter(item -> item instanceof Livro)
                .map(item -> (Livro) item)
                .filter(livro -> livro.getAutor().getNome().equalsIgnoreCase(nomeAutor))
                .collect(Collectors.toList());
    }

    public List<ItemDoAcervo> buscarItemPorPalavraChave(String palavraChave) {
        String palavraChaveLowerCase = palavraChave.toLowerCase();
        return this.acervo.stream()
                .filter(item -> item.getTitulo().toLowerCase().contains(palavraChaveLowerCase))
                .collect(Collectors.toList());
    }

    //=== Membros ===
    public void adicionarMembro(Membro membro) {
        this.membros.add(membro);
    }

    public Membro buscarMembroPorCPF(String cpf) {
        return this.membros.stream()
                .filter(membro -> membro.getCpf().equals(cpf))
                .findFirst()
                .orElse(null);
    }

    //==== Empréstimos e Reservas ====
    public boolean realizarEmprestimo(Membro membro, ItemDoAcervo item) throws biblioteca.Excecoes.MembroComDebitoException {
        if (!debitosPendentes(membro).isEmpty()) {
            throw new biblioteca.Excecoes.MembroComDebitoException("O membro possui debitos pendentes e nao pode realizar emprestimos.");
        }

        if (item.verificarDisponibilidade() && item.getStatus() == EnumStatusItem.DISPONIVEL) {
            Emprestimo novoEmprestimo = new Emprestimo(membro, item, LocalDate.now());
            this.emprestimos.add(novoEmprestimo);
            item.setStatus(EnumStatusItem.EMPRESTADO);
            return true;
        }
        return false;
    }

    public void realizarDevolucao(Emprestimo emprestimo) {
        emprestimo.finalizarEmprestimo(LocalDate.now());
        emprestimo.getItemDoAcervo().setStatus(EnumStatusItem.DISPONIVEL);
        calcularMultasAtraso(emprestimo);
    }

    public boolean realizarReserva(Membro membro, ItemDoAcervo item) {
        if (item.getStatus() != EnumStatusItem.DISPONIVEL) {
            Reserva novaReserva = new Reserva(membro, item, LocalDate.now());
            this.reservas.add(novaReserva);
            item.setStatus(EnumStatusItem.RESERVADO);
            return true;
        }
        return false;
    }

    private void calcularMultasAtraso(Emprestimo emprestimo) {
        if (emprestimo.estaAtrasado()) {
            long diasAtraso = ChronoUnit.DAYS.between(emprestimo.getDataDevolucaoPrevista(), emprestimo.getDevolucaoRealizada());
            BigDecimal valorMulta = BigDecimal.valueOf(diasAtraso * 1.0);
            Multa novaMulta = new Multa(emprestimo.getMembro(), emprestimo, valorMulta, StatusMulta.PENDENTE);
            this.multas.add(novaMulta);
        }
    }

    public List<Multa> debitosPendentes(Membro membro) {
        return this.multas.stream()
                .filter(multa -> multa.getMembro().equals(membro) && multa.getStatus() == StatusMulta.PENDENTE)
                .collect(Collectors.toList());
    }

    //==== Relatórios ====
    public List<Emprestimo> gerarRelatorioDeAtrasos() {
        return this.emprestimos.stream()
                .filter(e -> e.getStatus() == StatusEmprestimo.ATIVO && e.estaAtrasado())
                .collect(Collectors.toList());
    }

    public Map<ItemDoAcervo, Long> gerarRelatorioDeItensMaisEmprestados() {
        return this.emprestimos.stream()
                .collect(Collectors.groupingBy(Emprestimo::getItemDoAcervo, Collectors.counting()
                ));
    }

    public List<Reserva> gerarRelatorioDeReservasAtivas() {
        return this.reservas.stream()
                .filter(r -> r.getStatus() == StatusReserva.ATIVA)
                .collect(Collectors.toList());
    }


    //==== Setores ====
    public void adicionarSetor(Setor setor) {
        this.setores.add(setor);
    }

    //==== Caixa ====
    public void abrirCaixa(double saldoInicial) {
        if (this.caixaAtual == null || this.caixaAtual.getStatus() == biblioteca.Enum.StatusCaixa.FECHADO) {
            this.caixaAtual = new Caixa();
        }
    }

    public void fecharCaixa() {
        if (this.caixaAtual != null && this.caixaAtual.getStatus() == biblioteca.Enum.StatusCaixa.ABERTO) {
            this.caixaAtual.fecharCaixa();
        }
    }

    //==== Funcionários ====
    public void adicionarFuncionario(Funcionario funcionario) {
        this.funcionarios.add(funcionario);
    }

    public boolean autenticarFuncionario(String login, String senha) {
        for (Funcionario f : this.funcionarios) {
            if (f.getLogin().equals(login) && f.getSenhaHash().equals(senha)) {
                this.funcionarioLogado = f;
                return true;
            }
        }
        return false;
    }

    public void salvar() {
        for (ItemDoAcervo item : acervo) {
            if (item instanceof Livro) {
                repositorioLivro.salvar((Livro) item);
            }
        }
    
        for (Membro m : membros) {
            repositorioMembro.salvar(m);
        }
        for (Funcionario f : funcionarios) {
            repositorioFuncionario.salvar(f);
        }
        System.out.println("Todos os dados foram salvos em CSV!");
    }

}

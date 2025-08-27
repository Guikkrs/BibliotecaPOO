package biblioteca.Negocio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import biblioteca.Enum.StatusCaixa;
import biblioteca.Enum.StatusEmprestimo;
import biblioteca.Enum.StatusLivro;
import biblioteca.Enum.StatusMulta;
import biblioteca.Enum.StatusReserva;

public class Biblioteca {
    List<Membro> membros;
    List<Funcionario> funcionarios;
    List<ItemDoAcervo> acervos;
    List<Emprestimo> emprestimos;
    List<Reserva> reservas;
    List<Setor> setores;
    List<Multa> multas; // Adicionado: Lista para armazenar as multas do sistema
    Caixa caixaAtual;
    Funcionario funcionarioLogado;
    
    public Biblioteca() {
        this.membros = new ArrayList<>();
        this.funcionarios = new ArrayList<>();
        this.acervos = new ArrayList<>();
        this.emprestimos = new ArrayList<>();
        this.reservas = new ArrayList<>();
        this.setores = new ArrayList<>();
        this.multas = new ArrayList<>(); // Inicializado
    }

    // Retorna uma lista de empréstimos ativos de um membro.
    public List<Emprestimo> consultarEmprestimos(Membro membro) {
        List<Emprestimo> emprestimosAtivos = new ArrayList<>();
        for (Emprestimo e : this.emprestimos) {
            if (e.getMembro().equals(membro) && e.getStatus() == StatusEmprestimo.ATIVO) {
                emprestimosAtivos.add(e);
            }
        }
        return emprestimosAtivos;
    }

    // Retorna o histórico completo de empréstimos de um membro.
    public List<Emprestimo> consultarHistorico(Membro membro) {
        List<Emprestimo> historico = new ArrayList<>();
        for (Emprestimo e : this.emprestimos) {
            if (e.getMembro().equals(membro)) {
                historico.add(e);
            }
        }
        return historico;
    }

    // Retorna uma lista de multas pendentes de um membro.
    public List<Multa> debitosPendentes(Membro membro) {
        List<Multa> multasPendentes = new ArrayList<>();
        for (Multa multa : this.multas) {
            if (multa.getMembro().equals(membro) && multa.getStatus() == StatusMulta.PENDENTE) {
                multasPendentes.add(multa);
            }
        }
        return multasPendentes;
    }

    // Calcula multas para um empréstimo atrasado e o registra.
    public void calcularMultas(Emprestimo emprestimo) {
        LocalDate dataAtual = LocalDate.now();
        LocalDate dataDevolucaoPrevista = emprestimo.getDataDevolucaoPrevista();
        
        if (dataAtual.isAfter(dataDevolucaoPrevista)) {
            long diasAtraso = ChronoUnit.DAYS.between(dataDevolucaoPrevista, dataAtual);
            BigDecimal valorMulta = new BigDecimal("1.00").multiply(new BigDecimal(diasAtraso));

            Multa novaMulta = new Multa(emprestimo.getMembro(), emprestimo, valorMulta, StatusMulta.PENDENTE);
            this.multas.add(novaMulta);
        }
    }

    // Autentica um funcionário.
    public boolean autenticarFuncionario(String login, String senha) {
        for (Funcionario f : this.funcionarios) {
            if (f.getLogin().equals(login) && f.getSenhaHash().equals(senha)) {
                this.funcionarioLogado = f;
                return true;
            }
        }
        return false;
    }

    // Adiciona um novo item ao acervo com uma quantidade inicial.
    public void adicionarItem(ItemDoAcervo item, int quantidade) {
        item.setQuantidade(quantidade);
        this.acervos.add(item);
    }

    // Adiciona mais cópias de um item existente.
    public void adicionarMaisCopias(ItemDoAcervo item, int quantidade) {
        for (ItemDoAcervo i : this.acervos) {
            if (i.equals(item)) {
                i.setQuantidade(i.getQuantidade() + quantidade);
                return;
            }
        }
    }

    // Remove um item completamente do acervo.
    public boolean removerItem(ItemDoAcervo item) {
        return this.acervos.remove(item);
    }

    // Busca itens do acervo por título.
    public List<ItemDoAcervo> buscarPorTitulo(String titulo) {
        List<ItemDoAcervo> resultados = new ArrayList<>();
        for (ItemDoAcervo item : this.acervos) {
            if (item.getTitulo().equalsIgnoreCase(titulo)) {
                resultados.add(item);
            }
        }
        return resultados;
    }

    // Busca livros por autor.
    public List<Livro> buscarLivroPorAutor(String nomeAutor) {
        List<Livro> resultados = new ArrayList<>();
        for (ItemDoAcervo item : this.acervos) {
            if (item instanceof Livro) {
                Livro livro = (Livro) item;
                if (livro.getAutor().getNome().equalsIgnoreCase(nomeAutor)) {
                    resultados.add(livro);
                }
            }
        }
        return resultados;
    }
    
    // Busca itens por palavra-chave no título, gênero, etc.
    public List<ItemDoAcervo> buscarItemPorPalavraChave(String palavraChave) {
        List<ItemDoAcervo> resultados = new ArrayList<>();
        String palavraChaveLowerCase = palavraChave.toLowerCase();
        for (ItemDoAcervo item : this.acervos) {
            if (item.getTitulo().toLowerCase().contains(palavraChaveLowerCase) || 
                item.getGenero().toLowerCase().contains(palavraChaveLowerCase)) {
                resultados.add(item);
            }
        }
        return resultados;
    }
    
    // Busca um membro por CPF.
    public Membro buscarMembroPorCPF(String cpf) {
        for (Membro m : this.membros) {
            if (m.getCpf().equals(cpf)) {
                return m;
            }
        }
        return null;
    }
    
    // Realiza um empréstimo.
    public boolean realizarEmprestimo(Membro membro, ItemDoAcervo item) {
        // Regra 1: Verificar se o item está disponível
        if (item.getQuantidade() == 0 || item.getStatus() != StatusLivro.DISPONIVEL) {
            return false;
        }
        // Regra 2: Verificar débitos pendentes do membro
        if (!debitosPendentes(membro).isEmpty()) {
            return false;
        }
        // Regra 3: Verificar limite de empréstimos (exemplo: 5)
        if (consultarEmprestimos(membro).size() >= 5) {
            return false;
        }
        
        Emprestimo novoEmprestimo = new Emprestimo(membro, item, LocalDate.now());
        this.emprestimos.add(novoEmprestimo);
        item.setQuantidade(item.getQuantidade() - 1);
        item.setStatus(StatusLivro.EMPRESTADO);
        
        return true;
    }
    
    // Realiza a devolução de um item.
    public void realizarDevolucao(Emprestimo emprestimo) {
        emprestimo.setStatus(StatusEmprestimo.DEVOLVIDO);
        emprestimo.setDevolucaoRealizada(LocalDate.now());
        
        ItemDoAcervo item = emprestimo.getItemDoAcervo();
        item.setQuantidade(item.getQuantidade() + 1);
        item.setStatus(StatusLivro.DISPONIVEL);
        
        calcularMultas(emprestimo);
    }
    
    // Realiza a reserva de um item.
    public boolean realizarReserva(Membro membro, ItemDoAcervo item) {
        if (item.getStatus() != StatusLivro.EMPRESTADO) {
            return false;
        }

        for (Reserva r : this.reservas) {
            if (r.getMembro().equals(membro) && r.getItemdoarcevo().equals(item)) {
                return false;
            }
        }
        
        Reserva novaReserva = new Reserva(membro, item, LocalDate.now());
        this.reservas.add(novaReserva);
        item.setStatus(StatusLivro.RESERVADO);
        
        return true;
    }
    
    // Gera um relatório de empréstimos em atraso.
    public List<Emprestimo> gerarRelatorioDeAtrasos() {
        List<Emprestimo> atrasos = new ArrayList<>();
        for (Emprestimo e : this.emprestimos) {
            if (e.getStatus() == StatusEmprestimo.ATIVO && LocalDate.now().isAfter(e.getDataDevolucaoPrevista())) {
                atrasos.add(e);
            }
        }
        return atrasos;
    }
    
    // Gera um relatório dos itens mais emprestados.
    public Map<ItemDoAcervo, Integer> gerarRelatorioDeItensMaisEmprestados() {
        Map<ItemDoAcervo, Integer> contagemEmprestimos = new HashMap<>();
        for (Emprestimo e : this.emprestimos) {
            ItemDoAcervo item = e.getItemDoAcervo();
            contagemEmprestimos.put(item, contagemEmprestimos.getOrDefault(item, 0) + 1);
        }
        return contagemEmprestimos;
    }
    
    // Gera um relatório de reservas ativas.
    public List<Reserva> gerarRelatorioDeReservasAtivas() {
        List<Reserva> reservasAtivas = new ArrayList<>();
        for (Reserva r : this.reservas) {
            if (r.getStatus() == StatusReserva.ATIVA) {
                reservasAtivas.add(r);
            }
        }
        return reservasAtivas;
    }

    // Adiciona um setor à lista de setores da biblioteca.
    public void adicionarSetor(Setor setor) {
        this.setores.add(setor);
    }

    // Abre o caixa.
    public void abrirCaixa(BigDecimal saldoInicial) {
        if (this.caixaAtual == null) {
            this.caixaAtual = new Caixa();
        }
        this.caixaAtual.abrirCaixa(saldoInicial);
    }

    // Fecha o caixa.
    public void fecharCaixa() {
        if (this.caixaAtual != null) {
            this.caixaAtual.fecharCaixa();
        }
    }

}
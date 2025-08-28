package biblioteca.Negocio;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import biblioteca.Enum.EnumStatusItem;
import biblioteca.Enum.StatusCaixa;
import biblioteca.Enum.StatusEmprestimo;
import biblioteca.Enum.StatusMulta;
import biblioteca.Enum.StatusReserva;
import biblioteca.Enum.EnumStatusItem;

public class Biblioteca {

    List<Membro> membros;
    List<Funcionario> funcionarios;
    List<ItemDoAcervo> acervos;
    List<Emprestimo> emprestimos;
    List<Reserva> reservas;
    List<Setor> setores;
    List<Multa> multas;
    Caixa caixaAtual;
    Funcionario funcionarioLogado;

    public Biblioteca() {
        this.membros = new ArrayList<>();
        this.funcionarios = new ArrayList<>();
        this.acervos = new ArrayList<>();
        this.emprestimos = new ArrayList<>();
        this.reservas = new ArrayList<>();
        this.setores = new ArrayList<>();
        this.multas = new ArrayList<>();
    }

    public List<Emprestimo> consultarEmprestimos(Membro membro) {
        List<Emprestimo> emprestimosAtivos = new ArrayList<>();
        for (Emprestimo e : this.emprestimos) {
            if (e.getMembro().equals(membro) && e.getStatus() == StatusEmprestimo.ATIVO) {
                emprestimosAtivos.add(e);
            }
        }
        return emprestimosAtivos;
    }

    public List<Emprestimo> consultarHistorico(Membro membro) {
        List<Emprestimo> historico = new ArrayList<>();
        for (Emprestimo e : this.emprestimos) {
            if (e.getMembro().equals(membro)) {
                historico.add(e);
            }
        }
        return historico;
    }

    public List<Multa> debitosPendentes(Membro membro) {
        List<Multa> multasPendentes = new ArrayList<>();
        for (Multa multa : this.multas) {
            if (multa.getMembro().equals(membro) && multa.getStatus() == StatusMulta.PENDENTE) {
                multasPendentes.add(multa);
            }
        }
        return multasPendentes;
    }

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

    public boolean autenticarFuncionario(String login, String senha) {
        for (Funcionario f : this.funcionarios) {
            if (f.getLogin().equals(login) && f.getSenhaHash().equals(senha)) {
                this.funcionarioLogado = f;
                return true;
            }
        }
        return false;
    }

    public void adicionarItem(ItemDoAcervo item, int quantidade) {
        item.setQuantidade(quantidade);
        this.acervos.add(item);
    }

    public void adicionarMaisCopias(ItemDoAcervo item, int quantidade) {
        for (ItemDoAcervo i : this.acervos) {
            if (i.equals(item)) {
                i.setQuantidade(i.getQuantidade() + quantidade);
                return;
            }
        }
    }

    public boolean removerItem(ItemDoAcervo item) {
        return this.acervos.remove(item);
    }

    public List<ItemDoAcervo> buscarPorTitulo(String titulo) {
        List<ItemDoAcervo> resultados = new ArrayList<>();
        for (ItemDoAcervo item : this.acervos) {
            if (item.getTitulo().equalsIgnoreCase(titulo)) {
                resultados.add(item);
            }
        }
        return resultados;
    }

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

    public List<ItemDoAcervo> buscarItemPorPalavraChave(String palavraChave) {
        List<ItemDoAcervo> resultados = new ArrayList<>();
        String palavraChaveLowerCase = palavraChave.toLowerCase();
        for (ItemDoAcervo item : this.acervos) {
            if (item.getTitulo().toLowerCase().contains(palavraChaveLowerCase)
                    || ((String) ((Livro) item).getGenero()).toLowerCase().contains(palavraChaveLowerCase)) {
                resultados.add(item);
            }
        }
        return resultados;
    }

    public Membro buscarMembroPorCPF(String cpf) {
        for (Membro m : this.membros) {
            if (m.getCpf().equals(cpf)) {
                return m;
            }
        }
        return null;
    }

    public boolean realizarEmprestimo(Membro membro, ItemDoAcervo item) {
        if (item.getQuantidade() == 0 || item.getStatus() != EnumStatusItem.DISPONIVEL) {
            return false;
        }
        if (!debitosPendentes(membro).isEmpty()) {
            return false;
        }
        if (consultarEmprestimos(membro).size() >= 5) {
            return false;
        }

        Emprestimo novoEmprestimo = new Emprestimo(membro, item, LocalDate.now());
        this.emprestimos.add(novoEmprestimo);
        item.setQuantidade(item.getQuantidade() - 1);
        item.setStatus(EnumStatusItem.EMPRESTADO);

        return true;
    }

    // Método corrigido e unificado para devolução
    public void realizarDevolucao(Emprestimo emprestimo) {
        emprestimo.setStatus(StatusEmprestimo.DEVOLVIDO);
        emprestimo.setDevolucaoRealizada(LocalDate.now());

        ItemDoAcervo item = emprestimo.getItemDoAcervo();
        item.setQuantidade(item.getQuantidade() + 1);
        item.setStatus(EnumStatusItem.DISPONIVEL);

        calcularMultas(emprestimo);
    }

    public boolean realizarReserva(Membro membro, ItemDoAcervo item) {
        if (item.getStatus() != EnumStatusItem.EMPRESTADO) {
            return false;
        }

        for (Reserva r : this.reservas) {
            if (r.getMembro().equals(membro) && r.getItemdoarcevo().equals(item)) {
                return false;
            }
        }

        Reserva novaReserva = new Reserva(membro, item, LocalDate.now());
        this.reservas.add(novaReserva);
        item.setStatus(EnumStatusItem.RESERVADO);

        return true;
    }

    public List<Emprestimo> gerarRelatorioDeAtrasos() {
        List<Emprestimo> atrasos = new ArrayList<>();
        for (Emprestimo e : this.emprestimos) {
            if (e.getStatus() == StatusEmprestimo.ATIVO && LocalDate.now().isAfter(e.getDataDevolucaoPrevista())) {
                atrasos.add(e);
            }
        }
        return atrasos;
    }

    public Map<ItemDoAcervo, Integer> gerarRelatorioDeItensMaisEmprestados() {
        Map<ItemDoAcervo, Integer> contagemEmprestimos = new HashMap<>();
        for (Emprestimo e : this.emprestimos) {
            ItemDoAcervo item = e.getItemDoAcervo();
            contagemEmprestimos.put(item, contagemEmprestimos.getOrDefault(item, 0) + 1);
        }
        return contagemEmprestimos;
    }

    public List<Reserva> gerarRelatorioDeReservasAtivas() {
        List<Reserva> reservasAtivas = new ArrayList<>();
        for (Reserva r : this.reservas) {
            if (r.getStatus() == StatusEmprestimo.ATIVO) {
                reservasAtivas.add(r);
            }
        }
        return reservasAtivas;
    }

    public void adicionarSetor(Setor setor) {
        this.setores.add(setor);
    }

    public void abrirCaixa(BigDecimal saldoInicial) {
        if (this.caixaAtual == null) {
            this.caixaAtual = new Caixa();
        }
        this.caixaAtual.abrirCaixa(saldoInicial);
    }

    public void fecharCaixa() {
        if (this.caixaAtual != null) {
            this.caixaAtual.fecharCaixa();
        }
    }

    public void adicionarMembro(Membro membro) {
        this.membros.add(membro);
    }
}

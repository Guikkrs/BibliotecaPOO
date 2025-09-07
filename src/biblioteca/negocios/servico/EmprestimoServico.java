package biblioteca.negocios.servico;

import biblioteca.dados.repositorio.IRepositorioEmprestimo;
import biblioteca.dados.repositorio.IRepositorioItemDoAcervo;
import biblioteca.dados.repositorio.IRepositorioMulta;
import biblioteca.negocios.entidade.Emprestimo;
import biblioteca.negocios.entidade.ItemDoAcervo;
import biblioteca.negocios.entidade.Membro;
import biblioteca.negocios.entidade.Multa;
import biblioteca.negocios.enums.EnumStatusItem;
import biblioteca.negocios.enums.StatusEmprestimo;
import biblioteca.negocios.enums.StatusMulta;
import biblioteca.negocios.excecoes.acervo.ItemNaoDisponivelException;
import biblioteca.negocios.excecoes.emprestimo.EmprestimoNaoEncontradoException;
import biblioteca.negocios.excecoes.emprestimo.LimiteDeEmprestimosAtingidoException;
import biblioteca.negocios.excecoes.emprestimo.MembroComDebitoException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class EmprestimoServico {

    private final IRepositorioEmprestimo repositorioEmprestimo;
    private final IRepositorioMulta repositorioMulta;
    private final IRepositorioItemDoAcervo repositorioAcervo;
    private static final int LIMITE_EMPRESTIMOS = 5;

    public EmprestimoServico(IRepositorioEmprestimo repositorioEmprestimo, IRepositorioMulta repositorioMulta, IRepositorioItemDoAcervo repositorioAcervo) {
        this.repositorioEmprestimo = repositorioEmprestimo;
        this.repositorioMulta = repositorioMulta;
        this.repositorioAcervo = repositorioAcervo;
    }

    public void realizarEmprestimo(Membro membro, ItemDoAcervo item) throws MembroComDebitoException, ItemNaoDisponivelException, LimiteDeEmprestimosAtingidoException {
        if (!debitosPendentes(membro).isEmpty()) {
            throw new MembroComDebitoException("O membro possui multas pendentes.");
        }

        long emprestimosAtivos = repositorioEmprestimo.listarTodos().stream()
                .filter(e -> e.getMembro().equals(membro) && e.getStatus() == StatusEmprestimo.ATIVO)
                .count();

        if (emprestimosAtivos >= LIMITE_EMPRESTIMOS) {
            throw new LimiteDeEmprestimosAtingidoException(membro.getNome(), LIMITE_EMPRESTIMOS);
        }

        if (!item.verificarDisponibilidade()) {
            throw new ItemNaoDisponivelException(item.getTitulo());
        }

        Emprestimo novoEmprestimo = new Emprestimo(membro, item, LocalDate.now());
        repositorioEmprestimo.adicionar(novoEmprestimo);

        item.setQuantidade(item.getQuantidade() - 1);
        if (item.getQuantidade() == 0) {
            item.setStatus(EnumStatusItem.EMPRESTADO);
        }
        repositorioAcervo.atualizar(item);
    }

    public void realizarDevolucao(Emprestimo emprestimo) {
        emprestimo.finalizarEmprestimo(LocalDate.now());
        repositorioEmprestimo.atualizar(emprestimo);

        ItemDoAcervo item = emprestimo.getItemDoAcervo();
        item.setQuantidade(item.getQuantidade() + 1);
        item.setStatus(EnumStatusItem.DISPONIVEL);
        repositorioAcervo.atualizar(item);

        if (emprestimo.getDevolucaoRealizada().isAfter(emprestimo.getDataDevolucaoPrevista())) {
            long diasAtraso = ChronoUnit.DAYS.between(emprestimo.getDataDevolucaoPrevista(), emprestimo.getDevolucaoRealizada());
            if (diasAtraso > 0) {
                BigDecimal valorMulta = new BigDecimal(diasAtraso);
                Multa novaMulta = new Multa(emprestimo.getMembro(), emprestimo, valorMulta);
                repositorioMulta.adicionar(novaMulta);
            }
        }
    }

    public List<Multa> debitosPendentes(Membro membro) {
        return repositorioMulta.listarTodos().stream()
                .filter(multa -> multa.getMembro().equals(membro) && multa.getStatus() == StatusMulta.PENDENTE)
                .collect(Collectors.toList());
    }

    public Emprestimo buscarEmprestimoAtivo(Membro membro, ItemDoAcervo item) throws EmprestimoNaoEncontradoException {
        return repositorioEmprestimo.listarTodos().stream()
                .filter(e -> e.getMembro().equals(membro) &&
                        e.getItemDoAcervo().equals(item) &&
                        e.getStatus() == StatusEmprestimo.ATIVO)
                .findFirst()
                .orElseThrow(EmprestimoNaoEncontradoException::new);
    }
    public List<Emprestimo> listarEmprestimosAtivosPorMembro(Membro membro) {
        return repositorioEmprestimo.listarTodos().stream()
                .filter(e -> e.getMembro().equals(membro) && e.getStatus() == StatusEmprestimo.ATIVO)
                .collect(Collectors.toList());
    }
}


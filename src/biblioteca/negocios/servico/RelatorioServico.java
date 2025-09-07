package biblioteca.negocios.servico;

import biblioteca.dados.repositorio.IRepositorioEmprestimo;
import biblioteca.dados.repositorio.IRepositorioReserva;
import biblioteca.negocios.entidade.Emprestimo;
import biblioteca.negocios.entidade.ItemDoAcervo;
import biblioteca.negocios.entidade.Reserva;
import biblioteca.negocios.enums.StatusReserva;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RelatorioServico {

    private final IRepositorioEmprestimo repositorioEmprestimo;
    private final IRepositorioReserva repositorioReserva;

    public RelatorioServico(IRepositorioEmprestimo repositorioEmprestimo, IRepositorioReserva repositorioReserva) {
        this.repositorioEmprestimo = repositorioEmprestimo;
        this.repositorioReserva = repositorioReserva;
    }

    /**
     * Gera uma lista de todos os empréstimos ativos que estão atrasados na data atual.
     * @return Lista de empréstimos em atraso.
     */
    public List<Emprestimo> gerarRelatorioDeAtrasos() {
        return repositorioEmprestimo.listarTodos().stream()
                .filter(Emprestimo::estaAtrasado)
                .collect(Collectors.toList());
    }

    /**
     * Gera um mapa dos itens mais emprestados, contando quantas vezes cada item foi emprestado.
     * @return Um Mapa onde a chave é o ItemDoAcervo e o valor é a contagem de empréstimos.
     */
    public Map<ItemDoAcervo, Long> gerarRelatorioDeItensMaisEmprestados() {
        return repositorioEmprestimo.listarTodos().stream()
                .collect(Collectors.groupingBy(Emprestimo::getItemDoAcervo, Collectors.counting()));
    }

    /**
     * Gera uma lista de todas as reservas que estão atualmente com o status ATIVA.
     * @return Lista de reservas ativas.
     */
    public List<Reserva> gerarRelatorioDeReservasAtivas() {
        return repositorioReserva.listarTodos().stream()
                .filter(reserva -> reserva.getStatus() == StatusReserva.ATIVA)
                .collect(Collectors.toList());
    }
}
package biblioteca.UI;

import biblioteca.fachada.Fachada;
import biblioteca.negocios.entidade.Emprestimo;
import biblioteca.negocios.entidade.ItemDoAcervo;
import biblioteca.negocios.entidade.Reserva;

import java.util.List;
import java.util.Map;

public class TelaGerarRelatorios {

    public static void exibir() {
        boolean executando = true;
        while(executando) {
            UIUtils.exibirCabecalho("GERAR RELATÓRIOS");
            System.out.println("[1] Relatório de Empréstimos em Atraso");
            System.out.println("[2] Relatório de Itens Mais Emprestados");
            System.out.println("[3] Relatório de Reservas Ativas");
            System.out.println("[0] Voltar ao Menu Principal");

            int opcao = UIUtils.lerOpcao();

            switch (opcao) {
                case 1 -> relatorioAtrasos();
                case 2 -> relatorioMaisEmprestados();
                case 3 -> relatorioReservas();
                case 0 -> executando = false;
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void relatorioAtrasos() {
        UIUtils.exibirCabecalho("RELATÓRIO DE ATRASOS");
        List<Emprestimo> atrasos = Fachada.getInstance().gerarRelatorioDeAtrasos();
        if (atrasos.isEmpty()) {
            System.out.println("Nenhum empréstimo em atraso no momento.");
        } else {
            atrasos.forEach(e -> System.out.printf("- Item: %s | Membro: %s (CPF: %s) | Previsto: %s\n",
                    e.getItemDoAcervo().getTitulo(),
                    e.getMembro().getNome(),
                    e.getMembro().getCpf(),
                    e.getDataDevolucaoPrevista()));
        }
    }

    private static void relatorioMaisEmprestados() {
        UIUtils.exibirCabecalho("RELATÓRIO DE ITENS MAIS EMPRESTADOS");
        Map<ItemDoAcervo, Long> ranking = Fachada.getInstance().gerarRelatorioDeItensMaisEmprestados();
        if (ranking.isEmpty()) {
            System.out.println("Nenhum empréstimo foi realizado ainda.");
        } else {
            ranking.entrySet().stream()
                    .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                    .forEach(entry -> System.out.printf("- %d vez(es): %s (ID: %d)\n",
                            entry.getValue(),
                            entry.getKey().getTitulo(),
                            entry.getKey().getId()));
        }
    }

    private static void relatorioReservas() {
        UIUtils.exibirCabecalho("RELATÓRIO DE RESERVAS ATIVAS");
        List<Reserva> reservas = Fachada.getInstance().gerarRelatorioDeReservasAtivas();
        if (reservas.isEmpty()) {
            System.out.println("Nenhuma reserva ativa no momento.");
        } else {
            reservas.forEach(r -> System.out.printf("- Item: %s | Membro: %s (CPF: %s) | Data Reserva: %s\n",
                    r.getItemDoAcervo().getTitulo(),
                    r.getMembro().getNome(),
                    r.getMembro().getCpf(),
                    r.getDataReserva()));
        }
    }
}

package biblioteca.UI;

import biblioteca.fachada.Fachada;
import biblioteca.negocios.entidade.Emprestimo;
import biblioteca.negocios.entidade.Membro;
import biblioteca.negocios.entidade.Multa;
import biblioteca.negocios.entidade.Reserva;

import java.util.List;

public class TelaMinhaConta {

    public static void exibir(Membro membro) {
        boolean executando = true;
        while (executando) {
            UIUtils.exibirCabecalho("MINHA CONTA");
            System.out.println("[1] Ver Empréstimos Ativos");
            System.out.println("[2] Ver Multas Pendentes");
            System.out.println("[3] Ver Reservas Ativas");
            System.out.println("[0] Voltar ao Menu");

            int opcao = UIUtils.lerOpcao();

            switch (opcao) {
                case 1 -> verEmprestimos(membro);
                case 2 -> verMultas(membro);
                case 3 -> verReservas(membro);
                case 0 -> executando = false;
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void verEmprestimos(Membro membro) {
        UIUtils.exibirCabecalho("MEUS EMPRÉSTIMOS ATIVOS");
        List<Emprestimo> emprestimos = Fachada.getInstance().listarEmprestimosAtivosPorMembro(membro);
        if (emprestimos.isEmpty()) {
            System.out.println("Você não possui empréstimos ativos.");
        } else {
            emprestimos.forEach(e -> System.out.printf("- Item: %s | Devolução Prevista: %s\n",
                    e.getItemDoAcervo().getTitulo(), e.getDataDevolucaoPrevista()));
        }
    }

    private static void verMultas(Membro membro) {
        UIUtils.exibirCabecalho("MINHAS MULTAS PENDENTES");
        List<Multa> multas = Fachada.getInstance().debitosPendentes(membro);
        if (multas.isEmpty()) {
            System.out.println("Você não possui multas pendentes.");
        } else {
            multas.forEach(m -> System.out.printf("- Valor: R$%.2f | Referente a: %s\n",
                    m.getValor(), m.getEmprestimo().getItemDoAcervo().getTitulo()));
        }
    }

    private static void verReservas(Membro membro) {
        UIUtils.exibirCabecalho("MINHAS RESERVAS ATIVAS");
        List<Reserva> reservas = Fachada.getInstance().listarReservasAtivasPorMembro(membro);
        if (reservas.isEmpty()) {
            System.out.println("Você não possui reservas ativas.");
        } else {
            reservas.forEach(r -> System.out.printf("- Item: %s | Data da Reserva: %s\n",
                    r.getItemDoAcervo().getTitulo(), r.getDataReserva()));
        }
    }
}

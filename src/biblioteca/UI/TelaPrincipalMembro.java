package biblioteca.UI;

import biblioteca.fachada.Fachada;
import biblioteca.negocios.entidade.Membro;

public class TelaPrincipalMembro {

    public static void exibir(Membro membro) {
        boolean executando = true;
        while (executando) {
            UIUtils.exibirCabecalho("MENU DO MEMBRO - Bem-vindo(a), " + membro.getNome());
            System.out.println("[1] Buscar Itens no Acervo");
            System.out.println("[2] Minha Conta (Empréstimos, Multas, Reservas)");
            System.out.println("[0] Logout");

            int opcao = UIUtils.lerOpcao();

            switch (opcao) {
                case 1 -> TelaBuscarAcervo.exibir(membro);
                case 2 -> TelaMinhaConta.exibir(membro);
                case 0 -> { // SINTAXE CORRIGIDA
                    System.out.println("Até logo!");
                    executando = false;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }
}
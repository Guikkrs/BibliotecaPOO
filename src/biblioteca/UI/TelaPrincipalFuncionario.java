package biblioteca.UI;

import biblioteca.fachada.Fachada;
import biblioteca.negocios.entidade.Funcionario;

public class TelaPrincipalFuncionario {
    public static void exibir(Funcionario funcionario) {
        boolean executando = true;
        while (executando) {
            UIUtils.exibirCabecalho("MENU PRINCIPAL - " + funcionario.getPermissao());
            System.out.println("[1] Gerenciar Acervo");
            System.out.println("[2] Gerenciar Pessoas");
            System.out.println("[3] Gerenciar Empréstimos");
            System.out.println("[4] Gerenciar Caixa");
            System.out.println("[5] Gerar Relatórios");
            System.out.println("[0] Logout");

            int opcao = UIUtils.lerOpcao();

            switch (opcao) {
                // Navega para as outras telas
                case 1:
                    TelaGerenciarAcervo.exibir();
                    break;
                case 2:
                    TelaGerenciarPessoas.exibir();
                    break;
                case 3:
                    TelaGerenciarEmprestimos.exibir();
                    break;
                case 4:
                    TelaGerenciarCaixa.exibir();
                    break;
                case 5:
                    TelaGerarRelatorios.exibir();
                    break;
                case 0:
                    Fachada.getInstance().logout();
                    System.out.println("Logout realizado com sucesso.");
                    executando = false;
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }
}
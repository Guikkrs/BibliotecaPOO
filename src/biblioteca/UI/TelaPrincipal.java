package biblioteca.UI;

import biblioteca.fachada.Fachada;
import biblioteca.negocios.entidade.Funcionario;
import biblioteca.negocios.entidade.Membro;

public class TelaPrincipal {

    public static void main(String[] args) {
        // Inicializa a fachada para carregar os dados dos arquivos .dat
        Fachada fachada = Fachada.getInstance();
        System.out.println("=== Bem-vindo ao Sistema de Biblioteca ===");
        System.out.println("Dados carregados. O sistema está pronto.");

        while (true) {
            // Verifica o estado da sessão através da fachada
            Funcionario funcionarioLogado = fachada.getFuncionarioLogado();
            Membro membroLogado = fachada.getMembroLogado();

            if (funcionarioLogado == null && membroLogado == null) {
                // Se ninguém estiver logado, exibe o menu público
                exibirMenuPublico();
            } else if (funcionarioLogado != null) {
                // Se um funcionário estiver logado, exibe o seu menu principal
                TelaPrincipalFuncionario.exibir(funcionarioLogado);
            } else if (membroLogado != null) {
                // Se um membro estiver logado, exibe o seu menu principal
                TelaPrincipalMembro.exibir(membroLogado);
            }
        }
    }

    /**
     * Exibe as opções disponíveis para um utilizador não autenticado.
     */
    private static void exibirMenuPublico() {
        UIUtils.exibirCabecalho("MENU PÚBLICO");
        System.out.println("[1] Login como Funcionário");
        System.out.println("[2] Login como Membro");
        System.out.println("[3] Cadastrar-se como Novo Membro");
        System.out.println("[0] Sair do Sistema");

        int opcao = UIUtils.lerOpcao();
        switch (opcao) {
            case 1:
                TelaLogin.exibir(); // Tenta o login do funcionário
                break;
            case 2:
                TelaLoginMembro.exibir(); // Tenta o login do membro
                break;
            case 3:
                TelaCadastroMembro.exibir();
                break;
            case 0:
                System.out.println("A encerrar o sistema...");
                System.exit(0); // Termina a aplicação
                break;
            default:
                System.out.println("Opção inválida.");
        }
    }
}

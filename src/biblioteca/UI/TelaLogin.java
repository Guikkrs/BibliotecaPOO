package biblioteca.UI;

import biblioteca.fachada.Fachada;
import biblioteca.negocios.entidade.Funcionario;
import biblioteca.negocios.excecoes.login.CredenciaisInvalidasException;

/**
 * Tela responsável por obter as credenciais de um funcionário e
 * tentar autenticá-lo no sistema.
 */
public class TelaLogin {

    public static Funcionario exibir() {
        UIUtils.exibirCabecalho("LOGIN DE FUNCIONÁRIO");

        String login = UIUtils.lerString("Login: ");
        String senha = UIUtils.lerString("Senha: ");

        try {
            // Delega a lógica de login para a fachada
            Fachada.getInstance().login(login, senha);
            System.out.println("Login bem-sucedido!");

            // Se o login for bem-sucedido, retorna o funcionário logado
            return Fachada.getInstance().getFuncionarioLogado();

        } catch (CredenciaisInvalidasException e) {
            System.out.println("ERRO: " + e.getMessage());
            return null; // Retorna null em caso de falha no login
        }
    }
}
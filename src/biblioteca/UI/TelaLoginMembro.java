package biblioteca.UI;

import biblioteca.fachada.Fachada;
import biblioteca.negocios.entidade.Membro;
import biblioteca.negocios.excecoes.login.CredenciaisInvalidasException;

public class TelaLoginMembro {

    public static Membro exibir() {
        UIUtils.exibirCabecalho("LOGIN DE MEMBRO");

        String login = UIUtils.lerString("Utilizador: ");
        String senha = UIUtils.lerString("Senha: ");

        try {
            Fachada fachada = Fachada.getInstance();
            fachada.loginMembro(login, senha); // Método a ser criado na fachada
            System.out.println("Login bem-sucedido!");
            return fachada.getMembroLogado(); // Método a ser criado na fachada
        } catch (CredenciaisInvalidasException e) {
            System.out.println("ERRO: " + e.getMessage());
            return null;
        }
    }
}

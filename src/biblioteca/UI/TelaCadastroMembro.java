package biblioteca.UI;

import biblioteca.fachada.Fachada;
import biblioteca.negocios.entidade.Membro;
import biblioteca.negocios.enums.Permissao;
import biblioteca.negocios.excecoes.pessoa.CpfJaExistenteException;
import biblioteca.negocios.excecoes.validacao.ValidacaoException;

public class TelaCadastroMembro {

    public static void exibir() {
        UIUtils.exibirCabecalho("CADASTRO DE NOVO MEMBRO");
        try {
            String nome = UIUtils.lerString("Nome Completo: ");
            String cpf = UIUtils.lerString("CPF: ");
            String telefone = UIUtils.lerString("Telefone: ");
            int idade = UIUtils.lerInt("Idade: ");
            String login = UIUtils.lerString("Crie um nome de utilizador (login): ");
            String senha = UIUtils.lerString("Crie uma senha: ");

            Membro novoMembro = new Membro(nome, cpf, telefone, idade, login, senha, Permissao.MEMBRO);
            Fachada.getInstance().cadastrarMembro(novoMembro);
            System.out.println("\nSUCESSO: Cadastro realizado! Agora pode fazer o login.");

        } catch (CpfJaExistenteException | ValidacaoException e) {
            System.out.println("ERRO DE CADASTRO: " + e.getMessage());
        }
    }
}

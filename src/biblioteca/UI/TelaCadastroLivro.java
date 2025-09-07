package biblioteca.UI;

import biblioteca.fachada.Fachada;
import biblioteca.negocios.entidade.Autor;
import biblioteca.negocios.entidade.Livro;
import biblioteca.negocios.enums.EnumSetor;
import biblioteca.negocios.excecoes.login.PermissaoInsuficienteException;
import biblioteca.negocios.excecoes.validacao.ValidacaoException;

public class TelaCadastroLivro {

    /**
     * Exibe o formulário para recolher os dados de um novo livro e o cadastra no sistema.
     * Esta tela é chamada a partir do menu de gestão do acervo do funcionário.
     */
    public static void exibir() {
        UIUtils.exibirCabecalho("CADASTRO DE NOVO LIVRO");
        try {
            // Recolha de dados do utilizador através da classe utilitária
            String titulo = UIUtils.lerString("Título: ");
            String nomeAutor = UIUtils.lerString("Nome do autor: ");
            String nacionalidadeAutor = UIUtils.lerString("Nacionalidade do autor: ");
            int ano = UIUtils.lerInt("Ano de publicação: ");
            String setorStr = UIUtils.lerString("Setor (LITERATURA_NACIONAL, TECNOLOGIA, etc.): ");
            int paginas = UIUtils.lerInt("Número de páginas: ");
            String isbn = UIUtils.lerString("ISBN: ");
            int quantidade = UIUtils.lerInt("Quantidade de cópias: ");

            // Criação dos objetos necessários
            Autor autor = new Autor(nomeAutor, nacionalidadeAutor);
            EnumSetor setor = EnumSetor.valueOf(setorStr.trim().toUpperCase());
            Livro novoLivro = new Livro(titulo, autor, ano, setor, paginas, isbn, quantidade);

            // Chamada à Fachada para executar a operação de negócio
            Fachada.getInstance().cadastrarLivro(novoLivro);
            System.out.println("\nSUCESSO: Livro cadastrado!");

        } catch (IllegalArgumentException e) {
            // Captura erro na conversão do Enum
            System.out.println("\nERRO: Setor inválido. Use um dos valores válidos: " + java.util.Arrays.toString(EnumSetor.values()));
        } catch (ValidacaoException | PermissaoInsuficienteException e) {
            // Captura exceções de negócio personalizadas
            System.out.println("\nERRO: " + e.getMessage());
        }
    }
}

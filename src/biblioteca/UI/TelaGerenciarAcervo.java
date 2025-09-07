package biblioteca.UI;

import biblioteca.fachada.Fachada;
import biblioteca.negocios.entidade.Autor;
import biblioteca.negocios.entidade.Livro;
import biblioteca.negocios.enums.EnumSetor;
import biblioteca.negocios.excecoes.acervo.ItemComPendenciasException;
import biblioteca.negocios.excecoes.itemDoAcervo.ItemNaoEncontradoException;
import biblioteca.negocios.excecoes.login.PermissaoInsuficienteException;
import biblioteca.negocios.excecoes.validacao.ValidacaoException;

public class TelaGerenciarAcervo {

    public static void exibir() {
        boolean executando = true;
        while(executando) {
            UIUtils.exibirCabecalho("GERENCIAR ACERVO");
            System.out.println("[1] Cadastrar Novo Livro");
            System.out.println("[2] Adicionar Cópias de um Item");
            System.out.println("[3] Remover Item do Acervo");
            System.out.println("[0] Voltar ao Menu Principal");

            int opcao = UIUtils.lerOpcao();

            switch (opcao) {
                case 1 -> cadastrarLivro();
                case 2 -> adicionarCopias();
                case 3 -> removerItem();
                case 0 -> executando = false;
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void cadastrarLivro() {
        UIUtils.exibirCabecalho("CADASTRO DE NOVO LIVRO");
        try {
            String titulo = UIUtils.lerString("Título: ");
            String nomeAutor = UIUtils.lerString("Nome do autor: ");
            String nacionalidadeAutor = UIUtils.lerString("Nacionalidade do autor: ");
            int ano = UIUtils.lerInt("Ano de publicação: ");
            String setorStr = UIUtils.lerString("Setor (LITERATURA_NACIONAL, TECNOLOGIA, etc.): ");
            int paginas = UIUtils.lerInt("Número de páginas: ");
            String isbn = UIUtils.lerString("ISBN: ");
            int quantidade = UIUtils.lerInt("Quantidade de cópias: ");

            Autor autor = new Autor(nomeAutor, nacionalidadeAutor);
            EnumSetor setor = EnumSetor.valueOf(setorStr.toUpperCase());
            Livro novoLivro = new Livro(titulo, autor, ano, setor, paginas, isbn, quantidade);

            Fachada.getInstance().cadastrarLivro(novoLivro);
            System.out.println("SUCESSO: Livro cadastrado!");

        } catch (IllegalArgumentException e) {
            System.out.println("ERRO: Setor inválido. Use um dos valores válidos: " + java.util.Arrays.toString(EnumSetor.values()));
        } catch (ValidacaoException | PermissaoInsuficienteException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    private static void adicionarCopias() {
        UIUtils.exibirCabecalho("ADICIONAR CÓPIAS");
        try {
            int id = UIUtils.lerInt("ID do item: ");
            int quantidade = UIUtils.lerInt("Quantidade a adicionar: ");
            Fachada.getInstance().adicionarCopias(id, quantidade);
            System.out.println("SUCESSO: Cópias adicionadas!");
        } catch (ItemNaoEncontradoException | ValidacaoException | PermissaoInsuficienteException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    private static void removerItem() {
        UIUtils.exibirCabecalho("REMOVER ITEM");
        try {
            int id = UIUtils.lerInt("ID do item a ser removido: ");
            Fachada.getInstance().removerItem(id);
            System.out.println("SUCESSO: Item removido!");
        } catch (ItemNaoEncontradoException | ItemComPendenciasException | PermissaoInsuficienteException | ValidacaoException e) { // CORREÇÃO AQUI
            System.out.println("ERRO: " + e.getMessage());
        }
    }
}
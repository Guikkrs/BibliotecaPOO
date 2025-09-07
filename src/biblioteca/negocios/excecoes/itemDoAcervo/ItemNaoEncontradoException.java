package biblioteca.negocios.excecoes.itemDoAcervo;

public class ItemNaoEncontradoException extends Exception {

    public ItemNaoEncontradoException(String identificador) {
        super("O item com o identificador '" + identificador + "' não foi encontrado no acervo.");
    }

    // CONSTRUTOR ADICIONADO
    public ItemNaoEncontradoException(int id) {
        super("O item com ID '" + id + "' não foi encontrado no acervo.");
    }
}

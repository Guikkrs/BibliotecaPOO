package biblioteca.negocios.excecoes;

public class ItemNaoEncontradoException extends Exception {
    public ItemNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}

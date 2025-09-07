package biblioteca.negocios.excecoes.acervo;

/**
 * Exceção lançada ao tentar realizar uma operação (ex: empréstimo)
 * com um item que não possui cópias disponíveis.
 */
public class ItemNaoDisponivelException extends Exception {
    public ItemNaoDisponivelException(String tituloItem) {
        super("O item '" + tituloItem + "' não possui cópias disponíveis para empréstimo.");
    }
}

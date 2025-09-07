package biblioteca.negocios.excecoes.acervo;

/**
 * Exceção lançada ao tentar remover um item do acervo que ainda
 * possui cópias emprestadas.
 */
public class ItemComPendenciasException extends Exception {
    public ItemComPendenciasException(String tituloItem) {
        super("O item '" + tituloItem + "' não pode ser removido pois possui cópias emprestadas.");
    }
}

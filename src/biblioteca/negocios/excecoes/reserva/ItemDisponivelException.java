package biblioteca.negocios.excecoes.reserva;

/**
 * Exceção lançada ao tentar reservar um item que já está
 * disponível para empréstimo direto.
 */
public class ItemDisponivelException extends Exception {
    public ItemDisponivelException(String tituloItem) {
        super("O item '" + tituloItem + "' já está disponível e não precisa ser reservado.");
    }
}

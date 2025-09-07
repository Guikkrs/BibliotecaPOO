package biblioteca.negocios.excecoes.reserva;

/**
 * Exceção lançada quando um membro tenta fazer uma segunda
 * reserva para o mesmo item.
 */
public class ReservaDuplicadaException extends Exception {
    public ReservaDuplicadaException(String nomeMembro, String tituloItem) {
        super("O membro '" + nomeMembro + "' já possui uma reserva ativa para o item '" + tituloItem + "'.");
    }
}

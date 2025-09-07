package biblioteca.negocios.excecoes.pessoa;

/**
 * Exceção lançada ao tentar remover um membro que ainda possui
 * empréstimos ativos ou multas pendentes.
 */
public class MembroComPendenciasException extends Exception {
    public MembroComPendenciasException(String nomeMembro) {
        super("O membro '" + nomeMembro + "' não pode ser removido pois possui pendências (empréstimos ou multas).");
    }
}

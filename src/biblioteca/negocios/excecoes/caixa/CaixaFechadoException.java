package biblioteca.negocios.excecoes.caixa;

/**
 * Exceção lançada ao tentar realizar uma operação financeira
 * (ex: receber multa) quando não há um caixa aberto.
 */
public class CaixaFechadoException extends Exception {
    public CaixaFechadoException() {
        super("A operação não pode ser realizada pois não há um caixa aberto.");
    }
}

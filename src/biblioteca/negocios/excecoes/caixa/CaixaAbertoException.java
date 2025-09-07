package biblioteca.negocios.excecoes.caixa;

/**
 * Exceção lançada ao tentar abrir um novo caixa quando
 * já existe um em aberto.
 */
public class CaixaAbertoException extends Exception {
    public CaixaAbertoException() {
        super("Não é possível abrir um novo caixa pois já existe um em aberto.");
    }
}

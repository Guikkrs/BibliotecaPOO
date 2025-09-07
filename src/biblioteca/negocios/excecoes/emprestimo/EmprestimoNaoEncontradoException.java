package biblioteca.negocios.excecoes.emprestimo;

/**
 * Exceção lançada ao tentar operar sobre um empréstimo que não foi encontrado
 * para um determinado membro e item.
 */
public class EmprestimoNaoEncontradoException extends Exception {
    public EmprestimoNaoEncontradoException() {
        super("Nenhum empréstimo ativo foi encontrado para os dados informados.");
    }
}
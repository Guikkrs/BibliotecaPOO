package biblioteca.negocios.excecoes.emprestimo;

/**
 * Exceção lançada quando um membro tenta realizar um novo empréstimo
 * mas já atingiu o número máximo de itens permitidos.
 */
public class LimiteDeEmprestimosAtingidoException extends Exception {
    public LimiteDeEmprestimosAtingidoException(String nomeMembro, int limite) {
        super("O membro '" + nomeMembro + "' já atingiu o limite de " + limite + " empréstimos simultâneos.");
    }
}
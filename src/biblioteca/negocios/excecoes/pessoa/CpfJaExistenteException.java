package biblioteca.negocios.excecoes.pessoa;

/**
 * Exceção lançada ao tentar cadastrar uma Pessoa (Membro ou Funcionário)
 * com um CPF que já está em uso no sistema.
 */
public class CpfJaExistenteException extends Exception {
    public CpfJaExistenteException(String cpf) {
        super("O CPF '" + cpf + "' já está cadastrado no sistema.");
    }
}

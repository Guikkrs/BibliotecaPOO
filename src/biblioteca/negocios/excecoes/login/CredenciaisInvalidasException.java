package biblioteca.negocios.excecoes.login;

/**
 * Exceção lançada quando a combinação de login e senha
 * não corresponde a nenhum funcionário no sistema.
 */
public class CredenciaisInvalidasException extends Exception {
    public CredenciaisInvalidasException() {
        super("Login ou senha inválidos.");
    }
}

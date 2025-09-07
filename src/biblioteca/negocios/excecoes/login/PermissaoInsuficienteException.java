package biblioteca.negocios.excecoes.login;

/**
 * Exceção lançada quando um funcionário tenta executar uma ação
 * para a qual não tem o nível de permissão necessário.
 */
public class PermissaoInsuficienteException extends Exception {
    public PermissaoInsuficienteException() {
        super("Permissão insuficiente para realizar esta operação.");
    }
}

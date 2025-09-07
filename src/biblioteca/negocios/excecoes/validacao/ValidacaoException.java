package biblioteca.negocios.excecoes.validacao;

/**
 * Exceção lançada quando os dados fornecidos para uma operação
 * (ex: cadastro) violam as regras de validação definidas no sistema.
 */
public class ValidacaoException extends Exception {
    public ValidacaoException(String mensagem) {
        super(mensagem);
    }
}
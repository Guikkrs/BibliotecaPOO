package biblioteca.negocios.excecoes.pessoa;

/**
 * Exceção lançada quando uma busca por uma Pessoa (Membro ou Funcionário)
 * não retorna resultados.
 */
public class PessoaNaoEncontradaException extends Exception {
    public PessoaNaoEncontradaException(String identificador) {
        super("Nenhuma pessoa encontrada com o identificador: " + identificador);
    }
}
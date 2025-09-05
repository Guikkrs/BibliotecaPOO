package biblioteca.negocios.excecoes;

public class MembroComDebitoException extends Exception {

    public MembroComDebitoException() {
        super();
    }

    public MembroComDebitoException(String mensagem) {
        super(mensagem);
    }

    public MembroComDebitoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}

package biblioteca.Negocio;

public class Setor implements java.io.Serializable {
    private String nome;

    public Setor(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return "nome do setor: " + nome + '\'';
    }


}
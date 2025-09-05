package biblioteca.negocios;

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

    @Override
    public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Setor)) return false;
    Setor setor = (Setor) o;
    return nome.equalsIgnoreCase(setor.nome);
    }

    @Override
    public int hashCode() {
    return nome.toLowerCase().hashCode();
    }

}
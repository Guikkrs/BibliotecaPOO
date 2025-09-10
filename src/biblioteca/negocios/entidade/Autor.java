package biblioteca.negocios.entidade;

import java.util.Objects;

public class Autor implements java.io.Serializable {

    private int id; // ADICIONADO
    private String nome;
    private String nacionalidade;


    public Autor(String nome, String nacionalidade) {
        this.nome = nome;
        this.nacionalidade = nacionalidade;
    }

    // GETTERS E SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public String getNacionalidade() {
        return this.nacionalidade;
    }

    @Override
    public String toString() {
        return this.nome + " (" + this.nacionalidade + ")";
    }

    /**
     * MODIFICAÇÃO: A igualdade agora é baseada estritamente no ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Autor autor = (Autor) o;
        if (this.id == 0 || autor.id == 0) return false;
        return id == autor.id;
    }

    /**
     * MODIFICAÇÃO: O hashCode agora é baseado no ID.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
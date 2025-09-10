package biblioteca.negocios.entidade;

import java.util.Objects;

/**
 * Representa uma categoria ou seção da biblioteca onde os itens do acervo são organizados.
 * A classe é Serializable para permitir a persistência de seus objetos.
 */
public class Setor implements java.io.Serializable {

    private int id; // Identificador único para a persistência
    private String nome;

    public Setor(String nome) {
        this.nome = nome;
    }

    // GETTERS E SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    // MÉTODOS PADRÃO (EQUALS, HASHCODE, TOSTRING)

    /**
     * MODIFICAÇÃO: A igualdade agora é baseada estritamente no ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Setor setor = (Setor) o;
        if (this.id == 0 || setor.id == 0) return false;
        return id == setor.id;
    }

    /**
     * MODIFICAÇÃO: O hashCode agora é baseado no ID.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Setor{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}
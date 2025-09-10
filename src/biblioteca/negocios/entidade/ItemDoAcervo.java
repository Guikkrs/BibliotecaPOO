package biblioteca.negocios.entidade;

import biblioteca.negocios.enums.EnumSetor;
import biblioteca.negocios.enums.EnumStatusItem;
import java.util.Objects; // Adicionar import

public abstract class ItemDoAcervo implements java.io.Serializable {

    private String titulo;
    private Autor autor;
    private int ano;
    private EnumSetor setor;
    private EnumStatusItem status;
    protected int quantidade;
    private int id;

    // Construtor e Getters/Setters permanecem os mesmos...
    public ItemDoAcervo(String titulo, Autor autor, int ano, EnumSetor setor, int quantidade) {
        this.titulo = titulo;
        this.autor = autor;
        this.ano = ano;
        this.setor = setor;
        this.quantidade = quantidade;
        this.status = (quantidade > 0) ? EnumStatusItem.DISPONIVEL : EnumStatusItem.EMPRESTADO;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public int getAno() {
        return ano;
    }

    public EnumSetor getSetor() {
        return setor;
    }

    public EnumStatusItem getStatus() {
        return status;
    }

    public void setStatus(EnumStatusItem novoStatus) {
        this.status = novoStatus;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public boolean verificarDisponibilidade() {
        return this.quantidade > 0;
    }


    /**
     * MODIFICAÇÃO: A igualdade agora é baseada estritamente no ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemDoAcervo that = (ItemDoAcervo) o;
        if (this.id == 0 || that.id == 0) return false;
        return id == that.id;
    }

    /**
     * MODIFICAÇÃO: O hashCode agora é baseado no ID.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
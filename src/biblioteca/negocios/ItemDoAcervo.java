package biblioteca.negocios;

import biblioteca.negocios.enums.EnumSetor;
import biblioteca.negocios.enums.EnumStatusItem;

public abstract class ItemDoAcervo implements java.io.Serializable {

    private String titulo;
    private Autor autor;
    private int ano;
    private EnumSetor setor;
    private EnumStatusItem status;
    protected int quantidade;

    public ItemDoAcervo(String titulo, Autor autor, int ano, EnumSetor setor, int quantidade) {
        this.titulo = titulo;
        this.autor = autor;
        this.ano = ano;
        this.setor = setor;
        this.quantidade = quantidade;
        this.status = (quantidade > 0) ? EnumStatusItem.DISPONIVEL : EnumStatusItem.RESERVADO;
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
}
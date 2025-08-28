package biblioteca.Negocio;

import biblioteca.Enum.EnumSetor;
import biblioteca.Enum.EnumStatusItem;
import biblioteca.Enum.StatusLivro;

public abstract class ItemDoAcervo {

    private String titulo;
    private Autor autor;
    private int ano;
    private EnumSetor setor;
    private EnumStatusItem status; // Usamos o enum para o status
    private int quantidade; // Atributo para controlar o número de cópias

    public ItemDoAcervo(String titulo, Autor autor, int ano, EnumSetor setor) {
        this.titulo = titulo;
        this.autor = autor;
        this.ano = ano;
        this.setor = setor;
        this.status = EnumStatusItem.DISPONIVEL; // Status inicial é DISPONIVEL
        this.quantidade = 0;
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

    // Retorna a quantidade de cópias disponíveis
    public int getQuantidade() {
        return quantidade;
    }

    // Define a quantidade de cópias
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    // Verifica a disponibilidade com base na quantidade
    public boolean verificarDisponibilidade() {
        return this.quantidade > 0;
    }
}

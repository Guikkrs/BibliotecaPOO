package biblioteca.Negocio;

import biblioteca.Enum.EnumSetor;

public class Livro extends ItemDoAcervo {
    
    private int numeroDePaginas;
    private String isbn;

    public Livro(String titulo, Autor autor, int ano, EnumSetor setor, int numeroDePaginas, String isbn) {
        super(titulo, autor, ano, setor);
        this.numeroDePaginas = numeroDePaginas;
        this.isbn = isbn;
    }

    public int getNumeroDePaginas() {
        return this.numeroDePaginas;
    }

    public String getIsbn() {
        return this.isbn;
    }
    
    @Override
    public String toString() {
        return "Livro: " + getTitulo() + " | Autor: " + getAutor().getNome() + " | ISBN: " + this.isbn;
    }
}

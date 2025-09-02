package biblioteca.Negocio;

import biblioteca.Enum.EnumSetor;

public class Livro extends ItemDoAcervo implements java.io.Serializable {
    
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
        return "Livro: " + getTitulo() + " | Autor: " + getAutor().getNome() + " | ISBN: " + this.isbn + " | Ano: " + getAno() + " | Setor: " + getSetor() + " | Páginas: " + this.numeroDePaginas;
    }

    Object getGenero() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String toCSV() {
        return String.join(";",
                getTitulo(),
                getAutor().getNome(),
                String.valueOf(getAno()),
                getSetor().name(),
                String.valueOf(numeroDePaginas),
                isbn
        );
    }

    public static Livro fromCSV(String csvLine, Autor autor) {
        String[] parts = csvLine.split(";");
        String titulo = parts[0];
        int ano = Integer.parseInt(parts[2]);
        EnumSetor setor = EnumSetor.valueOf(parts[3]);
        int paginas = Integer.parseInt(parts[4]);
        String isbn = parts[5];
        return new Livro(titulo, autor, ano, setor, paginas, isbn);
    }

}

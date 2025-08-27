package biblioteca.Negocio;

import biblioteca.Enum.EnumSetor;

public abstract class ItemDoAcervo {
    private String titulo;
    private Autor autor;
    private int ano;
    private EnumSetor setor;
    private boolean emprestado;

    public ItemDoAcervo(String titulo, Autor autor, int ano, EnumSetor setor) {
        this.titulo = titulo;
        this.autor = autor;
        this.ano = ano;
        this.setor = setor;
        this.emprestado = false;
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

    public String getStatus() {
        return emprestado ? "Emprestado" : "Disponível";
    }

    public boolean verificarDisponibilidade() {
        return !emprestado;
    }

    public void marcarComoEmprestado() {
        this.emprestado = true;
    }
}

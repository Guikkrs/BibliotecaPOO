package biblioteca.Negocio;

public class Autor {

    
    private String nome;
    private String nacionalidade;


    public Autor(String nome, String nacionalidade) {
        this.nome = nome;
        this.nacionalidade = nacionalidade;
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
}
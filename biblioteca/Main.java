package biblioteca;

import biblioteca.Enum.EnumSetor;
import biblioteca.Negocio.Autor;
import biblioteca.Negocio.ItemDoAcervo;

class Livro extends ItemDoAcervo {
    
    public Livro(String titulo, Autor autor, int ano, EnumSetor setor) {
        super(titulo, autor, ano, setor);
    }
}

public class Main {
    public static void main(String[] args) {

        Autor autor = new Autor("Machado de Assis", "Brasileiro");

        Livro livro = new Livro("Dom Casmurro", autor, 1899, EnumSetor.LITERATURA_NACIONAL);

        System.out.println("\nLivro criado: " + livro.getTitulo());
        System.out.println("Autor do livro: " + livro.getAutor().getNome());
        System.out.println("Status inicial: " + livro.getStatus());
        System.out.println("Disponível? " + livro.verificarDisponibilidade());

        System.out.println("\n... Emprestando o livro ...\n");
        livro.marcarComoEmprestado();

        System.out.println("Status final: " + livro.getStatus());
        System.out.println("Disponível? " + livro.verificarDisponibilidade());

        System.out.println("\n>>> Teste básico finalizado <<<");
    }
}

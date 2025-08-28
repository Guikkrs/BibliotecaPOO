package biblioteca.UI;

import java.time.LocalDate;
import java.util.List;

import biblioteca.Negocio.Autor;
import biblioteca.Negocio.Biblioteca;
import biblioteca.Negocio.Emprestimo;
import biblioteca.Negocio.ItemDoAcervo;
import biblioteca.Negocio.Livro;
import biblioteca.Negocio.Membro;
import biblioteca.Enum.Permissao;
import biblioteca.Enum.StatusEmprestimo;
import biblioteca.Enum.StatusLivro;
import biblioteca.Enum.StatusMulta;
import biblioteca.Negocio.Multa;


public class Main {
    public static void main(String[] args) {

        Biblioteca minhaBiblioteca = new Biblioteca();

        Membro novoMembro = new Membro("Ana Souza", "111.222.333-44", "99999-8888", 28, "aninha", "senhaSegura", Permissao.MEMBRO);
        
        Autor autorLivro = new Autor("Machado de Assis");
        Livro novoLivro = new Livro("Dom Casmurro", autorLivro, 1899, null, 256, "978-85-8041-000-0");

        minhaBiblioteca.adicionarMembro(novoMembro);
        minhaBiblioteca.adicionarItem(novoLivro, 1);
        
        System.out.println("Status inicial do livro: " + (novoLivro.getStatus()));
        System.out.println("Tentando realizar um empréstimo...");

        boolean sucessoEmprestimo = minhaBiblioteca.realizarEmprestimo(novoMembro, novoLivro);
        
        if (sucessoEmprestimo) {
            System.out.println("Empréstimo realizado com sucesso!");
        } else {
            System.out.println("Falha ao realizar o empréstimo. Verifique as regras de negócio.");
        }
        
        List<Emprestimo> emprestimosAtivos = minhaBiblioteca.consultarEmprestimos(novoMembro);
        if (!emprestimosAtivos.isEmpty()) {
            Emprestimo emprestimoParaDevolver = emprestimosAtivos.get(0);
            
            System.out.println("Realizando devolução do livro...");
            minhaBiblioteca.realizarDevolucao(emprestimoParaDevolver);
            
            System.out.println("Devolução concluída. Status final do livro: " + (novoLivro.getStatus()));
        }

        List<Livro> livrosEncontrados = minhaBiblioteca.buscarLivroPorAutor("Machado de Assis");
        System.out.println("\nLivros encontrados por Machado de Assis:");
        for(Livro livro : livrosEncontrados) {
            System.out.println("- " + livro.getTitulo());
        }
    }
}
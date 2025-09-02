package biblioteca.UI;

import biblioteca.Excecoes.MembroComDebitoException;
import biblioteca.Negocio.Autor;
import biblioteca.Negocio.Biblioteca;
import biblioteca.Negocio.Livro;
import biblioteca.Negocio.Membro;
import biblioteca.Enum.Permissao;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        Biblioteca minhaBiblioteca = Biblioteca.getInstance();

        System.out.println("Sistema de Biblioteca iniciado.");

        Membro membroExistente = minhaBiblioteca.buscarMembroPorCPF("11122233344");

        if (membroExistente == null) {
            System.out.println("Cadastrando novo membro e livro");
            Membro novoMembro = new Membro("Ana Souza", "11122233344", "999998888", 28, "aninha", "senhaSegura", Permissao.MEMBRO);
            minhaBiblioteca.adicionarMembro(novoMembro);

            Autor autorLivro = new Autor("Machado de Assis", "Brasileiro");
            Livro novoLivro = new Livro("Dom Casmurro", autorLivro, 1899, null, 256, "978-85-8041-000-0");
            minhaBiblioteca.adicionarItem(novoLivro, 1);
        }

        Membro membroParaEmprestimo = minhaBiblioteca.buscarMembroPorCPF("11122233344");
        List<Livro> livrosDoAutor = minhaBiblioteca.buscarLivroPorAutor("Machado de Assis");

        if (membroParaEmprestimo != null && !livrosDoAutor.isEmpty()) {
            Livro livroParaEmprestar = livrosDoAutor.get(0);

            try {
                System.out.println("Tentando realizar um emprestimo");
                boolean sucesso = minhaBiblioteca.realizarEmprestimo(membroParaEmprestimo, livroParaEmprestar);
                if (sucesso) {
                    System.out.println("Emprestimo realizado com sucesso!");
                } else {
                    System.out.println("Nao foi possivel realizar o emprestimo. O livro pode nao estar disponivel.");
                }
            } catch (MembroComDebitoException e) {
                System.err.println("Erro ao realizar emprestimo: " + e.getMessage());
            }
        }

        System.out.println("\n--- Fim da execucao ---");

        minhaBiblioteca.salvar();
        System.out.println("Dados salvos com sucesso.");
    }
}

package biblioteca.repositorios;

import java.io.*;
import java.util.*;
import biblioteca.Negocio.Livro;
import biblioteca.Negocio.Autor;
import biblioteca.Enum.EnumSetor;

public class RepositorioLivroCSV {

    private List<Livro> livros = new ArrayList<>();
    private final String arquivo = "livros.csv";

    public RepositorioLivroCSV() {
        carregar();
    }

    // Adiciona um livro ao repositório, evitando duplicatas
    public void adicionar(Livro livro) {
        if (!livros.contains(livro)) { 
            livros.add(livro);
            salvar();
        }
    }

    public Optional<Livro> buscarPorTitulo(String titulo) {
        return livros.stream()
                     .filter(l -> l.getTitulo().equalsIgnoreCase(titulo))
                     .findFirst();
    }

    // Lista todos os livros
    public List<Livro> listarTodos() {
        return new ArrayList<>(livros); // retorna cópia da lista
    }

    // Salva todos os livros no CSV
    private void salvar() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(arquivo))) {
            for (Livro l : livros) {
                pw.println(
                    l.getTitulo() + "," +
                    l.getAutor().getNome() + "," +
                    l.getAutor().getNacionalidade() + "," +
                    l.getAno() + "," +
                    l.getSetor() + "," +
                    l.getNumeroDePaginas() + "," +
                    l.getIsbn()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void carregar() {
        livros.clear();
        File f = new File(arquivo);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] campos = linha.split(",");
                if (campos.length < 7) continue;

                String titulo = campos[0];
                String nome = campos[1];
                String nacionalidade = campos[2];
                Autor autor = new Autor(nome, nacionalidade);
                int ano = Integer.parseInt(campos[3]);
                EnumSetor setor = EnumSetor.valueOf(campos[4]);
                int numeroDePaginas = Integer.parseInt(campos[5]);
                String isbn = campos[6];

                Livro l = new Livro(titulo, autor, ano, setor, numeroDePaginas, isbn);
                if (!livros.contains(l)) {
                    livros.add(l);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

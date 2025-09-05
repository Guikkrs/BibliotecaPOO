package biblioteca.dados;

import biblioteca.negocios.Autor;
import biblioteca.negocios.Livro;
import biblioteca.negocios.enums.EnumSetor;

import java.io.*;
import java.util.*;

public class RepositorioLivroCSV implements Repositorio<Livro> {

    private List<Livro> livros = new ArrayList<>();
    private final String arquivo = "livros.csv";

    public RepositorioLivroCSV() {
        carregar();
    }

    // Adiciona um livro ao repositório, evitando duplicatas
    public void salvar(Livro livro) {
        if (!livros.contains(livro)) {
            livros.add(livro);
            salvarCSV();
        }
    }

    public Optional<Livro> buscarPorTitulo(String titulo) {
        return livros.stream()
                .filter(l -> l.getTitulo().equalsIgnoreCase(titulo))
                .findFirst();
    }

    // Lista todos os livros
    public List<Livro> listar() {
        return new ArrayList<>(livros);
    }

    @Override
    public void atualizar(int index, Livro livro) {
        if (index >= 0 && index < livros.size()) {
            livros.set(index, livro);
            salvarCSV();
        }
    }

    @Override
    public void remover(int index) {
        if (index >= 0 && index < livros.size()) {
            livros.remove(index);
            salvarCSV();
        }
    }

    @Override
    public List<Livro> carregar() {
        livros.clear();
        carregarCSV();
        return new ArrayList<>(livros);
    }

    // Salva todos os livros no CSV
    private void salvarCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(arquivo))) {
            for (Livro l : livros) {
                pw.println(l.toCSV());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //carrega todos os livros no csv
    private void carregarCSV() {
        File file = new File(arquivo);
        if (!file.exists()) {
            return; // não faz nada se o arquivo ainda não existe
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length == 8) { // AGORA TEMOS 8 CAMPOS
                    String titulo = dados[0];
                    String nome = dados[1];
                    String nacionalidade = dados[2];
                    Autor autor = new Autor(nome, nacionalidade);
                    int ano = Integer.parseInt(dados[3]);
                    EnumSetor setor = EnumSetor.valueOf(dados[4]);
                    int paginas = Integer.parseInt(dados[5]);
                    String isbn = dados[6];
                    int quantidade = Integer.parseInt(dados[7]); // NOVO CAMPO: QUANTIDADE

                    livros.add(new Livro(titulo, autor, ano, setor, paginas, isbn, quantidade));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

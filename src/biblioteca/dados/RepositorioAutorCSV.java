package biblioteca.dados;

import biblioteca.Negocio.Autor;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepositorioAutorCSV {
    private List<Autor> autores = new ArrayList<>();
    private final String arquivo = "autores.csv";

    public RepositorioAutorCSV() {
        carregar(); // carrega dados ao iniciar
    }

    public void adicionar(Autor autor) {
        autores.add(autor);
        salvar(); // salva no CSV
    }

    public Optional<Autor> buscarPorNome(String nome) {
        return autores.stream()
                    .filter(a -> a.getNome().equalsIgnoreCase(nome))
                    .findFirst();
    }

    public List<Autor> listarTodos() {
        return new ArrayList<>(autores);
    }

    // Salvar lista em CSV
    private void salvar() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(arquivo))) {
            for (Autor autor : autores) {
                pw.println(autor.getNome()); // se tiver mais campos, separe por vírgula
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Carregar lista do CSV
    private void carregar() {
        autores.clear();
        File f = new File(arquivo);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String nome = linha.trim();
                String nacionalidade = ""; 
                if (!nome.isEmpty()) {
                    autores.add(new Autor(nome, nacionalidade));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


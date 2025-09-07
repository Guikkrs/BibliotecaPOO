package biblioteca.dados.persistencia;

import biblioteca.dados.repositorio.IRepositorioAutor;
import biblioteca.negocios.entidade.Autor;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RepositorioAutorPersistencia implements IRepositorioAutor {

    private ArrayList<Autor> autores;
    private static final String NOME_ARQUIVO = "autores.dat";

    public RepositorioAutorPersistencia() {
        this.autores = new ArrayList<>();
        carregarDoArquivo();
    }

    @Override
    public void adicionar(Autor autor) {
        boolean autorExiste = false;
        for (Autor a : autores) {
            if (a.equals(autor)) {
                autorExiste = true;
                break;
            }
        }
        if (!autorExiste) {
            autores.add(autor);
            salvarNoArquivo();
        }
    }

    @Override
    public void atualizar(Autor autor) {
        for (int i = 0; i < autores.size(); i++) {
            if (autores.get(i).getNome().equalsIgnoreCase(autor.getNome())) {
                autores.set(i, autor);
                salvarNoArquivo();
                return;
            }
        }
    }

    @Override
    public void remover(Autor autor) {
        if (autores.remove(autor)) {
            salvarNoArquivo();
        }
    }

    @Override
    public Autor buscarPorNome(String nome) {
        for (Autor a : autores) {
            if (a.getNome().equalsIgnoreCase(nome)) {
                return a;
            }
        }
        return null;
    }

    @Override
    public List<Autor> listarTodos() {
        return new ArrayList<>(this.autores);
    }

    private void salvarNoArquivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(NOME_ARQUIVO)))) {
            oos.writeObject(this.autores);
        } catch (IOException e) {
            System.err.println("Erro ao salvar autores: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarDoArquivo() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) {
            this.autores = new ArrayList<>();
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(NOME_ARQUIVO)))) {
            this.autores = (ArrayList<Autor>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar autores: " + e.getMessage());
            this.autores = new ArrayList<>();
        }
    }
}
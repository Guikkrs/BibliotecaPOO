package biblioteca.dados.persistencia;

import biblioteca.dados.repositorio.IRepositorioSetor;
import biblioteca.negocios.entidade.Setor;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RepositorioSetorPersistencia implements IRepositorioSetor {
    private ArrayList<Setor> setores;
    private int proximoId = 1;
    private static final String NOME_ARQUIVO = "setores.dat";

    public RepositorioSetorPersistencia() {
        this.setores = new ArrayList<>();
        carregarDoArquivo();
    }

    @Override
    public void adicionar(Setor setor) {
        boolean setorExiste = false;
        for (Setor s : setores) {
            if (s.equals(setor)) {
                setorExiste = true;
                break;
            }
        }

        if (!setorExiste) {
            setor.setId(this.proximoId++);
            this.setores.add(setor);
            salvarNoArquivo();
        }
    }

    @Override
    public void atualizar(Setor setor) {
        for (int i = 0; i < setores.size(); i++) {
            if (setores.get(i).getId() == setor.getId()) {
                setores.set(i, setor);
                salvarNoArquivo();
                return;
            }
        }
    }

    @Override
    public void remover(Setor setor) {
        if (this.setores.remove(setor)) {
            salvarNoArquivo();
        }
    }

    @Override
    public Setor buscarPorNome(String nome) {
        for (Setor s : this.setores) {
            if (s.getNome().equalsIgnoreCase(nome)) {
                return s;
            }
        }
        return null;
    }

    @Override
    public Setor buscarPorId(int id) {
        for (Setor s : this.setores) {
            if (s.getId() == id) {
                return s;
            }
        }
        return null;
    }

    @Override
    public List<Setor> listarTodos() {
        return new ArrayList<>(this.setores);
    }

    private void salvarNoArquivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(NOME_ARQUIVO)))) {
            oos.writeObject(this.setores);
        } catch (IOException e) {
            System.err.println("Erro ao salvar setores: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarDoArquivo() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) {
            this.setores = new ArrayList<>();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(NOME_ARQUIVO)))) {
            this.setores = (ArrayList<Setor>) ois.readObject();
            atualizarProximoId();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar setores: " + e.getMessage());
            this.setores = new ArrayList<>();
        }
    }

    private void atualizarProximoId() {
        if (this.setores.isEmpty()) {
            this.proximoId = 1;
        } else {
            int maiorId = 0;
            for(Setor s : this.setores){
                if(s.getId() > maiorId){
                    maiorId = s.getId();
                }
            }
            this.proximoId = maiorId + 1;
        }
    }
}
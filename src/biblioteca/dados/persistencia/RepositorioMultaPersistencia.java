package biblioteca.dados.persistencia;

import biblioteca.dados.repositorio.IRepositorioMulta;
import biblioteca.negocios.entidade.Multa;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RepositorioMultaPersistencia implements IRepositorioMulta {

    private ArrayList<Multa> multas;
    private int proximoId = 1;
    private static final String NOME_ARQUIVO = "multas.dat";

    public RepositorioMultaPersistencia() {
        this.multas = new ArrayList<>();
        carregarDoArquivo();
    }

    @Override
    public void adicionar(Multa multa) {
        multa.setId(this.proximoId++);
        this.multas.add(multa);
        salvarNoArquivo();
    }

    @Override
    public void atualizar(Multa multa) {
        for (int i = 0; i < multas.size(); i++) {
            if (multas.get(i).getId() == multa.getId()) {
                multas.set(i, multa);
                salvarNoArquivo();
                return;
            }
        }
    }

    @Override
    public void remover(Multa multa) {
        if (this.multas.remove(multa)) {
            salvarNoArquivo();
        }
    }

    @Override
    public Multa buscarPorId(int id) {
        for (Multa m : this.multas) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    @Override
    public List<Multa> listarTodos() {
        return new ArrayList<>(this.multas);
    }

    private void salvarNoArquivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(NOME_ARQUIVO)))) {
            oos.writeObject(this.multas);
        } catch (IOException e) {
            System.err.println("Erro ao salvar multas: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarDoArquivo() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) {
            this.multas = new ArrayList<>();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(NOME_ARQUIVO)))) {
            this.multas = (ArrayList<Multa>) ois.readObject();
            atualizarProximoId();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar multas: " + e.getMessage());
            this.multas = new ArrayList<>();
        }
    }

    private void atualizarProximoId() {
        if (this.multas.isEmpty()) {
            this.proximoId = 1;
        } else {
            int maiorId = 0;
            for(Multa m : this.multas){
                if(m.getId() > maiorId){
                    maiorId = m.getId();
                }
            }
            this.proximoId = maiorId + 1;
        }
    }
}
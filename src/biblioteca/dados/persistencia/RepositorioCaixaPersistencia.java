package biblioteca.dados.persistencia;

import biblioteca.dados.repositorio.IRepositorioCaixa;
import biblioteca.negocios.entidade.Caixa;
import biblioteca.negocios.enums.StatusCaixa;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RepositorioCaixaPersistencia implements IRepositorioCaixa {

    private ArrayList<Caixa> caixas;
    private int proximoId = 1;
    private static final String NOME_ARQUIVO = "caixas.dat";

    public RepositorioCaixaPersistencia() {
        this.caixas = new ArrayList<>();
        carregarDoArquivo();
    }

    @Override
    public void adicionar(Caixa caixa) {
        caixa.setId(this.proximoId++);
        this.caixas.add(caixa);
        salvarNoArquivo();
    }

    @Override
    public void atualizar(Caixa caixa) {
        for (int i = 0; i < caixas.size(); i++) {
            if (caixas.get(i).getId() == caixa.getId()) {
                caixas.set(i, caixa);
                salvarNoArquivo();
                return;
            }
        }
    }

    @Override
    public Caixa buscarPorId(int id) {
        for (Caixa c : this.caixas) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }

    @Override
    public Caixa buscarCaixaAberto() {
        for (Caixa c : this.caixas) {
            if (c.getStatus() == StatusCaixa.ABERTO) {
                return c;
            }
        }
        return null;
    }

    @Override
    public List<Caixa> listarTodos() {
        return new ArrayList<>(this.caixas);
    }

    private void salvarNoArquivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(NOME_ARQUIVO)))) {
            oos.writeObject(this.caixas);
        } catch (IOException e) {
            System.err.println("Erro ao salvar caixas: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarDoArquivo() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(NOME_ARQUIVO)))) {
            this.caixas = (ArrayList<Caixa>) ois.readObject();
            atualizarProximoId();
        } catch (IOException | ClassNotFoundException e) {
            this.caixas = new ArrayList<>();
        }
    }

    private void atualizarProximoId() {
        if (!this.caixas.isEmpty()) {
            this.proximoId = this.caixas.stream().mapToInt(Caixa::getId).max().orElse(0) + 1;
        } else {
            this.proximoId = 1;
        }
    }
}

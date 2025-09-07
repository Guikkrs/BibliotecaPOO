package biblioteca.dados.persistencia;

import biblioteca.dados.repositorio.IRepositorioEmprestimo;
import biblioteca.negocios.entidade.Emprestimo;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RepositorioEmprestimoPersistencia implements IRepositorioEmprestimo {

    private ArrayList<Emprestimo> emprestimos;
    private int proximoId = 1;
    private static final String NOME_ARQUIVO = "emprestimos.dat";

    public RepositorioEmprestimoPersistencia() {
        this.emprestimos = new ArrayList<>();
        carregarDoArquivo();
    }

    @Override
    public void adicionar(Emprestimo emprestimo) {
        emprestimo.setId(this.proximoId++);
        this.emprestimos.add(emprestimo);
        salvarNoArquivo();
    }

    @Override
    public void atualizar(Emprestimo emprestimo) {
        for (int i = 0; i < emprestimos.size(); i++) {
            if (emprestimos.get(i).getId() == emprestimo.getId()) {
                emprestimos.set(i, emprestimo);
                salvarNoArquivo();
                return;
            }
        }
    }

    @Override
    public void remover(Emprestimo emprestimo) {
        this.emprestimos.remove(emprestimo);
        salvarNoArquivo();
    }

    @Override
    public Emprestimo buscarPorId(int id) {
        for (Emprestimo e : emprestimos) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    @Override
    public List<Emprestimo> listarTodos() {
        return new ArrayList<>(this.emprestimos);
    }

    private void salvarNoArquivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(NOME_ARQUIVO)))) {
            oos.writeObject(this.emprestimos);
        } catch (IOException e) {
            System.err.println("Erro ao salvar empréstimos: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarDoArquivo() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) {
            this.emprestimos = new ArrayList<>();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(NOME_ARQUIVO)))) {
            this.emprestimos = (ArrayList<Emprestimo>) ois.readObject();
            atualizarProximoId();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar empréstimos: " + e.getMessage());
            this.emprestimos = new ArrayList<>();
        }
    }

    private void atualizarProximoId() {
        if (this.emprestimos.isEmpty()) {
            this.proximoId = 1;
        } else {
            int maiorId = 0;
            for(Emprestimo e : this.emprestimos){
                if(e.getId() > maiorId){
                    maiorId = e.getId();
                }
            }
            this.proximoId = maiorId + 1;
        }
    }
}
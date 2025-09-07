package biblioteca.dados.persistencia;

import biblioteca.dados.repositorio.IRepositorioFuncionario;
import biblioteca.negocios.entidade.Funcionario;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static biblioteca.negocios.enums.Permissao.ADMINISTRADOR;
import static biblioteca.negocios.enums.Permissao.GERENTE;

public class RepositorioFuncionarioPersistencia implements IRepositorioFuncionario {

    private ArrayList<Funcionario> funcionarios;
    private int proximoId = 1;
    private static final String NOME_ARQUIVO = "funcionarios.dat";

    public RepositorioFuncionarioPersistencia() {
        this.funcionarios = new ArrayList<>();
        carregarDoArquivo();
        if (this.funcionarios.isEmpty()){
            CriarFuncionarioBase();
        }
    }

    @Override
    public void adicionar(Funcionario funcionario) {
        if (buscarPorCpf(funcionario.getCpf()) == null) {
            funcionario.setId(this.proximoId++);
            this.funcionarios.add(funcionario);
            salvarNoArquivo();
        }
    }

    @Override
    public void atualizar(Funcionario funcionario) {
        for (int i = 0; i < funcionarios.size(); i++) {
            if (funcionarios.get(i).getId() == funcionario.getId()) {
                funcionarios.set(i, funcionario);
                salvarNoArquivo();
                return;
            }
        }
    }

    @Override
    public void remover(Funcionario funcionario) {
        if (this.funcionarios.remove(funcionario)) {
            salvarNoArquivo();
        }
    }

    @Override
    public Funcionario buscarPorCpf(String cpf) {
        for (Funcionario f : this.funcionarios) {
            if (f.getCpf().equals(cpf)) {
                return f;
            }
        }
        return null;
    }

    @Override
    public Funcionario autenticar(String login, String senha) {
        for (Funcionario f : this.funcionarios) {
            if (f.getLogin().equals(login) && f.getSenhaHash().equals(senha)) {
                return f;
            }
        }
        return null;
    }

    @Override
    public List<Funcionario> listarTodos() {
        return new ArrayList<>(this.funcionarios);
    }

    private void salvarNoArquivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(NOME_ARQUIVO)))) {
            oos.writeObject(this.funcionarios);
        } catch (IOException e) {
            System.err.println("Erro ao salvar funcionários: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarDoArquivo() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(NOME_ARQUIVO)))) {
            this.funcionarios = (ArrayList<Funcionario>) ois.readObject();
            atualizarProximoId();
        } catch (IOException | ClassNotFoundException e) {
            this.funcionarios = new ArrayList<>();
        }
    }

    private void atualizarProximoId() {
        if (!this.funcionarios.isEmpty()) {
            this.proximoId = this.funcionarios.stream().mapToInt(Funcionario::getId).max().orElse(0) + 1;
        } else {
            this.proximoId = 1;
        }
    }
    public void CriarFuncionarioBase() {
        Funcionario f1 = new Funcionario("admin", "11122233345","(87)996342896",46,"admin","123",ADMINISTRADOR);
        Funcionario f2 = new Funcionario("gerente", "11122233347","(87)996342888",26,"gerente","123",GERENTE);
            this.adicionar(f1);
            this.adicionar(f2);
            System.out.println("Funcionarios Padrao carregados com sucesso");
    }
}
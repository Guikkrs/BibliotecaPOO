package biblioteca.repositorios;

import java.io.*;
import java.util.*;
import biblioteca.Negocio.Funcionario;
import biblioteca.Enum.Permissao;

public class RepositorioFuncionarioCSV implements Repositorio<Funcionario> {

    private List<Funcionario> funcionarios = new ArrayList<>();
    private final String arquivo = "funcionarios.csv";

    public RepositorioFuncionarioCSV() {
        carregar();
    }

    @Override
    public void salvar(Funcionario funcionario) {
        if (!funcionarios.contains(funcionario)) {
            funcionarios.add(funcionario);
            salvarCSV();
        }
    }

    @Override
    public List<Funcionario> listar() {
        return new ArrayList<>(funcionarios);
    }

    @Override
    public void atualizar(int index, Funcionario funcionario) {
        if (index >= 0 && index < funcionarios.size()) {
            funcionarios.set(index, funcionario);
            salvarCSV();
        }
    }

    @Override
    public void remover(int index) {
        if (index >= 0 && index < funcionarios.size()) {
            funcionarios.remove(index);
            salvarCSV();
        }
    }

    @Override
    public List<Funcionario> carregar() {
        funcionarios.clear();
        carregarCSV();
        return new ArrayList<>(funcionarios);
    }

    public Funcionario autenticar(String login, String senha) {
        return funcionarios.stream()
                .filter(f -> f.getLogin().equals(login) && f.getSenhaHash().equals(senha))
                .findFirst()
                .orElse(null);
    }

    private void carregarCSV() {
        File file = new File(arquivo);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length >= 7) {
                    String nome = dados[0];
                    String cpf = dados[1];
                    String telefone = dados[2];
                    int idade = Integer.parseInt(dados[3]);
                    String login = dados[4];
                    String senhaHash = dados[5];
                    Permissao permissao = Permissao.valueOf(dados[6]);

                    funcionarios.add(new Funcionario(nome, cpf, telefone, idade, login, senhaHash, permissao));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void salvarCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
            for (Funcionario f : funcionarios) {
                writer.write(
                        f.getNome() + ";" +
                        f.getCpf() + ";" +
                        f.getTelefone() + ";" +
                        f.getIdade() + ";" +
                        f.getLogin() + ";" +
                        f.getSenhaHash() + ";" +
                        f.getPermissao().name()
                );
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

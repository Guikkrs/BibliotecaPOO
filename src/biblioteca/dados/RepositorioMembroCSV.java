package biblioteca.dados;

import java.io.*;
import java.util.*;
import biblioteca.Negocio.Membro;
import biblioteca.Enum.Permissao;

public class RepositorioMembroCSV implements Repositorio<Membro> {

    private List<Membro> membros = new ArrayList<>();
    private final String arquivo = "membros.csv";

    public RepositorioMembroCSV() {
        carregar();
    }

    @Override
    public void salvar(Membro membro) {
        if (!membros.contains(membro)) {
            membros.add(membro);
            salvarCSV();
        }
    }

    @Override
    public List<Membro> listar() {
        return new ArrayList<>(membros);
    }

    @Override
    public void atualizar(int index, Membro membro) {
        if (index >= 0 && index < membros.size()) {
            membros.set(index, membro);
            salvarCSV();
        }
    }

    @Override
    public void remover(int index) {
        if (index >= 0 && index < membros.size()) {
            membros.remove(index);
            salvarCSV();
        }
    }

    @Override
    public List<Membro> carregar() {
        membros.clear();
        carregarCSV();
        return new ArrayList<>(membros);
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

                    membros.add(new Membro(nome, cpf, telefone, idade, login, senhaHash, permissao));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void salvarCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
            for (Membro membro : membros) {
                writer.write(
                        membro.getNome() + ";" +
                        membro.getCpf() + ";" +
                        membro.getTelefone() + ";" +
                        membro.getIdade() + ";" +
                        membro.getLogin() + ";" +
                        membro.getSenhaHash() + ";" +
                        membro.getPermissao().name()
                );
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
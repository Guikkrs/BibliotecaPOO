package biblioteca.dados.persistencia;

import biblioteca.dados.repositorio.IRepositorioMembro;
import biblioteca.negocios.entidade.Funcionario;
import biblioteca.negocios.entidade.Membro;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static biblioteca.negocios.enums.Permissao.*;

public class RepositorioMembroPersistencia implements IRepositorioMembro {

    private ArrayList<Membro> membros;
    private int proximoId = 1;
    private static final String NOME_ARQUIVO = "membros.dat";

    public RepositorioMembroPersistencia() {
        this.membros = new ArrayList<>();
        carregarDoArquivo();
            if (this.membros.isEmpty()){
            CriarMembroBase();
        }
    }

    @Override
    public void adicionar(Membro membro) {
        if (buscarPorCpf(membro.getCpf()) == null) {
            membro.setId(this.proximoId++);
            this.membros.add(membro);
            salvarNoArquivo();
        }
    }

    @Override
    public void atualizar(Membro membro) {
        for (int i = 0; i < membros.size(); i++) {
            if (membros.get(i).getId() == membro.getId()) {
                membros.set(i, membro);
                salvarNoArquivo();
                return;
            }
        }
    }

    @Override
    public void remover(Membro membro) {
        if (this.membros.remove(membro)) {
            salvarNoArquivo();
        }
    }

    @Override
    public Membro buscarPorCpf(String cpf) {
        for (Membro m : this.membros) {
            if (m.getCpf().equals(cpf)) {
                return m;
            }
        }
        return null;
    }

    @Override
    public Membro buscarPorId(int id) {
        for (Membro m : this.membros) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    @Override
    public List<Membro> listarTodos() {
        return new ArrayList<>(this.membros);
    }

    private void salvarNoArquivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(NOME_ARQUIVO)))) {
            oos.writeObject(this.membros);
        } catch (IOException e) {
            System.err.println("Erro ao salvar membros: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarDoArquivo() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(NOME_ARQUIVO)))) {
            this.membros = (ArrayList<Membro>) ois.readObject();
            atualizarProximoId();
        } catch (IOException | ClassNotFoundException e) {
            this.membros = new ArrayList<>();
        }
    }

    private void atualizarProximoId() {
        if (!this.membros.isEmpty()) {
            this.proximoId = this.membros.stream().mapToInt(Membro::getId).max().orElse(0) + 1;
        } else {
            this.proximoId = 1;
        }
    }
    /**
     * Procura um membro na lista com base no login e senha.
     * @param login O login do membro.
     * @param senha A senha do membro.
     * @return O objeto Membro se as credenciais corresponderem, caso contrário, retorna null.
     */
    @Override
    public Membro autenticar(String login, String senha) {
        // Percorre cada membro na lista 'membros'
        for (Membro m : this.membros) {
            // Compara o login e a senha do membro atual com os fornecidos
            if (m.getLogin().equals(login) && m.getSenhaHash().equals(senha)) {
                return m; // Encontrou, retorna o objeto do membro
            }
        }
        return null; // Não encontrou ninguém com essas credenciais
    }
    public void CriarMembroBase() {
       Membro m1 = new Membro("Membro1", "11122233315","(87)996342168",24,"membro","123", MEMBRO);
            this.adicionar(m1);
            System.out.println("Membro Padrao carregado com sucesso");
    }
}
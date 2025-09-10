package biblioteca.dados.persistencia;

import biblioteca.dados.repositorio.IRepositorioPessoa;
import biblioteca.negocios.entidade.Pessoa; // CORREÇÃO AQUI

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RepositorioPessoaPersistencia implements IRepositorioPessoa {

    private ArrayList<Pessoa> pessoas;
    private int proximoId = 1;
    private static final String NOME_ARQUIVO = "pessoas.dat";

    public RepositorioPessoaPersistencia() {
        this.pessoas = new ArrayList<>();
        carregarDoArquivo();

    }

    public void adicionar(Pessoa pessoa) {
        if (pessoa.getId() == 0) {
            pessoa.setId(this.proximoId++);
        }
        this.pessoas.add(pessoa);
        salvarNoArquivo();
    }

    public void atualizar(Pessoa pessoa){
        for (int i = 0; i < pessoas.size(); i++) {
            if(pessoas.get(i).getId() == pessoa.getId()){
                pessoas.set(i, pessoa);
                salvarNoArquivo();
                return;
            }
        }
    }


    public void remover (Pessoa pessoa){
        this.pessoas.remove(pessoa);
        salvarNoArquivo();
    }

    public Pessoa buscarPorNome(String nome){
        for (Pessoa p:  this.pessoas){
            if (p.getNome().equalsIgnoreCase(nome)) {
                return p;
            }
        }
        return null;
    }

    public Pessoa buscarIndex(int id){
        for (Pessoa p:  this.pessoas){
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public List<Pessoa> listarTodos(){
        return new ArrayList<>(this.pessoas);
    }

    private void salvarNoArquivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(NOME_ARQUIVO)))) {
            oos.writeObject(this.pessoas);
        } catch (IOException e) {
            System.err.println("Erro ao salvar clientes: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarDoArquivo() {
        File arquivo = new File(NOME_ARQUIVO);
        if(!arquivo.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(NOME_ARQUIVO)))) {
            this.pessoas = (ArrayList<Pessoa>) ois.readObject();
            atualizarProximoId();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar clientes: " + e.getMessage());
            this.pessoas = new ArrayList<>();
        }
    }

    private void atualizarProximoId() {
        if (!this.pessoas.isEmpty()) {
            this.proximoId = this.pessoas.stream().mapToInt(Pessoa::getId).max().orElse(0) + 1;
        } else {
            this.proximoId = 1;
        }
    }
}

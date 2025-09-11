package biblioteca.dados.persistencia;

import biblioteca.dados.repositorio.IRepositorioItemDoAcervo;
import biblioteca.negocios.entidade.ItemDoAcervo;
import biblioteca.negocios.enums.EnumSetor;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioItemDoAcervoPersistencia implements IRepositorioItemDoAcervo {

    private ArrayList<ItemDoAcervo> acervo;
    private int proximoId = 1;
    private static final String NOME_ARQUIVO = "acervo.dat";

    public RepositorioItemDoAcervoPersistencia() {
        this.acervo = new ArrayList<>();
        carregarDoArquivo();
    }

    @Override
    public void adicionar(ItemDoAcervo item) {
        if (item.getId() == 0) {
            item.setId(this.proximoId++);
        }
        this.acervo.add(item);
        salvarNoArquivo();
    }

    @Override
    public void atualizar(ItemDoAcervo item) {
        for (int i = 0; i < acervo.size(); i++) {
            if (acervo.get(i).getId() == item.getId()) {
                acervo.set(i, item);
                salvarNoArquivo();
                return;
            }
        }
    }

    @Override
    public void remover(ItemDoAcervo item) {
        if (this.acervo.remove(item)) {
            salvarNoArquivo();
        }
    }

    @Override
    public ItemDoAcervo buscarPorTitulo(String titulo) {
        for(ItemDoAcervo item : this.acervo) {
            if (item.getTitulo().equalsIgnoreCase(titulo)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public ItemDoAcervo buscarPorId(int id) {
        for(ItemDoAcervo item : this.acervo) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    @Override
    public List<ItemDoAcervo> listarTodos() {
        return new ArrayList<>(this.acervo);
    }

    private void salvarNoArquivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(NOME_ARQUIVO)))) {
            oos.writeObject(this.acervo);
        } catch (IOException e) {
            System.err.println("Erro ao salvar acervo: " + e.getMessage());
        }
    }

    @Override
    public List<ItemDoAcervo> buscarPorSetor(EnumSetor setor) {
        return this.acervo.stream()
                .filter(item -> item.getSetor() == setor)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private void carregarDoArquivo() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) {
            this.acervo = new ArrayList<>();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(NOME_ARQUIVO)))) {
            this.acervo = (ArrayList<ItemDoAcervo>) ois.readObject();
            atualizarProximoId();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar acervo: " + e.getMessage());
            this.acervo = new ArrayList<>();
        }
    }

    private void atualizarProximoId() {
        if (this.acervo.isEmpty()) {
            this.proximoId = 1;
        } else {
            int maiorId = 0;
            for(ItemDoAcervo item : this.acervo){
                if(item.getId() > maiorId){
                    maiorId = item.getId();
                }
            }
            this.proximoId = maiorId + 1;
        }
    }
}
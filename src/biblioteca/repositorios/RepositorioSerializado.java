package biblioteca.repositorios;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RepositorioSerializado<T> implements Repositorio<T>, Serializable {

    private static final long serialVersionUID = 1L;
    private String nomeArquivo;
    private List<T> itens;

    public RepositorioSerializado(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
        this.itens = new ArrayList<>();
        carregar(); // tenta carregar os dados existentes
    }

    @Override
    public void adicionar(T obj) {
        itens.add(obj);
        try {
            salvar();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remover(T obj) {
        itens.remove(obj);
        try {
            salvar();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<T> listar() {
        return new ArrayList<>(itens);
    }

    @Override
    public void salvar() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomeArquivo))) {
            oos.writeObject(itens);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> carregar() {
        File arquivo = new File(nomeArquivo);
        if (!arquivo.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            itens = (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(itens);
    }

    public List<T> getItens() {
        return itens;
    }
}





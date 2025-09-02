package biblioteca.repositorios;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class RepositorioCSV<T> implements Repositorio<T> {

    private String arquivo;
    private Function<String, T> parser; // converte String -> Objeto
    private Function<T, String> serializer; // converte Objeto -> String

    public RepositorioCSV(String arquivo, Function<String, T> parser, Function<T, String> serializer) {
        this.arquivo = arquivo;
        this.parser = parser;
        this.serializer = serializer;
    }

    @Override
    public void salvar(T obj) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivo, true))) {
            bw.write(serializer.apply(obj));
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<T> listar() {
        List<T> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                lista.add(parser.apply(linha));
            }
        } catch (IOException e) {
            // se não existir arquivo ainda, retorna lista vazia
        }
        return lista;
    }

    @Override
    public void atualizar(int index, T obj) {
        List<T> lista = listar();
        if (index >= 0 && index < lista.size()) {
            lista.set(index, obj);
            sobrescreverArquivo(lista);
        }
    }

    @Override
    public void remover(int index) {
        List<T> lista = listar();
        if (index >= 0 && index < lista.size()) {
            lista.remove(index);
            sobrescreverArquivo(lista);
        }
    }

    private void sobrescreverArquivo(List<T> lista) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivo))) {
            for (T obj : lista) {
                bw.write(serializer.apply(obj));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    } 

    @Override
    public List<T> carregar() {
        return listar();
    }
}





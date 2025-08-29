package biblioteca.repositorios;

import java.io.*;
import java.util.*;
import biblioteca.Negocio.Setor;

public class RepositorioSetorCSV {

    private List<Setor> setores = new ArrayList<>();
    private final String arquivo = "setores.csv";

    public RepositorioSetorCSV() { carregar(); }

    public void adicionar(Setor setor) {
        if (!setores.contains(setor)) {
            setores.add(setor);
            salvar();
        }
    }

    public Optional<Setor> buscarPorNome(String nome) {
        return setores.stream()
                      .filter(s -> s.getNome().equalsIgnoreCase(nome))
                      .findFirst();
    }

    public List<Setor> listarTodos() { return new ArrayList<>(setores); }

    private void salvar() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(arquivo))) {
            for (Setor s : setores) pw.println(s.getNome());
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void carregar() {
        setores.clear();
        File f = new File(arquivo);
        if (!f.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (!linha.trim().isEmpty()) {
                    Setor s = new Setor(linha.trim());
                    if (!setores.contains(s)) setores.add(s);
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}


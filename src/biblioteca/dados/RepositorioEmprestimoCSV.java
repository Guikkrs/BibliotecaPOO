package biblioteca.dados;

import biblioteca.Negocio.Emprestimo;
import biblioteca.Negocio.ItemDoAcervo;
import biblioteca.Negocio.Membro;
import biblioteca.Negocio.Livro;
import biblioteca.Enum.StatusEmprestimo;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RepositorioEmprestimoCSV implements Repositorio<Emprestimo> {

    private List<Emprestimo> emprestimos = new ArrayList<>();
    private final String arquivo = "emprestimos.csv";

    // Repositórios necessários para buscar os objetos relacionados
    private Repositorio<Membro> repositorioMembro;
    private Repositorio<Livro> repositorioLivro;

    public RepositorioEmprestimoCSV(Repositorio<Membro> repositorioMembro, Repositorio<Livro> repositorioLivro) {
        this.repositorioMembro = repositorioMembro;
        this.repositorioLivro = repositorioLivro;
        carregar();
    }

    @Override
    public void salvar(Emprestimo emprestimo) {
        if (!emprestimos.contains(emprestimo)) {
            emprestimos.add(emprestimo);
        }
        salvarCSV();
    }

    @Override
    public List<Emprestimo> listar() {
        return new ArrayList<>(emprestimos);
    }

    @Override
    public void atualizar(int index, Emprestimo emprestimo) {
        if (index >= 0 && index < emprestimos.size()) {
            emprestimos.set(index, emprestimo);
        }
        salvarCSV();
    }

    @Override
    public void remover(int index) {
        if (index >= 0 && index < emprestimos.size()) {
            emprestimos.remove(index);
        }
        salvarCSV();
    }

    @Override
    public List<Emprestimo> carregar() {
        emprestimos.clear();
        carregarCSV();
        return new ArrayList<>(emprestimos);
    }

    private void salvarCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
            for (Emprestimo e : emprestimos) {
                writer.write(
                        e.getId() + ";"
                        + e.getMembro().getCpf() + ";"
                        + e.getItemDoAcervo().getTitulo() + ";"
                        + // Alterar para o ISBN do livro
                        e.getDataEmprestimo().toString() + ";"
                        + e.getDataDevolucaoPrevista().toString() + ";"
                        + (e.getDevolucaoRealizada() != null ? e.getDevolucaoRealizada().toString() : "null") + ";"
                        + e.getStatus().name()
                );
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void carregarCSV() {
        File file = new File(arquivo);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length >= 7) {
                    String id = dados[0];
                    String cpfMembro = dados[1];
                    String identificadorItem = dados[2];
                    LocalDate dataEmprestimo = LocalDate.parse(dados[3]);
                    String devolucaoRealizadaStr = dados[5];
                    StatusEmprestimo status = StatusEmprestimo.valueOf(dados[6]);

                    // Busca o Membro e o ItemDoAcervo nos repositórios já carregados
                    Membro membro = repositorioMembro.listar().stream()
                            .filter(m -> m.getCpf().equals(cpfMembro))
                            .findFirst()
                            .orElse(null);

                    ItemDoAcervo item = repositorioLivro.listar().stream()
                            .filter(l -> l.getTitulo().equalsIgnoreCase(identificadorItem)) // Alterar para buscar por ISBN
                            .findFirst()
                            .orElse(null);

                    if (membro != null && item != null) {
                        Emprestimo emprestimo = new Emprestimo(id, membro, item, dataEmprestimo);
                        if (!devolucaoRealizadaStr.equals("null")) {
                            emprestimo.setDevolucaoRealizada(LocalDate.parse(devolucaoRealizadaStr));
                        }
                        emprestimo.setStatus(status);
                        emprestimos.add(emprestimo);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

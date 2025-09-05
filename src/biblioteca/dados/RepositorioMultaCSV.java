package biblioteca.dados;

import biblioteca.negocios.Emprestimo;
import biblioteca.negocios.Membro;
import biblioteca.negocios.Multa;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RepositorioMultaCSV implements Repositorio<Multa> {

    private List<Multa> multas = new ArrayList<>();
    private final String arquivo = "multas.csv";

    // Adicione os repositórios necessários para buscar membros e empréstimos
    private Repositorio<Membro> repositorioMembro;
    private Repositorio<Emprestimo> repositorioEmprestimo;

    public RepositorioMultaCSV(Repositorio<Membro> repositorioMembro, Repositorio<Emprestimo> repositorioEmprestimo) {
        this.repositorioMembro = repositorioMembro;
        this.repositorioEmprestimo = repositorioEmprestimo;
        carregar();
    }

    @Override
    public void salvar(Multa multa) {
        if (!multas.contains(multa)) {
            multas.add(multa);
        }
        salvarCSV();
    }

    @Override
    public List<Multa> listar() {
        return new ArrayList<>(multas);
    }

    @Override
    public void atualizar(int index, Multa multa) {
        if (index >= 0 && index < multas.size()) {
            multas.set(index, multa);
            salvarCSV();
        }
    }

    @Override
    public void remover(int index) {
        if (index >= 0 && index < multas.size()) {
            multas.remove(index);
            salvarCSV();
        }
    }

    @Override
    public List<Multa> carregar() {
        multas.clear();
        carregarCSV();
        return new ArrayList<>(multas);
    }
    
    private void salvarCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
            for (Multa m : multas) {
                // Serializa o objeto Multa para uma linha CSV
                writer.write(
                    m.getMembro().getCpf() + ";" + 
                    m.getEmprestimo().getId() + ";" + // Assumindo que Emprestimo tem um ID único
                    m.getValor() + ";" + 
                    m.getStatus().name() + ";" + 
                    m.getDataCriacao().toString() + ";" + 
                    (m.getDataPagamento() != null ? m.getDataPagamento().toString() : "null")
                );
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void carregarCSV() {
        File file = new File(arquivo);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length >= 6) {
                    // Deserializa uma linha CSV para um objeto Multa
                    String cpfMembro = dados[0];
                    // String idEmprestimo = dados[1];
                    // Precisamos de um repositório para Emprestimo também
                    
                    // Lógica para recriar o objeto Multa...
                    
                    // Por enquanto, vamos assumir que as multas não são persistidas individualmente
                    // E que a lista 'multas' na classe Biblioteca é populada de outra forma
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
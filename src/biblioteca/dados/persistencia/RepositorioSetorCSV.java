package biblioteca.dados.persistencia;

import biblioteca.dados.repositorio.IRepositorioSetor;
import biblioteca.negocios.entidade.Setor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RepositorioSetorCSV implements IRepositorioSetor {

    private List<Setor> setores;
    private int proximoId = 1;
    private static final String NOME_ARQUIVO = "setores.csv";
    private static final String[] CABECALHO = {"id", "nome"};

    public RepositorioSetorCSV() {
        this.setores = new ArrayList<>();
        carregarDoArquivo();
    }

    @Override
    public void adicionar(Setor setor) {
        boolean existe = this.setores.stream()
                .anyMatch(s -> s.getNome().equalsIgnoreCase(setor.getNome()));

        if (!existe) {
            setor.setId(this.proximoId++);
            this.setores.add(setor);
            salvarNoArquivo();
        }
    }

    @Override
    public void atualizar(Setor setor) {
        for (int i = 0; i < setores.size(); i++) {
            if (setores.get(i).getId() == setor.getId()) {
                setores.set(i, setor);
                salvarNoArquivo();
                return;
            }
        }
    }

    @Override
    public void remover(Setor setor) {
        if (this.setores.remove(setor)) {
            salvarNoArquivo();
        }
    }

    @Override
    public Setor buscarPorNome(String nome) {
        return this.setores.stream()
                .filter(s -> s.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Setor buscarPorId(int id) {
        return this.setores.stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Setor> listarTodos() {
        return new ArrayList<>(this.setores);
    }

    private void salvarNoArquivo() {
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(NOME_ARQUIVO));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(CABECALHO))
        ) {
            for (Setor setor : this.setores) {
                csvPrinter.printRecord(setor.getId(), setor.getNome());
            }
            csvPrinter.flush();
        } catch (IOException e) {
            System.err.println("Erro ao salvar setores no CSV: " + e.getMessage());
        }
    }

    private void carregarDoArquivo() {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(NOME_ARQUIVO));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(CABECALHO)
                        .withFirstRecordAsHeader().withTrim());
        ) {
            this.setores.clear();
            for (CSVRecord csvRecord : csvParser) {
                Setor setor = new Setor(csvRecord.get("nome"));
                setor.setId(Integer.parseInt(csvRecord.get("id")));
                this.setores.add(setor);
            }
            atualizarProximoId();
        } catch (IOException e) {
            System.out.println("INFO: Arquivo " + NOME_ARQUIVO + " não encontrado, iniciando com base vazia.");
        } catch (Exception e) {
            System.err.println("ERRO CRÍTICO ao ler " + NOME_ARQUIVO + ": " + e.getMessage());
        }
    }

    private void atualizarProximoId() {
        if (!this.setores.isEmpty()) {
            this.proximoId = this.setores.stream()
                    .mapToInt(Setor::getId)
                    .max()
                    .orElse(0) + 1;
        } else {
            this.proximoId = 1;
        }
    }
}
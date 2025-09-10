package biblioteca.dados.persistencia;

import biblioteca.dados.repositorio.IRepositorioAutor;
import biblioteca.negocios.entidade.Autor;
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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

public class RepositorioAutorCSV implements IRepositorioAutor {

    private List<Autor> autores;
    private int proximoId = 1;
    private static final String NOME_ARQUIVO = "autores.csv";
    private static final String[] CABECALHO = {"id", "nome", "nacionalidade"};

    public RepositorioAutorCSV() {
        this.autores = new ArrayList<>();
        carregarDoArquivo();
    }

    @Override
    public void adicionar(Autor autor) {
        // Para evitar duplicados, poderíamos verificar pelo nome e nacionalidade
        boolean existe = this.autores.stream()
                .anyMatch(a -> a.getNome().equalsIgnoreCase(autor.getNome()) && a.getNacionalidade().equalsIgnoreCase(autor.getNacionalidade()));

        if (!existe) {
            autor.setId(this.proximoId++);
            this.autores.add(autor);
            salvarNoArquivo();
        }
    }

    @Override
    public void atualizar(Autor autor) {
        for (int i = 0; i < autores.size(); i++) {
            if (autores.get(i).getId() == autor.getId()) {
                autores.set(i, autor);
                salvarNoArquivo();
                return;
            }
        }
    }

    @Override
    public void remover(Autor autor) {
        if (this.autores.remove(autor)) {
            salvarNoArquivo();
        }
    }

    @Override
    public Autor buscarPorNome(String nome) {
        return this.autores.stream()
                .filter(a -> a.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Autor buscarPorId(int id) {
        return this.autores.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Autor> listarTodos() {
        return new ArrayList<>(this.autores);
    }

    private void salvarNoArquivo() {
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(NOME_ARQUIVO));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(CABECALHO))
        ) {
            for (Autor autor : this.autores) {
                csvPrinter.printRecord(autor.getId(), autor.getNome(), autor.getNacionalidade());
            }
            csvPrinter.flush();
        } catch (IOException e) {
            System.err.println("Erro ao salvar autores no CSV: " + e.getMessage());
        }
    }

    private void carregarDoArquivo() {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(NOME_ARQUIVO));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(CABECALHO)
                        .withFirstRecordAsHeader().withTrim());
        ) {
            this.autores.clear();
            for (CSVRecord csvRecord : csvParser) {
                Autor autor = new Autor(
                        csvRecord.get("nome"),
                        csvRecord.get("nacionalidade")
                );
                autor.setId(Integer.parseInt(csvRecord.get("id")));
                this.autores.add(autor);
            }
            atualizarProximoId();
        } catch (IOException e) {
            System.out.println("INFO: Arquivo " + NOME_ARQUIVO + " não encontrado, iniciando com base vazia.");
        } catch (Exception e) {
            System.err.println("ERRO CRÍTICO ao ler " + NOME_ARQUIVO + ": " + e.getMessage());
        }
    }

    private void atualizarProximoId() {
        if (!this.autores.isEmpty()) {
            this.proximoId = this.autores.stream()
                    .mapToInt(Autor::getId)
                    .max()
                    .orElse(0) + 1;
        } else {
            this.proximoId = 1;
        }
    }
}
package biblioteca.dados.persistencia;

import biblioteca.dados.repositorio.IRepositorioCaixa;
import biblioteca.negocios.entidade.Caixa;
import biblioteca.negocios.enums.StatusCaixa;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RepositorioCaixaCSV implements IRepositorioCaixa {

    private List<Caixa> caixas;
    private int proximoId = 1;
    private static final String NOME_ARQUIVO = "caixas.csv";
    private static final String[] CABECALHO = {"id", "dataAbertura", "saldoInicial", "dataFechamento", "saldoFinal", "saldoAtual", "status"};

    public RepositorioCaixaCSV() {
        this.caixas = new ArrayList<>();
        carregarDoArquivo();
    }

    @Override
    public void adicionar(Caixa caixa) {
        caixa.setId(this.proximoId++);
        this.caixas.add(caixa);
        salvarNoArquivo();
    }

    @Override
    public void atualizar(Caixa caixa) {
        for (int i = 0; i < caixas.size(); i++) {
            if (caixas.get(i).getId() == caixa.getId()) {
                caixas.set(i, caixa);
                salvarNoArquivo();
                return;
            }
        }
    }

    @Override
    public Caixa buscarPorId(int id) {
        return this.caixas.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Caixa buscarCaixaAberto() {
        return this.caixas.stream()
                .filter(c -> c.getStatus() == StatusCaixa.ABERTO)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Caixa> listarTodos() {
        return new ArrayList<>(this.caixas);
    }

    private void salvarNoArquivo() {
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(NOME_ARQUIVO));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(CABECALHO))
        ) {
            for (Caixa caixa : this.caixas) {
                csvPrinter.printRecord(
                        caixa.getId(),
                        caixa.getDataAbertura(),
                        caixa.getSaldoInicial().toPlainString(),
                        caixa.getDataFechamento(),
                        caixa.getSaldoFinal() != null ? caixa.getSaldoFinal().toPlainString() : "",
                        caixa.getSaldoAtual().toPlainString(),
                        caixa.getStatus().name()
                );
            }
            csvPrinter.flush();
        } catch (IOException e) {
            System.err.println("Erro ao salvar caixas no CSV: " + e.getMessage());
        }
    }

    // --- MÉTODO CORRIGIDO ---
    private void carregarDoArquivo() {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(NOME_ARQUIVO));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(CABECALHO)
                        .withFirstRecordAsHeader().withTrim());
        ) {
            this.caixas.clear();
            for (CSVRecord csvRecord : csvParser) {
                // Lê todos os campos do CSV
                int id = Integer.parseInt(csvRecord.get("id"));
                LocalDate dataAbertura = LocalDate.parse(csvRecord.get("dataAbertura"));
                BigDecimal saldoInicial = new BigDecimal(csvRecord.get("saldoInicial"));
                BigDecimal saldoAtual = new BigDecimal(csvRecord.get("saldoAtual"));
                StatusCaixa status = StatusCaixa.valueOf(csvRecord.get("status"));

                LocalDate dataFechamento = null;
                String dataFechamentoStr = csvRecord.get("dataFechamento");
                if (dataFechamentoStr != null && !dataFechamentoStr.isEmpty()) {
                    dataFechamento = LocalDate.parse(dataFechamentoStr);
                }

                BigDecimal saldoFinal = null;
                String saldoFinalStr = csvRecord.get("saldoFinal");
                if (saldoFinalStr != null && !saldoFinalStr.isEmpty()) {
                    saldoFinal = new BigDecimal(saldoFinalStr);
                }

                // Usa o novo construtor da classe Caixa
                Caixa caixa = new Caixa(id, dataAbertura, saldoInicial, dataFechamento, saldoFinal, saldoAtual, status);
                this.caixas.add(caixa);
            }
            atualizarProximoId();
        } catch (IOException e) {
            System.out.println("INFO: Arquivo " + NOME_ARQUIVO + " não encontrado, iniciando com base vazia.");
        } catch (Exception e) {
            System.err.println("ERRO CRÍTICO ao ler " + NOME_ARQUIVO + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void atualizarProximoId() {
        if (!this.caixas.isEmpty()) {
            this.proximoId = this.caixas.stream().mapToInt(Caixa::getId).max().orElse(0) + 1;
        } else {
            this.proximoId = 1;
        }
    }
}
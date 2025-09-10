package biblioteca.dados.persistencia;

import biblioteca.dados.repositorio.IRepositorioEmprestimo;
import biblioteca.dados.repositorio.IRepositorioMembro;
import biblioteca.dados.repositorio.IRepositorioMulta;
import biblioteca.negocios.entidade.Emprestimo;
import biblioteca.negocios.entidade.Membro;
import biblioteca.negocios.entidade.Multa;
import biblioteca.negocios.enums.StatusMulta;
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

public class RepositorioMultaCSV implements IRepositorioMulta {

    private List<Multa> multas;
    private int proximoId = 1;

    // Dependências para "ligar" os objetos
    private final IRepositorioMembro repositorioMembro;
    private final IRepositorioEmprestimo repositorioEmprestimo;

    private static final String NOME_ARQUIVO = "multas.csv";
    private static final String[] CABECALHO = {"id", "id_membro", "id_emprestimo", "valor", "status", "dataCriacao", "dataPagamento"};

    public RepositorioMultaCSV(IRepositorioMembro repositorioMembro, IRepositorioEmprestimo repositorioEmprestimo) {
        this.repositorioMembro = repositorioMembro;
        this.repositorioEmprestimo = repositorioEmprestimo;
        this.multas = new ArrayList<>();
        carregarDoArquivo();
    }

    @Override
    public void adicionar(Multa multa) {
        multa.setId(this.proximoId++);
        this.multas.add(multa);
        salvarNoArquivo();
    }

    @Override
    public void atualizar(Multa multa) {
        for (int i = 0; i < multas.size(); i++) {
            if (multas.get(i).getId() == multa.getId()) {
                multas.set(i, multa);
                salvarNoArquivo();
                return;
            }
        }
    }

    @Override
    public void remover(Multa multa) {
        if (this.multas.remove(multa)) {
            salvarNoArquivo();
        }
    }

    @Override
    public Multa buscarPorId(int id) {
        return this.multas.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Multa> listarTodos() {
        return new ArrayList<>(this.multas);
    }

    private void salvarNoArquivo() {
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(NOME_ARQUIVO));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(CABECALHO))
        ) {
            for (Multa m : this.multas) {
                csvPrinter.printRecord(
                        m.getId(),
                        m.getMembro().getId(),
                        m.getEmprestimo().getId(),
                        m.getValor().toPlainString(),
                        m.getStatus().name(),
                        m.getDataCriacao(),
                        m.getDataPagamento() != null ? m.getDataPagamento() : ""
                );
            }
            csvPrinter.flush();
        } catch (IOException e) {
            System.err.println("Erro ao salvar multas no CSV: " + e.getMessage());
        }
    }

    private void carregarDoArquivo() {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(NOME_ARQUIVO));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(CABECALHO)
                        .withFirstRecordAsHeader().withTrim());
        ) {
            this.multas.clear();
            for (CSVRecord csvRecord : csvParser) {
                int idMembro = Integer.parseInt(csvRecord.get("id_membro"));
                int idEmprestimo = Integer.parseInt(csvRecord.get("id_emprestimo"));

                Membro membro = this.repositorioMembro.buscarPorId(idMembro);
                Emprestimo emprestimo = this.repositorioEmprestimo.buscarPorId(idEmprestimo);

                if (membro != null && emprestimo != null) {
                    Multa multa = new Multa(
                            membro,
                            emprestimo,
                            new BigDecimal(csvRecord.get("valor"))
                    );
                    multa.setId(Integer.parseInt(csvRecord.get("id")));
                    multa.setStatus(StatusMulta.valueOf(csvRecord.get("status")));

                    String dataPagamentoStr = csvRecord.get("dataPagamento");
                    if(dataPagamentoStr != null && !dataPagamentoStr.isEmpty()){
                        // A classe Multa não tem um setDataPagamento, então precisamos de um truque
                        multa.registrarPagamento(); // Simula o pagamento para setar a data
                    }

                    this.multas.add(multa);
                } else {
                    System.err.println("AVISO: Não foi possível carregar a multa com ID " + csvRecord.get("id") + " pois o membro ou empréstimo associado não foi encontrado.");
                }
            }
            atualizarProximoId();
        } catch (IOException e) {
            System.out.println("INFO: Arquivo " + NOME_ARQUIVO + " não encontrado, iniciando com base vazia.");
        } catch (Exception e) {
            System.err.println("ERRO CRÍTICO ao ler " + NOME_ARQUIVO + ": " + e.getMessage());
        }
    }

    private void atualizarProximoId() {
        if (!this.multas.isEmpty()) {
            this.proximoId = this.multas.stream().mapToInt(Multa::getId).max().orElse(0) + 1;
        } else {
            this.proximoId = 1;
        }
    }
}
package biblioteca.dados.persistencia;

import biblioteca.dados.repositorio.IRepositorioEmprestimo;
import biblioteca.dados.repositorio.IRepositorioItemDoAcervo;
import biblioteca.dados.repositorio.IRepositorioMembro;
import biblioteca.negocios.entidade.Emprestimo;
import biblioteca.negocios.entidade.ItemDoAcervo;
import biblioteca.negocios.entidade.Membro;
import biblioteca.negocios.enums.StatusEmprestimo;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RepositorioEmprestimoCSV implements IRepositorioEmprestimo {

    private List<Emprestimo> emprestimos;
    private int proximoId = 1;

    // Dependências de outros repositórios para "ligar" os objetos ao carregar
    private final IRepositorioMembro repositorioMembro;
    private final IRepositorioItemDoAcervo repositorioAcervo;

    private static final String NOME_ARQUIVO = "emprestimos.csv";
    private static final String[] CABECALHO = {"id", "id_membro", "id_item", "dataEmprestimo", "dataDevolucaoPrevista", "devolucaoRealizada", "status"};

    // O construtor agora recebe os outros repositórios como dependência
    public RepositorioEmprestimoCSV(IRepositorioMembro repositorioMembro, IRepositorioItemDoAcervo repositorioAcervo) {
        this.repositorioMembro = repositorioMembro;
        this.repositorioAcervo = repositorioAcervo;
        this.emprestimos = new ArrayList<>();
        carregarDoArquivo();
    }

    @Override
    public void adicionar(Emprestimo emprestimo) {
        emprestimo.setId(this.proximoId++);
        this.emprestimos.add(emprestimo);
        salvarNoArquivo();
    }

    @Override
    public void atualizar(Emprestimo emprestimo) {
        for (int i = 0; i < emprestimos.size(); i++) {
            if (emprestimos.get(i).getId() == emprestimo.getId()) {
                emprestimos.set(i, emprestimo);
                salvarNoArquivo();
                return;
            }
        }
    }

    @Override
    public void remover(Emprestimo emprestimo) {
        if (this.emprestimos.remove(emprestimo)) {
            salvarNoArquivo();
        }
    }

    @Override
    public Emprestimo buscarPorId(int id) {
        return this.emprestimos.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Emprestimo> listarTodos() {
        return new ArrayList<>(this.emprestimos);
    }

    private void salvarNoArquivo() {
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(NOME_ARQUIVO));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(CABECALHO))
        ) {
            for (Emprestimo e : this.emprestimos) {
                csvPrinter.printRecord(
                        e.getId(),
                        e.getMembro().getId(),          // Salva o ID do membro
                        e.getItemDoAcervo().getId(),    // Salva o ID do item
                        e.getDataEmprestimo(),
                        e.getDataDevolucaoPrevista(),
                        e.getDevolucaoRealizada() != null ? e.getDevolucaoRealizada() : "", // Trata nulo
                        e.getStatus().name()
                );
            }
            csvPrinter.flush();
        } catch (IOException e) {
            System.err.println("Erro ao salvar empréstimos no CSV: " + e.getMessage());
        }
    }

    private void carregarDoArquivo() {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(NOME_ARQUIVO));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(CABECALHO)
                        .withFirstRecordAsHeader().withTrim());
        ) {
            this.emprestimos.clear();
            for (CSVRecord csvRecord : csvParser) {
                // Lê os IDs do CSV
                int idMembro = Integer.parseInt(csvRecord.get("id_membro"));
                int idItem = Integer.parseInt(csvRecord.get("id_item"));

                // Usa os repositórios injetados para encontrar os objetos completos
                Membro membro = this.repositorioMembro.buscarPorId(idMembro);
                ItemDoAcervo item = this.repositorioAcervo.buscarPorId(idItem);

                // Só cria o empréstimo se o membro e o item ainda existirem
                if (membro != null && item != null) {
                    LocalDate dataEmprestimo = LocalDate.parse(csvRecord.get("dataEmprestimo"));

                    Emprestimo emprestimo = new Emprestimo(membro, item, dataEmprestimo);
                    emprestimo.setId(Integer.parseInt(csvRecord.get("id")));
                    emprestimo.setStatus(StatusEmprestimo.valueOf(csvRecord.get("status")));

                    String devolucaoStr = csvRecord.get("devolucaoRealizada");
                    if (devolucaoStr != null && !devolucaoStr.isEmpty()) {
                        emprestimo.setDevolucaoRealizada(LocalDate.parse(devolucaoStr));
                    }

                    this.emprestimos.add(emprestimo);
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
        if (!this.emprestimos.isEmpty()) {
            this.proximoId = this.emprestimos.stream().mapToInt(Emprestimo::getId).max().orElse(0) + 1;
        } else {
            this.proximoId = 1;
        }
    }
}
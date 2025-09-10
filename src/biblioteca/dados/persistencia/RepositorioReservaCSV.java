package biblioteca.dados.persistencia;

import biblioteca.dados.repositorio.IRepositorioItemDoAcervo;
import biblioteca.dados.repositorio.IRepositorioMembro;
import biblioteca.dados.repositorio.IRepositorioReserva;
import biblioteca.negocios.entidade.ItemDoAcervo;
import biblioteca.negocios.entidade.Membro;
import biblioteca.negocios.entidade.Reserva;
import biblioteca.negocios.enums.StatusReserva;
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

public class RepositorioReservaCSV implements IRepositorioReserva {

    private List<Reserva> reservas;
    private int proximoId = 1;

    // Dependências para "ligar" os objetos ao carregar
    private final IRepositorioMembro repositorioMembro;
    private final IRepositorioItemDoAcervo repositorioAcervo;

    private static final String NOME_ARQUIVO = "reservas.csv";
    private static final String[] CABECALHO = {"id", "id_membro", "id_item", "dataReserva", "status"};

    public RepositorioReservaCSV(IRepositorioMembro repositorioMembro, IRepositorioItemDoAcervo repositorioAcervo) {
        this.repositorioMembro = repositorioMembro;
        this.repositorioAcervo = repositorioAcervo;
        this.reservas = new ArrayList<>();
        carregarDoArquivo();
    }

    @Override
    public void adicionar(Reserva reserva) {
        reserva.setId(this.proximoId++);
        this.reservas.add(reserva);
        salvarNoArquivo();
    }

    @Override
    public void atualizar(Reserva reserva) {
        for (int i = 0; i < reservas.size(); i++) {
            if (reservas.get(i).getId() == reserva.getId()) {
                reservas.set(i, reserva);
                salvarNoArquivo();
                return;
            }
        }
    }

    @Override
    public void remover(Reserva reserva) {
        if (this.reservas.remove(reserva)) {
            salvarNoArquivo();
        }
    }

    @Override
    public Reserva buscarPorId(int id) {
        return this.reservas.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Reserva> listarTodos() {
        return new ArrayList<>(this.reservas);
    }

    private void salvarNoArquivo() {
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(NOME_ARQUIVO));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(CABECALHO))
        ) {
            for (Reserva r : this.reservas) {
                csvPrinter.printRecord(
                        r.getId(),
                        r.getMembro().getId(),
                        r.getItemDoAcervo().getId(),
                        r.getDataReserva(),
                        r.getStatus().name()
                );
            }
            csvPrinter.flush();
        } catch (IOException e) {
            System.err.println("Erro ao salvar reservas no CSV: " + e.getMessage());
        }
    }

    private void carregarDoArquivo() {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(NOME_ARQUIVO));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(CABECALHO)
                        .withFirstRecordAsHeader().withTrim());
        ) {
            this.reservas.clear();
            for (CSVRecord csvRecord : csvParser) {
                int idMembro = Integer.parseInt(csvRecord.get("id_membro"));
                int idItem = Integer.parseInt(csvRecord.get("id_item"));

                Membro membro = this.repositorioMembro.buscarPorId(idMembro);
                ItemDoAcervo item = this.repositorioAcervo.buscarPorId(idItem);

                if (membro != null && item != null) {
                    Reserva reserva = new Reserva(
                            membro,
                            item,
                            LocalDate.parse(csvRecord.get("dataReserva"))
                    );
                    reserva.setId(Integer.parseInt(csvRecord.get("id")));
                    reserva.setStatus(StatusReserva.valueOf(csvRecord.get("status")));

                    this.reservas.add(reserva);
                } else {
                    System.err.println("AVISO: Não foi possível carregar a reserva com ID " + csvRecord.get("id") + " pois o membro ou item associado não foi encontrado.");
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
        if (!this.reservas.isEmpty()) {
            this.proximoId = this.reservas.stream().mapToInt(Reserva::getId).max().orElse(0) + 1;
        } else {
            this.proximoId = 1;
        }
    }
}
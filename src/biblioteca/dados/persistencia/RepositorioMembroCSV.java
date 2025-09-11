package biblioteca.dados.persistencia;

import biblioteca.dados.repositorio.IRepositorioMembro;
import biblioteca.negocios.entidade.Membro;
import biblioteca.negocios.entidade.Pessoa;
import biblioteca.negocios.enums.Permissao;
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

import static biblioteca.negocios.enums.Permissao.MEMBRO;

public class RepositorioMembroCSV implements IRepositorioMembro {

    private List<Membro> membros;
    private int proximoId = 1;
    private static final String NOME_ARQUIVO = "membros.csv";
    private static final String[] CABECALHO = {"id", "nome", "cpf", "telefone", "idade", "login", "senhaHash", "permissao"};

    public RepositorioMembroCSV() {
        this.membros = new ArrayList<>();
        carregarDoArquivo();
        if(this.membros.isEmpty()){
            CriarMembroBase();
        }
    }

    @Override
    public void adicionar(Membro membro) {
        if (buscarPorCpf(membro.getCpf()) == null) {
            membro.setId(this.proximoId++);
            this.membros.add(membro);
            salvarNoArquivo();
        }
    }

    @Override
    public void atualizar(Membro membro) {
        for (int i = 0; i < membros.size(); i++) {
            if (membros.get(i).getId() == membro.getId()) {
                membros.set(i, membro);
                salvarNoArquivo();
                return;
            }
        }
    }

    @Override
    public void remover(Membro membro) {
        if (this.membros.remove(membro)) {
            salvarNoArquivo();
        }
    }

    @Override
    public Membro buscarPorCpf(String cpf) {
        return this.membros.stream()
                .filter(m -> m.getCpf().equals(cpf))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Membro buscarPorId(int id) {
        return this.membros.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Membro autenticar(String login, String senha) {
        return this.membros.stream()
                .filter(m -> m.getLogin().equals(login) && m.getSenhaHash().equals(senha))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Membro> listarTodos() {
        return new ArrayList<>(this.membros);
    }

    private void salvarNoArquivo() {
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(NOME_ARQUIVO));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(CABECALHO))
        ) {
            for (Membro m : this.membros) {
                csvPrinter.printRecord(
                        m.getId(), m.getNome(), m.getCpf(), m.getTelefone(), m.getIdade(),
                        m.getLogin(), m.getSenhaHash(), m.getPermissao().name()
                );
            }
            csvPrinter.flush();
        } catch (IOException e) {
            System.err.println("Erro ao salvar membros no CSV: " + e.getMessage());
        }
    }

    private void carregarDoArquivo() {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(NOME_ARQUIVO));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(CABECALHO)
                        .withFirstRecordAsHeader().withTrim());
        ) {
            this.membros.clear();
            for (CSVRecord csvRecord : csvParser) {
                Membro membro = new Membro(
                        csvRecord.get("nome"),
                        csvRecord.get("cpf"),
                        csvRecord.get("telefone"),
                        Integer.parseInt(csvRecord.get("idade")),
                        csvRecord.get("login"),
                        csvRecord.get("senhaHash"),
                        Permissao.valueOf(csvRecord.get("permissao"))
                );
                membro.setId(Integer.parseInt(csvRecord.get("id")));
                this.membros.add(membro);
            }
            atualizarProximoId();
        } catch (IOException e) {
            System.out.println("INFO: Arquivo " + NOME_ARQUIVO + " não encontrado, iniciando com base vazia.");
        } catch (Exception e) {
            System.err.println("ERRO CRÍTICO ao ler " + NOME_ARQUIVO + ": " + e.getMessage());
        }
    }

    private void atualizarProximoId() {
        if (!this.membros.isEmpty()) {
            this.proximoId = this.membros.stream()
                    .mapToInt(Pessoa::getId)
                    .max()
                    .orElse(0) + 1;
        } else {
            this.proximoId = 1;
        }
    }
    public void CriarMembroBase() {
        Membro m1 = new Membro("Membro1", "11122233315","(87)996342168",24,"membro","123", MEMBRO);
        this.adicionar(m1);
        System.out.println("Membro Padrao(CSV) carregado com sucesso");
    }
}
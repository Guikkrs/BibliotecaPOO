package biblioteca.dados.persistencia;

import biblioteca.dados.repositorio.IRepositorioFuncionario;
import biblioteca.negocios.entidade.Funcionario;
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

public class RepositorioFuncionarioCSV implements IRepositorioFuncionario {

    private List<Funcionario> funcionarios;
    private int proximoId = 1;
    private static final String NOME_ARQUIVO = "funcionarios.csv";
    private static final String[] CABECALHO = {"id", "nome", "cpf", "telefone", "idade", "login", "senhaHash", "permissao"};

    public RepositorioFuncionarioCSV() {
        this.funcionarios = new ArrayList<>();
        carregarDoArquivo();
    }

    @Override
    public void adicionar(Funcionario funcionario) {
        if (buscarPorCpf(funcionario.getCpf()) == null) {
            funcionario.setId(this.proximoId++);
            this.funcionarios.add(funcionario);
            salvarNoArquivo();
        }
    }

    @Override
    public void atualizar(Funcionario funcionario) {
        for (int i = 0; i < funcionarios.size(); i++) {
            if (funcionarios.get(i).getId() == funcionario.getId()) {
                funcionarios.set(i, funcionario);
                salvarNoArquivo();
                return;
            }
        }
    }

    @Override
    public void remover(Funcionario funcionario) {
        if (this.funcionarios.remove(funcionario)) {
            salvarNoArquivo();
        }
    }

    @Override
    public Funcionario buscarPorCpf(String cpf) {
        return this.funcionarios.stream()
                .filter(f -> f.getCpf().equals(cpf))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Funcionario autenticar(String login, String senha) {
        return this.funcionarios.stream()
                .filter(f -> f.getLogin().equals(login) && f.getSenhaHash().equals(senha))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Funcionario buscarPorId(int id) {
        return this.funcionarios.stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Funcionario> listarTodos() {
        return new ArrayList<>(this.funcionarios);
    }

    private void salvarNoArquivo() {
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(NOME_ARQUIVO));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(CABECALHO))
        ) {
            for (Funcionario f : this.funcionarios) {
                csvPrinter.printRecord(
                        f.getId(), f.getNome(), f.getCpf(), f.getTelefone(), f.getIdade(),
                        f.getLogin(), f.getSenhaHash(), f.getPermissao().name()
                );
            }
            csvPrinter.flush();
        } catch (IOException e) {
            System.err.println("Erro ao salvar funcionários no CSV: " + e.getMessage());
        }
    }

    private void carregarDoArquivo() {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(NOME_ARQUIVO));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(CABECALHO)
                        .withFirstRecordAsHeader().withTrim());
        ) {
            this.funcionarios.clear();
            for (CSVRecord csvRecord : csvParser) {
                Funcionario func = new Funcionario(
                        csvRecord.get("nome"),
                        csvRecord.get("cpf"),
                        csvRecord.get("telefone"),
                        Integer.parseInt(csvRecord.get("idade")),
                        csvRecord.get("login"),
                        csvRecord.get("senhaHash"),
                        Permissao.valueOf(csvRecord.get("permissao"))
                );
                func.setId(Integer.parseInt(csvRecord.get("id")));
                this.funcionarios.add(func);
            }
            atualizarProximoId();
        } catch (IOException e) {
            System.out.println("INFO: Arquivo " + NOME_ARQUIVO + " não encontrado, iniciando com base vazia.");
        } catch (Exception e) {
            System.err.println("ERRO CRÍTICO ao ler " + NOME_ARQUIVO + ": " + e.getMessage());
        }
    }

    private void atualizarProximoId() {
        if (!this.funcionarios.isEmpty()) {
            this.proximoId = this.funcionarios.stream()
                    .mapToInt(Pessoa::getId)
                    .max()
                    .orElse(0) + 1;
        } else {
            this.proximoId = 1;
        }
    }
}
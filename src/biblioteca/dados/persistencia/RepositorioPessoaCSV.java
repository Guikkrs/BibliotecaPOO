package biblioteca.dados.persistencia;

import biblioteca.dados.repositorio.IRepositorioPessoa;
import biblioteca.negocios.entidade.Funcionario;
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

public class RepositorioPessoaCSV implements IRepositorioPessoa {

    private List<Pessoa> pessoas;
    private int proximoId = 1;
    private static final String NOME_ARQUIVO = "pessoas.csv";
    // Adicionamos a coluna "tipo" e as colunas específicas de Membro/Funcionario
    private static final String[] CABECALHO = {"id", "tipo", "nome", "cpf", "telefone", "idade", "login", "senhaHash", "permissao"};

    public RepositorioPessoaCSV() {
        this.pessoas = new ArrayList<>();
        carregarDoArquivo();
    }

    @Override
    public void adicionar(Pessoa pessoa) {
        pessoa.setId(this.proximoId++);
        this.pessoas.add(pessoa);
        salvarNoArquivo();
    }

    @Override
    public void atualizar(Pessoa pessoa) {
        for (int i = 0; i < pessoas.size(); i++) {
            if (pessoas.get(i).getId() == pessoa.getId()) {
                pessoas.set(i, pessoa);
                salvarNoArquivo();
                return;
            }
        }
    }

    @Override
    public void remover(Pessoa pessoa) {
        if (this.pessoas.remove(pessoa)) {
            salvarNoArquivo();
        }
    }

    @Override
    public Pessoa buscarPorNome(String nome) {
        return this.pessoas.stream()
                .filter(p -> p.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElse(null);
    }

    // O nome do método na interface é buscarIndex, mas a implementação busca por ID
    @Override
    public Pessoa buscarIndex(int id) {
        return this.pessoas.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Pessoa> listarTodos() {
        return new ArrayList<>(this.pessoas);
    }

    private void salvarNoArquivo() {
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(NOME_ARQUIVO));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(CABECALHO))
        ) {
            for (Pessoa p : this.pessoas) {
                if (p instanceof Funcionario) {
                    Funcionario f = (Funcionario) p;
                    csvPrinter.printRecord(f.getId(), "Funcionario", f.getNome(), f.getCpf(), f.getTelefone(),
                            f.getIdade(), f.getLogin(), f.getSenhaHash(), f.getPermissao().name());
                } else if (p instanceof Membro) {
                    Membro m = (Membro) p;
                    csvPrinter.printRecord(m.getId(), "Membro", m.getNome(), m.getCpf(), m.getTelefone(),
                            m.getIdade(), m.getLogin(), m.getSenhaHash(), m.getPermissao().name());
                }
            }
            csvPrinter.flush();
        } catch (IOException e) {
            System.err.println("Erro ao salvar pessoas no CSV: " + e.getMessage());
        }
    }

    private void carregarDoArquivo() {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(NOME_ARQUIVO));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(CABECALHO)
                        .withFirstRecordAsHeader().withTrim());
        ) {
            this.pessoas.clear();
            for (CSVRecord csvRecord : csvParser) {
                String tipo = csvRecord.get("tipo");
                Pessoa pessoa = null;

                if ("Funcionario".equalsIgnoreCase(tipo)) {
                    pessoa = new Funcionario(
                            csvRecord.get("nome"), csvRecord.get("cpf"), csvRecord.get("telefone"),
                            Integer.parseInt(csvRecord.get("idade")), csvRecord.get("login"),
                            csvRecord.get("senhaHash"), Permissao.valueOf(csvRecord.get("permissao"))
                    );
                } else if ("Membro".equalsIgnoreCase(tipo)) {
                    pessoa = new Membro(
                            csvRecord.get("nome"), csvRecord.get("cpf"), csvRecord.get("telefone"),
                            Integer.parseInt(csvRecord.get("idade")), csvRecord.get("login"),
                            csvRecord.get("senhaHash"), Permissao.valueOf(csvRecord.get("permissao"))
                    );
                }

                if (pessoa != null) {
                    pessoa.setId(Integer.parseInt(csvRecord.get("id")));
                    this.pessoas.add(pessoa);
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
        if (!this.pessoas.isEmpty()) {
            this.proximoId = this.pessoas.stream().mapToInt(Pessoa::getId).max().orElse(0) + 1;
        } else {
            this.proximoId = 1;
        }
    }
}
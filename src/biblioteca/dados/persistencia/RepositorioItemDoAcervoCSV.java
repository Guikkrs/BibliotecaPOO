package biblioteca.dados.persistencia;

import biblioteca.dados.repositorio.IRepositorioAutor;
import biblioteca.dados.repositorio.IRepositorioItemDoAcervo;
import biblioteca.negocios.entidade.Autor;
import biblioteca.negocios.entidade.ItemDoAcervo;
import biblioteca.negocios.entidade.Livro;
import biblioteca.negocios.enums.EnumSetor;
import biblioteca.negocios.enums.EnumStatusItem;
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
import java.util.stream.Collectors;

public class RepositorioItemDoAcervoCSV implements IRepositorioItemDoAcervo {

    private List<ItemDoAcervo> acervo;
    private int proximoId = 1;

    // Dependência para buscar os autores ao carregar os livros
    private final IRepositorioAutor repositorioAutor;

    private static final String NOME_ARQUIVO = "acervo.csv";
    private static final String[] CABECALHO = {"id", "tipo", "titulo", "id_autor", "ano", "setor", "paginas", "isbn", "quantidade", "status"};

    public RepositorioItemDoAcervoCSV(IRepositorioAutor repositorioAutor) {
        this.repositorioAutor = repositorioAutor;
        this.acervo = new ArrayList<>();
        carregarDoArquivo();
    }

    @Override
    public void adicionar(ItemDoAcervo item) {
        item.setId(this.proximoId++);
        this.acervo.add(item);
        salvarNoArquivo();
    }

    @Override
    public void atualizar(ItemDoAcervo item) {
        for (int i = 0; i < acervo.size(); i++) {
            if (acervo.get(i).getId() == item.getId()) {
                acervo.set(i, item);
                salvarNoArquivo();
                return;
            }
        }
    }

    @Override
    public void remover(ItemDoAcervo item) {
        if (this.acervo.remove(item)) {
            salvarNoArquivo();
        }
    }

    @Override
    public ItemDoAcervo buscarPorTitulo(String titulo) {
        return this.acervo.stream()
                .filter(item -> item.getTitulo().equalsIgnoreCase(titulo))
                .findFirst()
                .orElse(null);
    }

    @Override
    public ItemDoAcervo buscarPorId(int id) {
        return this.acervo.stream()
                .filter(item -> item.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<ItemDoAcervo> listarTodos() {
        return new ArrayList<>(this.acervo);
    }

    @Override
    public List<ItemDoAcervo> buscarPorSetor(EnumSetor setor) {
        return this.acervo.stream()
                .filter(item -> item.getSetor() == setor)
                .collect(Collectors.toList());
    }

    private void salvarNoArquivo() {
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(NOME_ARQUIVO));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(CABECALHO))
        ) {
            for (ItemDoAcervo item : this.acervo) {
                if (item instanceof Livro) {
                    Livro livro = (Livro) item;
                    csvPrinter.printRecord(
                            livro.getId(),
                            "Livro", // Tipo do item
                            livro.getTitulo(),
                            livro.getAutor().getId(), // Salva o ID do autor
                            livro.getAno(),
                            livro.getSetor().name(),
                            livro.getNumeroDePaginas(),
                            livro.getIsbn(),
                            livro.getQuantidade(),
                            livro.getStatus().name()
                    );
                }
                // Se tivesse outros tipos de item (Revista, CD), adicionaria um "else if" aqui
            }
            csvPrinter.flush();
        } catch (IOException e) {
            System.err.println("Erro ao salvar acervo no CSV: " + e.getMessage());
        }
    }

    private void carregarDoArquivo() {
        try (
                Reader reader = Files.newBufferedReader(Paths.get(NOME_ARQUIVO));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader(CABECALHO)
                        .withFirstRecordAsHeader().withTrim());
        ) {
            this.acervo.clear();
            for (CSVRecord csvRecord : csvParser) {
                String tipo = csvRecord.get("tipo");

                if ("Livro".equalsIgnoreCase(tipo)) {
                    int idAutor = Integer.parseInt(csvRecord.get("id_autor"));
                    Autor autor = this.repositorioAutor.buscarPorId(idAutor); // Busca o objeto Autor

                    if (autor != null) {
                        Livro livro = new Livro(
                                csvRecord.get("titulo"),
                                autor, // Usa o objeto Autor encontrado
                                Integer.parseInt(csvRecord.get("ano")),
                                EnumSetor.valueOf(csvRecord.get("setor")),
                                Integer.parseInt(csvRecord.get("paginas")),
                                csvRecord.get("isbn"),
                                Integer.parseInt(csvRecord.get("quantidade"))
                        );
                        livro.setId(Integer.parseInt(csvRecord.get("id")));
                        livro.setStatus(EnumStatusItem.valueOf(csvRecord.get("status")));
                        this.acervo.add(livro);
                    } else {
                        System.err.println("AVISO: Autor com ID " + idAutor + " não encontrado para o livro '" + csvRecord.get("titulo") + "'. Livro não carregado.");
                    }
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
        if (!this.acervo.isEmpty()) {
            this.proximoId = this.acervo.stream().mapToInt(ItemDoAcervo::getId).max().orElse(0) + 1;
        } else {
            this.proximoId = 1;
        }
    }


}
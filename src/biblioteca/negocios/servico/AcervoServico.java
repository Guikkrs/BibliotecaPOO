package biblioteca.negocios.servico;

// 1. IMPORTAR AS DEPENDÊNCIAS NECESSÁRIAS
import biblioteca.dados.repositorio.IRepositorioAutor;
import biblioteca.negocios.entidade.Autor;
import biblioteca.dados.repositorio.IRepositorioEmprestimo;
import biblioteca.dados.repositorio.IRepositorioItemDoAcervo;
import biblioteca.negocios.entidade.ItemDoAcervo;
import biblioteca.negocios.entidade.Livro;
import biblioteca.negocios.enums.StatusEmprestimo;
import biblioteca.negocios.excecoes.acervo.ItemComPendenciasException;
import biblioteca.negocios.excecoes.itemDoAcervo.ItemNaoEncontradoException;
import biblioteca.negocios.excecoes.validacao.ValidacaoException;
import biblioteca.negocios.enums.EnumSetor;

import java.util.List;
import java.util.stream.Collectors;

public class AcervoServico {

    private final IRepositorioItemDoAcervo repositorioAcervo;
    private final IRepositorioEmprestimo repositorioEmprestimo;
    private final IRepositorioAutor repositorioAutor; // ADICIONADA NOVA DEPENDÊNCIA

    // CONSTRUTOR ATUALIZADO
    public AcervoServico(IRepositorioItemDoAcervo repositorioAcervo, IRepositorioEmprestimo repositorioEmprestimo, IRepositorioAutor repositorioAutor) {
        this.repositorioAcervo = repositorioAcervo;
        this.repositorioEmprestimo = repositorioEmprestimo;
        this.repositorioAutor = repositorioAutor;
    }

    // MÉTODO COM A LÓGICA DE NEGÓCIO CENTRALIZADA
    public void cadastrarLivro(Livro livro) throws ValidacaoException {
        if (livro.getTitulo() == null || livro.getTitulo().trim().isEmpty()) {
            throw new ValidacaoException("O título do livro não pode ser vazio.");
        }

        // LÓGICA "ENCONTRE OU CRIE" O AUTOR
        Autor autorDoLivro = livro.getAutor();
        Autor autorExistente = repositorioAutor.buscarPorNome(autorDoLivro.getNome());

        if (autorExistente == null) {
            // Se o autor não existe, salva o novo autor para gerar um ID
            repositorioAutor.adicionar(autorDoLivro);
        } else {
            // Se o autor já existe, usa a referência encontrada no repositório
            livro.setAutor(autorExistente);
        }

        repositorioAcervo.adicionar(livro);
    }

    // --- O RESTO DA CLASSE PERMANECE IGUAL ---

    public void adicionarCopias(int idItem, int quantidade) throws ItemNaoEncontradoException, ValidacaoException {
        if (quantidade <= 0) {
            throw new ValidacaoException("A quantidade a ser adicionada deve ser positiva.");
        }
        ItemDoAcervo item = repositorioAcervo.buscarPorId(idItem);
        if (item == null) {
            throw new ItemNaoEncontradoException(idItem);
        }
        item.setQuantidade(item.getQuantidade() + quantidade);
        repositorioAcervo.atualizar(item);
    }

    public void removerItem(int idItem) throws ItemNaoEncontradoException, ItemComPendenciasException {
        ItemDoAcervo item = repositorioAcervo.buscarPorId(idItem);
        if (item == null) {
            throw new ItemNaoEncontradoException(idItem);
        }

        boolean temEmprestimosAtivos = repositorioEmprestimo.listarTodos().stream()
                .anyMatch(e -> e.getItemDoAcervo().equals(item) && e.getStatus() == StatusEmprestimo.ATIVO);

        if (temEmprestimosAtivos) {
            throw new ItemComPendenciasException(item.getTitulo());
        }

        repositorioAcervo.remover(item);
    }

    public ItemDoAcervo buscarItemPorTitulo(String titulo) {
        return repositorioAcervo.buscarPorTitulo(titulo);
    }

    public List<Livro> buscarLivroPorAutor(String nomeAutor) {
        return repositorioAcervo.listarTodos().stream()
                .filter(item -> item instanceof Livro)
                .map(item -> (Livro) item)
                .filter(livro -> livro.getAutor().getNome().equalsIgnoreCase(nomeAutor))
                .collect(Collectors.toList());
    }

    public List<Livro> listarTodosLivros() {
        return repositorioAcervo.listarTodos().stream()
                .filter(item -> item instanceof Livro)
                .map(item -> (Livro) item)
                .collect(Collectors.toList());
    }

    public List<ItemDoAcervo> buscarPorSetor(EnumSetor setor) {
        return repositorioAcervo.buscarPorSetor(setor);
    }
}
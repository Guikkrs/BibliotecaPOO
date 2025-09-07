package biblioteca.negocios.servico;

import biblioteca.dados.repositorio.IRepositorioEmprestimo;
import biblioteca.dados.repositorio.IRepositorioItemDoAcervo;
import biblioteca.negocios.entidade.ItemDoAcervo;
import biblioteca.negocios.entidade.Livro;
import biblioteca.negocios.enums.StatusEmprestimo;
import biblioteca.negocios.excecoes.acervo.ItemComPendenciasException;
import biblioteca.negocios.excecoes.itemDoAcervo.ItemNaoEncontradoException;
import biblioteca.negocios.excecoes.validacao.ValidacaoException;

import java.util.List;
import java.util.stream.Collectors;

public class AcervoServico {

    private final IRepositorioItemDoAcervo repositorioAcervo;
    private final IRepositorioEmprestimo repositorioEmprestimo; // Dependência para validar remoção

    public AcervoServico(IRepositorioItemDoAcervo repositorioAcervo, IRepositorioEmprestimo repositorioEmprestimo) {
        this.repositorioAcervo = repositorioAcervo;
        this.repositorioEmprestimo = repositorioEmprestimo;
    }

    public void cadastrarLivro(Livro livro) throws ValidacaoException {
        if (livro.getTitulo() == null || livro.getTitulo().trim().isEmpty()) {
            throw new ValidacaoException("O título do livro não pode ser vazio.");
        }
        // Outras validações podem ser adicionadas aqui
        repositorioAcervo.adicionar(livro);
    }

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

        // Regra de Negócio: Não permitir a remoção de um item se ele tiver cópias emprestadas.
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
}


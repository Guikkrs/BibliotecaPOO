package biblioteca.dados.repositorio;

import biblioteca.negocios.entidade.Autor;
import java.util.List;

public interface IRepositorioAutor {

    void adicionar(Autor autor);

    void atualizar(Autor autor);

    void remover(Autor autor);

    Autor buscarPorNome(String nome);

    List<Autor> listarTodos();
}
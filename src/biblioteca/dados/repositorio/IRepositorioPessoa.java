package biblioteca.dados.repositorio;

import biblioteca.negocios.entidade.Pessoa;
import java.util.List;

public interface IRepositorioPessoa {

    void adicionar(Pessoa pessoa);

    Pessoa buscarPorNome(String nome);

    List<Pessoa> listarTodos();

    void remover(Pessoa pessoa);

    void atualizar(Pessoa pessoa);

    Pessoa buscarIndex(int id);
}
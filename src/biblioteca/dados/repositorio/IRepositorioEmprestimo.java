package biblioteca.dados.repositorio;

import biblioteca.negocios.entidade.Emprestimo;
import java.util.List;

public interface IRepositorioEmprestimo {

    void adicionar(Emprestimo emprestimo);

    void atualizar(Emprestimo emprestimo);

    void remover(Emprestimo emprestimo);

    Emprestimo buscarPorId(int id);

    List<Emprestimo> listarTodos();
}
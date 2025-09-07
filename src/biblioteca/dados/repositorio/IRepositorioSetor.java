package biblioteca.dados.repositorio;

import biblioteca.negocios.entidade.Setor;
import java.util.List;

public interface IRepositorioSetor {
    void adicionar(Setor setor);
    void atualizar(Setor setor);
    void remover(Setor setor);
    Setor buscarPorNome(String nome);
    Setor buscarPorId(int id);
    List<Setor> listarTodos();
}
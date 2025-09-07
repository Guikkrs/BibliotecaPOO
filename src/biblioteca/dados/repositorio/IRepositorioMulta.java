package biblioteca.dados.repositorio;

import biblioteca.negocios.entidade.Multa;
import java.util.List;

public interface IRepositorioMulta {
    void adicionar(Multa multa);
    void atualizar(Multa multa);
    void remover(Multa multa);
    Multa buscarPorId(int id);
    List<Multa> listarTodos();
}
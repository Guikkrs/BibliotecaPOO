package biblioteca.dados.repositorio;

import biblioteca.negocios.entidade.Caixa;
import java.util.List;

public interface IRepositorioCaixa {
    void adicionar(Caixa caixa);
    void atualizar(Caixa caixa);
    Caixa buscarPorId(int id);
    Caixa buscarCaixaAberto();
    List<Caixa> listarTodos();
}

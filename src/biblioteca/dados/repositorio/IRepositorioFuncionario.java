package biblioteca.dados.repositorio;

import biblioteca.negocios.entidade.Funcionario;
import java.util.List;

public interface IRepositorioFuncionario {
    void adicionar(Funcionario funcionario);
    void atualizar(Funcionario funcionario);
    void remover(Funcionario funcionario);
    Funcionario buscarPorCpf(String cpf);
    Funcionario autenticar(String login, String senha);
    List<Funcionario> listarTodos();
}

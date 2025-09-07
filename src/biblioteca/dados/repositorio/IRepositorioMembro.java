package biblioteca.dados.repositorio;

import biblioteca.negocios.entidade.Membro;
import java.util.List;

public interface IRepositorioMembro {
    void adicionar(Membro membro);
    void atualizar(Membro membro);
    void remover(Membro membro);
    Membro buscarPorCpf(String cpf);
    Membro buscarPorId(int id);
    Membro autenticar(String login, String senha);
    List<Membro> listarTodos();
}

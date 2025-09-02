package biblioteca.repositorios;

import java.util.List;

public interface Repositorio<T> {
    void salvar(T obj);
    List<T> listar();
    void atualizar(int index, T obj);
    void remover(int index);
    List<T> carregar();
}


package biblioteca.repositorios;

import java.io.IOException;
import java.util.List;

public interface Repositorio<T> {
    void adicionar(T obj);
    void remover(T obj);
    List<T> listar();
    void salvar() throws IOException;
    List<T> carregar();
}


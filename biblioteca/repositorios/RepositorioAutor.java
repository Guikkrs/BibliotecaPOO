package biblioteca.repositorios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import biblioteca.Negocio.Autor;

public class RepositorioAutor {
     private List<Autor> autores = new ArrayList<>();

    public void adicionar(Autor autor) {
        autores.add(autor);
    }

    public Optional<Autor> buscarPorNome(String nome) {
        return autores.stream()
                      .filter(autor -> autor.getNome().equalsIgnoreCase(nome))
                      .findFirst();
    }
    
    public List<Autor> listarTodos() {
        return autores;
    }
}

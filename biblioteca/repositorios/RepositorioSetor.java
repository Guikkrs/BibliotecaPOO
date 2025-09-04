package biblioteca.repositorios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import biblioteca.Negocio.Setor;

public class RepositorioSetor {
    private List<Setor> setores = new ArrayList<>();

    public void adicionar(Setor setor) {
        setores.add(setor);
    }

    public Optional<Setor> buscarPorNome(String nome) {
        return setores.stream()
                      .filter(setor -> setor.getNome().equalsIgnoreCase(nome))
                      .findFirst();
    }
}

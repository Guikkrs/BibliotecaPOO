package biblioteca.dados.repositorio;

import biblioteca.negocios.enums.EnumSetor;
import biblioteca.negocios.entidade.ItemDoAcervo;
import java.util.List;

public interface IRepositorioItemDoAcervo {

    void adicionar(ItemDoAcervo item);

    void atualizar(ItemDoAcervo item);

    void remover(ItemDoAcervo item);

    ItemDoAcervo buscarPorTitulo(String titulo);

    ItemDoAcervo buscarPorId(int id);

    List<ItemDoAcervo> listarTodos();

    List<ItemDoAcervo> buscarPorSetor(EnumSetor setor);

}
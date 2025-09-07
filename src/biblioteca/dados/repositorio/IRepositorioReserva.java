package biblioteca.dados.repositorio;

import biblioteca.negocios.entidade.Reserva;
import java.util.List;

public interface IRepositorioReserva {
    void adicionar(Reserva reserva);
    void atualizar(Reserva reserva);
    void remover(Reserva reserva);
    Reserva buscarPorId(int id);
    List<Reserva> listarTodos();
}
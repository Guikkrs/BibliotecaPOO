package biblioteca.negocios.servico;

import biblioteca.dados.repositorio.IRepositorioItemDoAcervo; // CORREÇÃO AQUI
import biblioteca.dados.repositorio.IRepositorioReserva;
import biblioteca.negocios.entidade.ItemDoAcervo;
import biblioteca.negocios.entidade.Membro;
import biblioteca.negocios.entidade.Reserva;
import biblioteca.negocios.enums.EnumStatusItem;
import biblioteca.negocios.enums.StatusReserva;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ReservaServico {

    private final IRepositorioReserva repositorioReserva;
    private final IRepositorioItemDoAcervo repositorioAcervo; // CORREÇÃO AQUI

    public ReservaServico(IRepositorioReserva repositorioReserva, IRepositorioItemDoAcervo repositorioAcervo) { // CORREÇÃO AQUI
        this.repositorioReserva = repositorioReserva;
        this.repositorioAcervo = repositorioAcervo;
    }

    /**
     * Realiza a reserva de um item para um membro.
     * @param membro O membro que está fazendo a reserva.
     * @param item O item a ser reservado.
     * @throws IllegalStateException se o item já estiver disponível ou se o membro já tiver uma reserva para este item.
     */
    public void realizarReserva(Membro membro, ItemDoAcervo item) throws IllegalStateException {
        // Regra 1: Não se pode reservar um item que está disponível na prateleira.
        if (item.verificarDisponibilidade()) {
            throw new IllegalStateException("Não é possível reservar um item que já está disponível.");
        }

        // Regra 2: Um membro não pode ter duas reservas ativas para o mesmo item.
        boolean jaTemReserva = listarReservasAtivasPorMembro(membro).stream()
                .anyMatch(r -> r.getItemDoAcervo().equals(item));

        if (jaTemReserva) {
            throw new IllegalStateException("Membro já possui uma reserva ativa para este item.");
        }

        Reserva novaReserva = new Reserva(membro, item, LocalDate.now());
        repositorioReserva.adicionar(novaReserva);

        // Atualiza o status do item para refletir que há uma reserva sobre ele
        if (item.getStatus() == EnumStatusItem.EMPRESTADO) {
            item.setStatus(EnumStatusItem.RESERVADO);
            repositorioAcervo.atualizar(item);
        }
    }

    public List<Reserva> listarReservasAtivasPorMembro(Membro membro) {
        return repositorioReserva.listarTodos().stream()
                .filter(r -> r.getMembro().equals(membro) && r.getStatus() == StatusReserva.ATIVA)
                .collect(Collectors.toList());
    }
}

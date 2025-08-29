package biblioteca.Negocio;

import biblioteca.Enum.StatusReserva;

import java.time.LocalDate;
import java.util.Objects;

public class Reserva implements java.io.Serializable {

    private Membro membro;
    private ItemDoAcervo itemdoarcevo;
    private LocalDate dataReserva;
    private StatusReserva status;

    public Reserva(Membro membro, ItemDoAcervo itemdoarcevo, LocalDate dataReserva) {
        this.membro = membro;
        this.itemdoarcevo = itemdoarcevo;
        this.dataReserva = dataReserva;
        this.status = StatusReserva.ATIVA;
    }

    public Membro getMembro() {
        return membro;
    }

    public void setMembro(Membro membro) {
        this.membro = membro;
    }

    public ItemDoAcervo getItemdoarcevo() {
        return itemdoarcevo;
    }

    public void setItemdoarcevo(ItemDoAcervo itemdoarcevo) {
        this.itemdoarcevo = itemdoarcevo;
    }

    public LocalDate getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(LocalDate dataReserva) {
        this.dataReserva = dataReserva;
    }

    public StatusReserva getStatus() {
        return status;
    }

    public void setStatus(StatusReserva status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reserva reserva = (Reserva) o;
        return membro.equals(reserva.membro) && itemdoarcevo.equals(reserva.itemdoarcevo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(membro, itemdoarcevo);
    }
}

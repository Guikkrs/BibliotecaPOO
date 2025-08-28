package biblioteca.Negocio;

import biblioteca.Enum.StatusEmprestimo;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

public class Reserva {

    private Membro membro;
    private ItemDoAcervo itemdoarcevo;
    private Date dataReserva;
    private StatusEmprestimo status;

    public Reserva(Membro membro, ItemDoAcervo itemdoarcevo, LocalDate dataReserva) {
        this.membro = membro;
        this.itemdoarcevo = itemdoarcevo;
        this.dataReserva = dataReserva;
        this.status = StatusEmprestimo.ATIVA;
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

    public Date getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(Date dataReserva) {
        this.dataReserva = dataReserva;
    }

    public StatusEmprestimo getStatus() {
        return status;
    }

    public void setStatus(StatusEmprestimo status) {
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

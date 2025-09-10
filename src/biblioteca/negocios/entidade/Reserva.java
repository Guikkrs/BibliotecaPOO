package biblioteca.negocios.entidade;

import biblioteca.negocios.enums.StatusReserva;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Representa a reserva de um ItemDoAcervo por um Membro.
 * A classe é Serializable para permitir a persistência de seus objetos.
 */
public class Reserva implements java.io.Serializable {

    private int id; // Identificador único para a persistência
    private Membro membro;
    private ItemDoAcervo itemDoAcervo; // Corrigido para camelCase
    private LocalDate dataReserva;
    private StatusReserva status;

    public Reserva(Membro membro, ItemDoAcervo itemDoAcervo, LocalDate dataReserva) {
        this.membro = membro;
        this.itemDoAcervo = itemDoAcervo;
        this.dataReserva = dataReserva;
        this.status = StatusReserva.ATIVA;
    }

    // GETTERS E SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Membro getMembro() {
        return membro;
    }

    public void setMembro(Membro membro) {
        this.membro = membro;
    }

    public ItemDoAcervo getItemDoAcervo() { // Corrigido para camelCase
        return itemDoAcervo;
    }

    public void setItemDoAcervo(ItemDoAcervo itemDoAcervo) { // Corrigido para camelCase
        this.itemDoAcervo = itemDoAcervo;
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

    // MÉTODOS PADRÃO (EQUALS, HASHCODE, TOSTRING)

    /**
     * MODIFICAÇÃO: A igualdade agora é baseada estritamente no ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserva reserva = (Reserva) o;
        if (this.id == 0 || reserva.id == 0) return false;
        return id == reserva.id;
    }

    /**
     * MODIFICAÇÃO: O hashCode agora é baseado no ID.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "id=" + id +
                ", membro=" + membro.getNome() +
                ", item=" + itemDoAcervo.getTitulo() +
                ", status=" + status +
                '}';
    }
}
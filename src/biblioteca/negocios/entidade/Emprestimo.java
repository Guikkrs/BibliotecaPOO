package biblioteca.negocios.entidade;

import biblioteca.negocios.enums.StatusEmprestimo;
import java.time.LocalDate;
import java.util.Objects; // Import necessário para Objects.hash()

/**
 * Representa o ato de um Membro pegar um ItemDoAcervo emprestado.
 * A classe é Serializable para permitir a persistência de seus objetos.
 */
public class Emprestimo implements java.io.Serializable {

    private int id; // Identificador único para a persistência
    private Membro membro;
    private ItemDoAcervo itemDoAcervo;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate devolucaoRealizada;
    private StatusEmprestimo status;

    public Emprestimo(Membro membro, ItemDoAcervo itemDoAcervo, LocalDate dataEmprestimo) {
        this.membro = membro;
        this.itemDoAcervo = itemDoAcervo;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataEmprestimo.plusDays(14); // Prazo padrão de 14 dias
        this.status = StatusEmprestimo.ATIVO;
        this.devolucaoRealizada = null;
    }

    // GETTERS E SETTERS

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Membro getMembro() {
        return membro;
    }

    public ItemDoAcervo getItemDoAcervo() {
        return itemDoAcervo;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDate getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public LocalDate getDevolucaoRealizada() {
        return devolucaoRealizada;
    }

    public StatusEmprestimo getStatus() {
        return status;
    }

    public void setStatus(StatusEmprestimo status) {
        this.status = status;
    }

    public void setDevolucaoRealizada(LocalDate devolucaoRealizada) {
        this.devolucaoRealizada = devolucaoRealizada;
    }

    // MÉTODOS DE NEGÓCIO

    /**
     * MODIFICAÇÃO: Verifica se um empréstimo ATIVO ultrapassou a data de devolução.
     * Um empréstimo que já foi devolvido não é considerado "atrasado", embora
     * a devolução possa ter ocorrido com atraso (o que gera a multa).
     * @return true se o empréstimo está ativo e a data atual é posterior à data prevista de devolução.
     */
    public boolean estaAtrasado() {
        if (this.status != StatusEmprestimo.ATIVO) {
            return false;
        }
        return LocalDate.now().isAfter(this.dataDevolucaoPrevista);
    }

    /**
     * Finaliza um empréstimo, registrando a data de devolução e atualizando o status.
     * @param dataDevolucao A data em que o item foi efetivamente devolvido.
     */
    public void finalizarEmprestimo(LocalDate dataDevolucao) {
        setDevolucaoRealizada(dataDevolucao);
        setStatus(StatusEmprestimo.DEVOLVIDO);
    }

    // MÉTODOS PADRÃO (EQUALS, HASHCODE, TOSTRING)

    /**
     * ADICIONADO: A igualdade agora é baseada estritamente no ID.
     * Isso garante que cada empréstimo salvo na persistência seja um registro único.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Emprestimo that = (Emprestimo) o;
        // Se o id for 0, o objeto ainda não foi persistido.
        if (this.id == 0 || that.id == 0) return false;
        return id == that.id;
    }

    /**
     * ADICIONADO: O hashCode agora é baseado no ID, para ser consistente com o equals.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Emprestimo{" +
                "id=" + id +
                ", membro=" + membro.getNome() +
                ", item=" + itemDoAcervo.getTitulo() +
                ", dataEmprestimo=" + dataEmprestimo +
                ", status=" + status +
                '}';
    }
}
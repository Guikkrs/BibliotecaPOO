package biblioteca.negocios.entidade;

import biblioteca.negocios.enums.StatusMulta;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Representa uma penalidade financeira aplicada a um Membro por atraso na devolução.
 * A classe é Serializable para permitir a persistência de seus objetos.
 */
public class Multa implements java.io.Serializable {

    private int id; // Identificador único para a persistência
    private Membro membro;
    private Emprestimo emprestimo;
    private BigDecimal valor;
    private StatusMulta status;
    private LocalDate dataCriacao;
    private LocalDate dataPagamento;

    public Multa(Membro membro, Emprestimo emprestimo, BigDecimal valor) {
        if (membro == null || emprestimo == null || valor == null) {
            throw new IllegalArgumentException("Membro, empréstimo e valor não podem ser nulos.");
        }
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da multa deve ser positivo.");
        }
        this.membro = membro;
        this.emprestimo = emprestimo;
        this.valor = valor;
        this.status = StatusMulta.PENDENTE;
        this.dataCriacao = LocalDate.now();
        this.dataPagamento = null;
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

    public Emprestimo getEmprestimo() {
        return emprestimo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public StatusMulta getStatus() {
        return status;
    }

    public void setStatus(StatusMulta status) {
        this.status = status;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    // MÉTODOS DE NEGÓCIO

    /**
     * Registra o pagamento da multa, atualizando o status e a data de pagamento.
     * @return true se o pagamento foi registrado com sucesso (multa estava pendente), false caso contrário.
     */
    public boolean registrarPagamento() {
        if (this.status == StatusMulta.PENDENTE) {
            this.status = StatusMulta.PAGA;
            this.dataPagamento = LocalDate.now();
            return true;
        }
        return false;
    }

    // MÉTODOS PADRÃO (EQUALS, HASHCODE, TOSTRING)

    /**
     * MODIFICAÇÃO: A igualdade agora é baseada estritamente no ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Multa multa = (Multa) o;
        if (this.id == 0 || multa.id == 0) return false;
        return id == multa.id;
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
        return "Multa{" +
                "id=" + id +
                ", membro=" + membro.getNome() +
                ", valor=" + valor +
                ", status=" + status +
                '}';
    }
}
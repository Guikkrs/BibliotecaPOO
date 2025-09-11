package biblioteca.negocios.entidade;

import biblioteca.negocios.enums.StatusMulta;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Multa implements java.io.Serializable {

    private int id;
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


    public Multa(int id, Membro membro, Emprestimo emprestimo, BigDecimal valor, StatusMulta status, LocalDate dataCriacao, LocalDate dataPagamento) {
        this.id = id;
        this.membro = membro;
        this.emprestimo = emprestimo;
        this.valor = valor;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.dataPagamento = dataPagamento;
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

    public boolean registrarPagamento() {
        if (this.status == StatusMulta.PENDENTE) {
            this.status = StatusMulta.PAGA;
            this.dataPagamento = LocalDate.now();
            return true;
        }
        return false;
    }

    // MÉTODOS PADRÃO (EQUALS, HASHCODE, TOSTRING)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Multa multa = (Multa) o;
        if (this.id == 0 || multa.id == 0) return false;
        return id == multa.id;
    }

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
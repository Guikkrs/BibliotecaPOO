package biblioteca.negocios;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import biblioteca.negocios.enums.StatusMulta;

public class Multa implements java.io.Serializable {

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

    public boolean registrarPagamento() {
        if (this.status == StatusMulta.PENDENTE) {
            this.status = StatusMulta.PAGA;
            this.dataPagamento = LocalDate.now();
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Multa multa = (Multa) o;
        return emprestimo.equals(multa.emprestimo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emprestimo);
    }
}
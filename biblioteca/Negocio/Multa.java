package biblioteca.Negocio;

import biblioteca.Enum.StatusMulta;
import java.math.BigDecimal;

public class Multa {

    private Membro membro;
    private Emprestimo emprestimo;
    private BigDecimal valor;
    private StatusMulta status;

    public Multa(Membro membro, Emprestimo emprestimo, BigDecimal valor, StatusMulta status) {
        this.membro = membro;
        this.emprestimo = emprestimo;
        this.valor = valor;
        this.status = status;
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

    public void registrarPagamento() {
        if (this.status == StatusMulta.PENDENTE) {
            this.status = StatusMulta.PAGA;
        }
    }
}

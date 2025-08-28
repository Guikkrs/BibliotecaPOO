package biblioteca.Negocio;

import biblioteca.Enum.StatusCaixa;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Caixa implements java.io.Serializable {

    private LocalDate dataAbertura;
    private LocalDate dataFechamento;
    private BigDecimal saldoInicial;
    private BigDecimal saldoFinal;
    private StatusCaixa status;
    private BigDecimal saldoAtual;

    public Caixa() {
        this.saldoInicial = BigDecimal.ZERO;
        this.saldoFinal = BigDecimal.ZERO;
        this.saldoAtual = BigDecimal.ZERO;
        this.status = StatusCaixa.FECHADO;
    }

    public void abrirCaixa(BigDecimal saldoInicial1) {
        if (this.status == StatusCaixa.ABERTO) {
            throw new IllegalStateException("O caixa já está aberto.");
        }
        this.dataAbertura = LocalDate.now();
        this.saldoInicial = this.saldoAtual;
        this.status = StatusCaixa.ABERTO;
        System.out.println("Caixa aberto em " + this.dataAbertura);
    }

    /**
     * Registra uma entrada de valor no caixa.
     *
     * @param valor O valor a ser adicionado.
     */
    public void registrarEntrada(BigDecimal valor) {
        if (this.status != StatusCaixa.ABERTO) {
            throw new IllegalStateException("O caixa deve estar aberto para registrar entradas.");
        }
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da entrada deve ser positivo.");
        }
        this.saldoAtual = this.saldoAtual.add(valor);
        System.out.println("Entrada de R$ " + valor + " registrada. Saldo atual: R$ " + this.saldoAtual);
    }

    public void fecharCaixa() {
        if (this.status != StatusCaixa.ABERTO) {
            throw new IllegalStateException("O caixa já está fechado.");
        }
        this.dataFechamento = LocalDate.now();
        this.saldoFinal = this.saldoAtual;
        this.status = StatusCaixa.FECHADO;
        System.out.println("Caixa fechado em " + this.dataFechamento + ". Saldo final: R$ " + this.saldoFinal);
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
    }

    public LocalDate getDataFechamento() {
        return dataFechamento;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public BigDecimal getSaldoFinal() {
        return saldoFinal;
    }

    public StatusCaixa getStatus() {
        return status;
    }
}

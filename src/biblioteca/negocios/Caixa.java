package biblioteca.negocios;

import java.math.BigDecimal;
import java.time.LocalDate;

import biblioteca.negocios.enums.StatusCaixa;

public class Caixa implements java.io.Serializable {

    private final LocalDate dataAbertura;
    private final BigDecimal saldoInicial;

    private LocalDate dataFechamento;
    private BigDecimal saldoFinal;
    private BigDecimal saldoAtual;
    private StatusCaixa status;

    public Caixa(BigDecimal saldoInicial) {
        if (saldoInicial == null || saldoInicial.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O saldo inicial não pode ser nulo ou negativo.");
        }
        this.dataAbertura = LocalDate.now();
        this.saldoInicial = saldoInicial;
        this.saldoAtual = saldoInicial;
        this.status = StatusCaixa.ABERTO;
        this.dataFechamento = null;
        this.saldoFinal = null;
    }

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

    public BigDecimal getSaldoAtual() {
        return saldoAtual;
    }
}
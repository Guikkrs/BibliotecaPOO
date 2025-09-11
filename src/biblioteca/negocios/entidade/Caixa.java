// Em: src/biblioteca/negocios/entidade/Caixa.java

package biblioteca.negocios.entidade;

import biblioteca.negocios.enums.StatusCaixa;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Caixa implements java.io.Serializable {

    private int id;
    // --- ALTERAÇÃO AQUI: REMOVA O 'final' ---
    private LocalDate dataAbertura;
    private BigDecimal saldoInicial;

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

    // --- NOVO CONSTRUTOR ADICIONADO AQUI ---
    /**
     * Construtor para carregar um Caixa a partir da persistência (CSV).
     * Este construtor não executa validações, pois assume que os dados já são válidos.
     */
    public Caixa(int id, LocalDate dataAbertura, BigDecimal saldoInicial, LocalDate dataFechamento, BigDecimal saldoFinal, BigDecimal saldoAtual, StatusCaixa status) {
        this.id = id;
        this.dataAbertura = dataAbertura;
        this.saldoInicial = saldoInicial;
        this.dataFechamento = dataFechamento;
        this.saldoFinal = saldoFinal;
        this.saldoAtual = saldoAtual;
        this.status = status;
    }

    // O resto da classe (getters, setters, etc.) continua igual...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    // ... (cole o resto da sua classe Caixa aqui)
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

    public BigDecimal getSaldoAtual() {
        return saldoAtual;
    }

    public StatusCaixa getStatus() {
        return status;
    }

    public void registrarEntrada(BigDecimal valor) {
        if (this.status != StatusCaixa.ABERTO) {
            throw new IllegalStateException("O caixa deve estar aberto para registrar entradas.");
        }
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da entrada deve ser positivo.");
        }
        this.saldoAtual = this.saldoAtual.add(valor);
    }

    public void fecharCaixa() {
        if (this.status != StatusCaixa.ABERTO) {
            throw new IllegalStateException("O caixa já está fechado.");
        }
        this.dataFechamento = LocalDate.now();
        this.saldoFinal = this.saldoAtual;
        this.status = StatusCaixa.FECHADO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Caixa caixa = (Caixa) o;
        if (this.id == 0 || caixa.id == 0) return false;
        return id == caixa.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Caixa{" +
                "id=" + id +
                ", dataAbertura=" + dataAbertura +
                ", saldoAtual=" + saldoAtual +
                ", status=" + status +
                '}';
    }

}
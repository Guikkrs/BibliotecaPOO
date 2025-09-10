package biblioteca.negocios.entidade;

import biblioteca.negocios.enums.StatusCaixa;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Representa o registro financeiro de um dia de operação da biblioteca.
 * Cada instância desta classe pode ser persistida como um registro histórico.
 * A classe é Serializable para permitir a persistência de seus objetos.
 */
public class Caixa implements java.io.Serializable {

    private int id; // Identificador único para a persistência
    private final LocalDate dataAbertura;
    private final BigDecimal saldoInicial;

    private LocalDate dataFechamento;
    private BigDecimal saldoFinal;
    private BigDecimal saldoAtual;
    private StatusCaixa status;

    public Caixa(BigDecimal saldoInicial) {
        // Validação para garantir que o caixa seja criado em um estado consistente
        if (saldoInicial == null || saldoInicial.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O saldo inicial não pode ser nulo ou negativo.");
        }
        this.dataAbertura = LocalDate.now();
        this.saldoInicial = saldoInicial;
        this.saldoAtual = saldoInicial;
        this.status = StatusCaixa.ABERTO;
        this.dataFechamento = null; // Inicia como nulo
        this.saldoFinal = null;     // Inicia como nulo
    }

    // GETTERS E SETTERS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public BigDecimal getSaldoAtual() {
        return saldoAtual;
    }

    public StatusCaixa getStatus() {
        return status;
    }

    // MÉTODOS DE NEGÓCIO DA ENTIDADE

    /**
     * Adiciona um valor ao saldo atual do caixa.
     * Lança uma exceção se o caixa não estiver aberto ou se o valor for inválido.
     * @param valor O valor a ser adicionado (deve ser positivo).
     */
    public void registrarEntrada(BigDecimal valor) {
        if (this.status != StatusCaixa.ABERTO) {
            throw new IllegalStateException("O caixa deve estar aberto para registrar entradas.");
        }
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da entrada deve ser positivo.");
        }
        this.saldoAtual = this.saldoAtual.add(valor);
    }

    /**
     * Fecha o caixa, definindo a data de fechamento e o saldo final.
     * Lança uma exceção se o caixa já estiver fechado.
     */
    public void fecharCaixa() {
        if (this.status != StatusCaixa.ABERTO) {
            throw new IllegalStateException("O caixa já está fechado.");
        }
        this.dataFechamento = LocalDate.now();
        this.saldoFinal = this.saldoAtual;
        this.status = StatusCaixa.FECHADO;
    }

    // MÉTODOS PADRÃO (EQUALS, HASHCODE, TOSTRING)

    /**
     * ADICIONADO: A igualdade agora é baseada estritamente no ID.
     * Essencial para que a camada de persistência identifique cada registro de caixa de forma única.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Caixa caixa = (Caixa) o;
        if (this.id == 0 || caixa.id == 0) return false;
        return id == caixa.id;
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
        return "Caixa{" +
                "id=" + id +
                ", dataAbertura=" + dataAbertura +
                ", saldoAtual=" + saldoAtual +
                ", status=" + status +
                '}';
    }
}
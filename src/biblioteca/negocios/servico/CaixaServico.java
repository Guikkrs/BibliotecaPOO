package biblioteca.negocios.servico;

import biblioteca.dados.repositorio.IRepositorioCaixa;
import biblioteca.dados.repositorio.IRepositorioMulta;
import biblioteca.negocios.entidade.Caixa;
import biblioteca.negocios.entidade.Multa;
import java.math.BigDecimal;

public class CaixaServico {

    private final IRepositorioCaixa repositorioCaixa;
    private final IRepositorioMulta repositorioMulta;

    public CaixaServico(IRepositorioCaixa repositorioCaixa, IRepositorioMulta repositorioMulta) {
        this.repositorioCaixa = repositorioCaixa;
        this.repositorioMulta = repositorioMulta;
    }

    public Caixa abrirCaixa(BigDecimal saldoInicial) throws IllegalStateException {
        if (repositorioCaixa.buscarCaixaAberto() != null) {
            throw new IllegalStateException("Já existe um caixa aberto.");
        }
        Caixa novoCaixa = new Caixa(saldoInicial);
        repositorioCaixa.adicionar(novoCaixa);
        return novoCaixa;
    }

    public void registrarPagamentoDeMulta(Multa multa) throws IllegalStateException {
        Caixa caixaAberto = repositorioCaixa.buscarCaixaAberto();
        if (caixaAberto == null) {
            throw new IllegalStateException("Não há caixa aberto para registrar o pagamento.");
        }
        if (multa.registrarPagamento()) {
            caixaAberto.registrarEntrada(multa.getValor());
            repositorioCaixa.atualizar(caixaAberto);
            repositorioMulta.atualizar(multa);
        }
    }

    public Caixa fecharCaixa() throws IllegalStateException {
        Caixa caixaAberto = repositorioCaixa.buscarCaixaAberto();
        if (caixaAberto == null) {
            throw new IllegalStateException("Não há caixa aberto para fechar.");
        }
        caixaAberto.fecharCaixa();
        repositorioCaixa.atualizar(caixaAberto);
        return caixaAberto;
    }

    public Caixa getCaixaAberto() {
        return repositorioCaixa.buscarCaixaAberto();
    }
}

package biblioteca.UI;

import biblioteca.fachada.Fachada;
import biblioteca.negocios.entidade.Caixa;
import biblioteca.negocios.entidade.Membro;
import biblioteca.negocios.entidade.Multa;
import biblioteca.negocios.excecoes.caixa.CaixaAbertoException;
import biblioteca.negocios.excecoes.caixa.CaixaFechadoException;
import biblioteca.negocios.excecoes.login.PermissaoInsuficienteException;
import biblioteca.negocios.excecoes.pessoa.PessoaNaoEncontradaException;
import biblioteca.negocios.excecoes.validacao.ValidacaoException; // Import necessário

import java.math.BigDecimal;
import java.util.List;

public class TelaGerenciarCaixa {

    public static void exibir() {
        boolean executando = true;
        while(executando) {
            UIUtils.exibirCabecalho("GERENCIAR CAIXA");
            System.out.println("[1] Abrir Caixa");
            System.out.println("[2] Receber Pagamento de Multa");
            System.out.println("[3] Fechar Caixa");
            System.out.println("[4] Ver Saldo do Caixa Aberto");
            System.out.println("[0] Voltar ao Menu Principal");

            int opcao = UIUtils.lerOpcao();

            switch (opcao) {
                case 1 -> abrirCaixa();
                case 2 -> receberMulta();
                case 3 -> fecharCaixa();
                case 4 -> verSaldo();
                case 0 -> executando = false;
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void abrirCaixa() {
        UIUtils.exibirCabecalho("ABRIR CAIXA");
        try {
            BigDecimal saldoInicial = UIUtils.lerBigDecimal("Saldo inicial (ex: 100.50): ");
            Fachada.getInstance().abrirCaixa(saldoInicial);
            System.out.println("SUCESSO: Caixa aberto!");
        } catch (CaixaAbertoException | PermissaoInsuficienteException | ValidacaoException e) { // CORREÇÃO AQUI
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    private static void receberMulta() {
        UIUtils.exibirCabecalho("RECEBER MULTA");
        Fachada fachada = Fachada.getInstance();
        try {
            String cpf = UIUtils.lerString("CPF do membro: ");
            Membro membro = fachada.buscarMembroPorCpf(cpf);
            if (membro == null) throw new PessoaNaoEncontradaException(cpf);

            List<Multa> multas = fachada.debitosPendentes(membro);
            if (multas.isEmpty()) {
                System.out.println("INFO: O membro não possui multas pendentes.");
                return;
            }

            System.out.println("Multas pendentes para " + membro.getNome() + ":");
            for (int i = 0; i < multas.size(); i++) {
                Multa m = multas.get(i);
                System.out.printf("[%d] - Valor: R$%.2f - Referente a: %s\n",
                        (i + 1), m.getValor(), m.getEmprestimo().getItemDoAcervo().getTitulo());
            }

            int opcao = UIUtils.lerInt("Escolha o número da multa a ser paga (0 para cancelar): ");
            if (opcao > 0 && opcao <= multas.size()) {
                Multa multaPaga = multas.get(opcao - 1);
                fachada.pagarMulta(multaPaga);
                System.out.println("SUCESSO: Pagamento da multa registrado!");
            }
        } catch (PessoaNaoEncontradaException | CaixaFechadoException | PermissaoInsuficienteException | ValidacaoException e) { // CORREÇÃO AQUI
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    private static void fecharCaixa() {
        UIUtils.exibirCabecalho("FECHAR CAIXA");
        try {
            Caixa caixaFechado = Fachada.getInstance().fecharCaixa();
            System.out.printf("SUCESSO: Caixa fechado com saldo final de R$%.2f\n", caixaFechado.getSaldoFinal());
        } catch (CaixaFechadoException | PermissaoInsuficienteException | ValidacaoException e) { // CORREÇÃO AQUI
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    private static void verSaldo() {
        Caixa caixaAberto = Fachada.getInstance().getCaixaAberto();
        if (caixaAberto != null) {
            System.out.printf("INFO: Saldo atual do caixa: R$%.2f\n", caixaAberto.getSaldoAtual());
        } else {
            System.out.println("INFO: Não há caixa aberto no momento.");
        }
    }
}
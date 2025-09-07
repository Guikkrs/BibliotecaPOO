package biblioteca.UI;

import biblioteca.fachada.Fachada;
import biblioteca.negocios.entidade.Emprestimo;
import biblioteca.negocios.entidade.ItemDoAcervo;
import biblioteca.negocios.entidade.Membro;
import biblioteca.negocios.excecoes.emprestimo.EmprestimoNaoEncontradoException;
import biblioteca.negocios.excecoes.itemDoAcervo.ItemNaoEncontradoException;
import biblioteca.negocios.excecoes.pessoa.PessoaNaoEncontradaException;

public class TelaGerenciarEmprestimos {

    public static void exibir() {
        boolean executando = true;
        while(executando) {
            UIUtils.exibirCabecalho("GERENCIAR EMPRÉSTIMOS");
            System.out.println("[1] Realizar Devolução");
            System.out.println("[0] Voltar ao Menu Principal");

            int opcao = UIUtils.lerOpcao();

            switch (opcao) {
                case 1 -> realizarDevolucao();
                case 0 -> executando = false;
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void realizarDevolucao() {
        UIUtils.exibirCabecalho("REALIZAR DEVOLUÇÃO");
        try {
            String cpf = UIUtils.lerString("CPF do membro: ");
            String titulo = UIUtils.lerString("Título do item a ser devolvido: ");

            Fachada fachada = Fachada.getInstance();
            Membro membro = fachada.buscarMembroPorCpf(cpf);
            if (membro == null) {
                throw new PessoaNaoEncontradaException(cpf);
            }

            ItemDoAcervo item = fachada.buscarItemPorTitulo(titulo);
            if (item == null) {
                throw new ItemNaoEncontradoException(titulo);
            }

            Emprestimo emprestimo = fachada.buscarEmprestimoAtivo(membro, item);
            fachada.realizarDevolucao(emprestimo);
            System.out.println("SUCESSO: Devolução registrada. Verifique se há multas geradas.");

        } catch (PessoaNaoEncontradaException | ItemNaoEncontradoException | EmprestimoNaoEncontradoException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }
}

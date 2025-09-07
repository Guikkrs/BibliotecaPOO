package biblioteca.UI;

import biblioteca.fachada.Fachada;
import biblioteca.negocios.entidade.Funcionario;
import biblioteca.negocios.entidade.Membro;
import biblioteca.negocios.entidade.Pessoa;
import biblioteca.negocios.enums.Permissao;
import biblioteca.negocios.excecoes.login.PermissaoInsuficienteException; // Import necessário
import biblioteca.negocios.excecoes.pessoa.CpfJaExistenteException;
import biblioteca.negocios.excecoes.pessoa.MembroComPendenciasException;
import biblioteca.negocios.excecoes.pessoa.PessoaNaoEncontradaException;
import biblioteca.negocios.excecoes.validacao.ValidacaoException;

public class TelaGerenciarPessoas {

    public static void exibir() {
        boolean executando = true;
        while(executando) {
            UIUtils.exibirCabecalho("GERENCIAR PESSOAS");
            System.out.println("[1] Cadastrar Novo Membro");
            System.out.println("[2] Cadastrar Novo Funcionário");
            System.out.println("[3] Remover Membro");
            System.out.println("[4] Listar Todos os Membros");
            System.out.println("[5] Listar Todos os Funcionários");
            System.out.println("[0] Voltar ao Menu Principal");

            int opcao = UIUtils.lerOpcao();

            switch (opcao) {
                case 1 -> cadastrarMembro();
                case 2 -> cadastrarFuncionario();
                case 3 -> removerMembro();
                case 4 -> listarMembros();
                case 5 -> listarFuncionarios();
                case 0 -> executando = false;
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void cadastrarMembro() {
        UIUtils.exibirCabecalho("CADASTRO DE NOVO MEMBRO");
        try {
            String nome = UIUtils.lerString("Nome: ");
            String cpf = UIUtils.lerString("CPF: ");
            String telefone = UIUtils.lerString("Telefone: ");
            int idade = UIUtils.lerInt("Idade: ");
            String login = UIUtils.lerString("Login: ");
            String senha = UIUtils.lerString("Senha: ");

            Membro novoMembro = new Membro(nome, cpf, telefone, idade, login, senha, Permissao.MEMBRO);
            Fachada.getInstance().cadastrarMembro(novoMembro);
            System.out.println("SUCESSO: Membro cadastrado!");
        } catch (CpfJaExistenteException | ValidacaoException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    private static void cadastrarFuncionario() {
        UIUtils.exibirCabecalho("CADASTRO DE NOVO FUNCIONÁRIO");
        try {
            String nome = UIUtils.lerString("Nome: ");
            String cpf = UIUtils.lerString("CPF: ");
            String telefone = UIUtils.lerString("Telefone: ");
            int idade = UIUtils.lerInt("Idade: ");
            String login = UIUtils.lerString("Login: ");
            String senha = UIUtils.lerString("Senha: ");
            String permissaoStr = UIUtils.lerString("Permissão (ADMINISTRADOR, GERENTE, USUARIO_COMUM): ");

            Permissao permissao = Permissao.valueOf(permissaoStr.toUpperCase());
            Funcionario novoFuncionario = new Funcionario(nome, cpf, telefone, idade, login, senha, permissao);

            Fachada.getInstance().cadastrarFuncionario(novoFuncionario);
            System.out.println("SUCESSO: Funcionário cadastrado!");

        } catch (CpfJaExistenteException | ValidacaoException | PermissaoInsuficienteException e) { // CORREÇÃO AQUI
            System.out.println("ERRO de Cadastro: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("ERRO: Permissão inválida.");
        }
    }

    private static void removerMembro() {
        UIUtils.exibirCabecalho("REMOVER MEMBRO");
        try {
            String cpf = UIUtils.lerString("CPF do membro a ser removido: ");
            Fachada.getInstance().removerMembro(cpf);
            System.out.println("SUCESSO: Membro removido!");
        } catch (PessoaNaoEncontradaException | MembroComPendenciasException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    private static void listarMembros() {
        UIUtils.exibirCabecalho("LISTA DE MEMBROs");
        Fachada.getInstance().listarTodosMembros().forEach(System.out::println);
    }

    private static void listarFuncionarios() {
        UIUtils.exibirCabecalho("LISTA DE FUNCIONÁRIOS");
        Fachada.getInstance().listarTodosFuncionarios().forEach(p -> System.out.println(p.toString()));
    }
}
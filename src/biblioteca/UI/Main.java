package biblioteca.UI;

import biblioteca.Excecoes.MembroComDebitoException;
import biblioteca.Negocio.*;
import biblioteca.Enum.Permissao;
import biblioteca.repositorios.RepositorioFuncionarioCSV;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static Scanner sc = new Scanner(System.in);
    private static Biblioteca minhaBiblioteca = Biblioteca.getInstance();
    private static RepositorioFuncionarioCSV repositorioFuncionario = new RepositorioFuncionarioCSV();

    public static void main(String[] args) {
        System.out.println("=== Sistema de Biblioteca ===");

        boolean executando = true;
        while (executando) {
            System.out.println("\n[1] Entrar como Membro");
            System.out.println("[2] Entrar como Funcionário");
            System.out.println("[0] Sair");
            System.out.print("Escolha: ");
            int opcao = sc.nextInt();
            sc.nextLine(); // limpar buffer

            switch (opcao) {
                case 1 -> menuMembro();
                case 2 -> menuFuncionario();
                case 0 -> {
                    executando = false;
                    minhaBiblioteca.salvar();
                    System.out.println("Dados salvos. Até logo!");
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    // MENU DE MEMBRO
    private static void menuMembro() {
        System.out.println("\n=== Menu do Membro ===");
        System.out.println("[1] Cadastrar novo membro");
        System.out.println("[2] Buscar livro por autor");
        System.out.println("[3] Solicitar empréstimo");
        System.out.println("[0] Voltar");
        System.out.print("Escolha: ");
        int opcao = sc.nextInt();
        sc.nextLine();

        switch (opcao) {
            case 1 -> cadastrarMembro();
            case 2 -> buscarLivroAutor();
            case 3 -> solicitarEmprestimo();
            case 0 -> {}
            default -> System.out.println("Opção inválida!");
        }
    }

    private static void cadastrarMembro() {
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("CPF: ");
        String cpf = sc.nextLine();
        System.out.print("Telefone: ");
        String telefone = sc.nextLine();
        System.out.print("Idade: ");
        int idade = sc.nextInt();
        sc.nextLine();
        System.out.print("Login: ");
        String login = sc.nextLine();
        System.out.print("Senha: ");
        String senha = sc.nextLine();

        Membro novo = new Membro(nome, cpf, telefone, idade, login, senha, Permissao.MEMBRO);
        minhaBiblioteca.adicionarMembro(novo);
        System.out.println("Membro cadastrado com sucesso!");
    }

    private static void buscarLivroAutor() {
        System.out.print("Autor: ");
        String autor = sc.nextLine();
        List<Livro> livros = minhaBiblioteca.buscarLivroPorAutor(autor);
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado.");
        } else {
            livros.forEach(System.out::println);
        }
    }

    private static void solicitarEmprestimo() {
        System.out.print("CPF do membro: ");
        String cpf = sc.nextLine();
        Membro membro = minhaBiblioteca.buscarMembroPorCPF(cpf);
        if (membro == null) {
            System.out.println("Membro não encontrado.");
            return;
        }
        System.out.print("Título do livro: ");
        String titulo = sc.nextLine();
        List<Livro> livros = minhaBiblioteca.buscarLivroPorAutor(titulo); // aqui você pode trocar por busca por título
        if (livros.isEmpty()) {
            System.out.println("Livro não encontrado.");
            return;
        }
        Livro livro = livros.get(0);

        try {
            boolean sucesso = minhaBiblioteca.realizarEmprestimo(membro, livro);
            if (sucesso) {
                System.out.println("Empréstimo realizado com sucesso!");
            } else {
                System.out.println("Não foi possível realizar o empréstimo.");
            }
        } catch (MembroComDebitoException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    // MENU DE FUNCIONÁRIO
    private static void menuFuncionario() {
        System.out.print("Login: ");
        String login = sc.nextLine();
        System.out.print("Senha: ");
        String senha = sc.nextLine();

        Funcionario funcionario = repositorioFuncionario.autenticar(login, senha);

        if (funcionario == null) {
            System.out.println("Credenciais inválidas!");
            return;
        }

        System.out.println("Bem-vindo, " + funcionario.getNome() + " (" + funcionario.getPermissao() + ")");

        switch (funcionario.getPermissao()) {
            case USUARIO_COMUM -> System.out.println("Menu de funcionário comum (em construção).");
            case GERENTE, ADMINISTRADOR -> System.out.println("Menu de gerente/admin (em construção).");
            default -> System.out.println("Permissão desconhecida.");
        }
    }
}


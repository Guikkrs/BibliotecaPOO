package biblioteca.UI;

import biblioteca.Enum.EnumSetor;
import biblioteca.Enum.Permissao;
import biblioteca.Excecoes.ItemNaoEncontradoException;
import biblioteca.Excecoes.MembroComDebitoException;
import biblioteca.Negocio.*;
import biblioteca.dados.RepositorioFuncionarioCSV;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static Scanner sc = new Scanner(System.in);
    private static Biblioteca minhaBiblioteca = Biblioteca.getInstance();
    private static RepositorioFuncionarioCSV repositorioFuncionario = new RepositorioFuncionarioCSV();

    public static void main(String[] args) {
        System.out.println("=== Sistema de Biblioteca ===");

        boolean executando = true;
        while (executando) {
            System.out.println("\n[1] Listar todos os livros");
            System.out.println("[2] Entrar como Membro");
            System.out.println("[3] Entrar como Funcionário");
            System.out.println("[0] Sair");
            System.out.print("Escolha: ");
            int opcao = -1;
            try {
                opcao = sc.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                sc.next();
                continue;
            }
            sc.nextLine();

            switch (opcao) {
                case 1 -> listarTodosOsLivros();
                case 2 -> menuMembro();
                case 3 -> menuFuncionario();
                case 0 -> {
                    executando = false;
                    minhaBiblioteca.salvar();
                    System.out.println("Dados salvos. Até logo!");
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private static void listarTodosOsLivros() {
        System.out.println("\n--- Acervo de Livros ---");
        List<Livro> livros = minhaBiblioteca.getRepositorioLivro().listar();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado no acervo.");
        } else {
            livros.forEach(System.out::println);
        }
    }

    // MENU DE MEMBRO
    private static void menuMembro() {
        boolean emMenuMembro = true;
        while (emMenuMembro) {
            System.out.println("\n=== Menu do Membro ===");
            System.out.println("[1] Cadastrar novo membro");
            System.out.println("[2] Buscar livro por autor");
            System.out.println("[3] Buscar livro por título");
            System.out.println("[4] Buscar livro por palavra-chave");
            System.out.println("[5] Solicitar empréstimo");
            System.out.println("[6] Solicitar reserva");
            System.out.println("[7] Ver Meus Empréstimos e Multas"); // Opção nova
            System.out.println("[0] Voltar");
            System.out.print("Escolha: ");
            int opcao = -1;
            try {
                opcao = sc.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                sc.next();
                continue;
            }
            sc.nextLine();

            switch (opcao) {
                case 1 -> cadastrarMembro();
                case 2 -> buscarLivroAutor();
                case 3 -> buscarLivroTitulo();
                case 4 -> buscarLivroPalavraChave();
                case 5 -> solicitarEmprestimo();
                case 6 -> solicitarReserva();
                case 7 -> verEmprestimosEMultas(); // Método novo
                case 0 -> emMenuMembro = false;
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    // Método para ver empréstimos e multas de um membro (novo)
    private static void verEmprestimosEMultas() {
        System.out.print("CPF do membro: ");
        String cpf = sc.nextLine();
        Membro membro = minhaBiblioteca.buscarMembroPorCPF(cpf);
        if (membro == null) {
            System.out.println("Membro não encontrado.");
            return;
        }

        System.out.println("\n--- Empréstimos de " + membro.getNome() + " ---");
        List<Emprestimo> emprestimos = minhaBiblioteca.buscarEmprestimosDoMembro(membro);
        if (emprestimos.isEmpty()) {
            System.out.println("Nenhum empréstimo registrado.");
        } else {
            emprestimos.forEach(e -> System.out.println(" - " + e.getItemDoAcervo().getTitulo() + " (Status: " + e.getStatus() + ")"));
        }

        System.out.println("\n--- Multas Pendentes de " + membro.getNome() + " ---");
        List<Multa> multas = minhaBiblioteca.debitosPendentes(membro);
        if (multas.isEmpty()) {
            System.out.println("Nenhuma multa pendente.");
        } else {
            multas.forEach(m -> System.out.println(" - R$" + m.getValor() + " - Livro: " + m.getEmprestimo().getItemDoAcervo().getTitulo()));
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
        int idade;
        try {
            idade = sc.nextInt();
            sc.nextLine();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Idade inválida. Operação cancelada.");
            sc.nextLine();
            return;
        }
        System.out.print("Login: ");
        String login = sc.nextLine();
        System.out.print("Senha: ");
        String senha = sc.nextLine();

        Membro novo = new Membro(nome, cpf, telefone, idade, login, senha, Permissao.MEMBRO);
        minhaBiblioteca.adicionarMembro(novo);
        minhaBiblioteca.salvar();
        System.out.println("Membro cadastrado com sucesso!");
    }

    private static void buscarLivroAutor() {
        System.out.print("Nome do autor: ");
        String autor = sc.nextLine();
        List<Livro> livros = minhaBiblioteca.buscarLivroPorAutor(autor);
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado para este autor.");
        } else {
            livros.forEach(System.out::println);
        }
    }

    private static void buscarLivroTitulo() {
        System.out.print("Título do livro: ");
        String titulo = sc.nextLine();
        List<ItemDoAcervo> itens = minhaBiblioteca.buscarItemPorTitulo(titulo);
        if (itens.isEmpty()) {
            System.out.println("Nenhum livro encontrado com este título.");
        } else {
            itens.forEach(System.out::println);
        }
    }

    private static void buscarLivroPalavraChave() {
        System.out.print("Palavra-chave: ");
        String palavraChave = sc.nextLine();
        List<ItemDoAcervo> itens = minhaBiblioteca.buscarItemPorPalavraChave(palavraChave);
        if (itens.isEmpty()) {
            System.out.println("Nenhum item encontrado com esta palavra-chave.");
        } else {
            itens.forEach(System.out::println);
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
        List<ItemDoAcervo> itens = minhaBiblioteca.buscarItemPorTitulo(titulo);
        if (itens.isEmpty()) {
            System.out.println("Livro não encontrado.");
            return;
        }

        ItemDoAcervo item = itens.get(0);

        try {
            boolean sucesso = minhaBiblioteca.realizarEmprestimo(membro, item);
            if (sucesso) {
                System.out.println("Empréstimo realizado com sucesso!");
                minhaBiblioteca.salvar();
            } else {
                System.out.println("Não foi possível realizar o empréstimo. O livro pode não estar disponível.");
            }
        } catch (MembroComDebitoException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void solicitarReserva() {
        System.out.print("CPF do membro: ");
        String cpf = sc.nextLine();
        Membro membro = minhaBiblioteca.buscarMembroPorCPF(cpf);
        if (membro == null) {
            System.out.println("Membro não encontrado.");
            return;
        }

        System.out.print("Título do livro: ");
        String titulo = sc.nextLine();
        List<ItemDoAcervo> itens = minhaBiblioteca.buscarItemPorTitulo(titulo);
        if (itens.isEmpty()) {
            System.out.println("Livro não encontrado.");
            return;
        }
        ItemDoAcervo item = itens.get(0);

        boolean sucesso = minhaBiblioteca.realizarReserva(membro, item);
        if (sucesso) {
            System.out.println("Reserva realizada com sucesso!");
            minhaBiblioteca.salvar();
        } else {
            System.out.println("Não foi possível realizar a reserva, o item já está disponível.");
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

        minhaBiblioteca.login(funcionario);
        System.out.println("Bem-vindo, " + funcionario.getNome() + " (" + funcionario.getPermissao() + ")");

        boolean emMenuFuncionario = true;
        while (emMenuFuncionario) {
            System.out.println("\n=== Menu de Funcionário ===");
            System.out.println("[1] Gerenciar Acervo");
            System.out.println("[2] Gerenciar Pessoas");
            System.out.println("[3] Gerenciar Empréstimos e Reservas");
            System.out.println("[4] Gerar Relatórios");
            System.out.println("[5] Gerenciar Caixa");
            System.out.println("[0] Deslogar");
            System.out.print("Escolha: ");
            int opcao = -1;
            try {
                opcao = sc.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                sc.next();
                continue;
            }
            sc.nextLine();

            switch (opcao) {
                case 1 -> menuGerenciarAcervo();
                case 2 -> menuGerenciarPessoas();
                case 3 -> menuGerenciarEmprestimos();
                case 4 -> menuGerarRelatorios();
                case 5 -> menuGerenciarCaixa();
                case 0 -> {
                    minhaBiblioteca.logout();
                    emMenuFuncionario = false;
                }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private static void menuGerenciarAcervo() {
        boolean emMenuAcervo = true;
        while (emMenuAcervo) {
            System.out.println("\n--- Gerenciar Acervo ---");
            System.out.println("[1] Cadastrar novo livro");
            System.out.println("[2] Adicionar mais cópias de um item");
            System.out.println("[3] Remover item do acervo");
            System.out.println("[4] Editar livro");
            System.out.println("[0] Voltar");
            System.out.print("Escolha: ");
            int opcao = -1;
            try {
                opcao = sc.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                sc.next();
                continue;
            }
            sc.nextLine();

            switch (opcao) {
                case 1 -> cadastrarLivro();
                case 2 -> adicionarCopias();
                case 3 -> removerItem();
                case 4 -> editarLivro();
                case 0 -> emMenuAcervo = false;
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private static void cadastrarLivro() {
        System.out.print("Título: ");
        String titulo = sc.nextLine();
        System.out.print("Nome do autor: ");
        String nomeAutor = sc.nextLine();
        System.out.print("Nacionalidade do autor: ");
        String nacionalidadeAutor = sc.nextLine();
        System.out.print("Ano: ");
        int ano;
        try {
            ano = sc.nextInt();
            sc.nextLine();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Ano inválido. Operação cancelada.");
            sc.nextLine();
            return;
        }
        System.out.print("Setor (LITERATURA_NACIONAL, TECNOLOGIA, etc.): ");
        String setorStr = sc.nextLine();
        System.out.print("Número de páginas: ");
        int paginas;
        try {
            paginas = sc.nextInt();
            sc.nextLine();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Número de páginas inválido. Operação cancelada.");
            sc.nextLine();
            return;
        }
        System.out.print("ISBN: ");
        String isbn = sc.nextLine();
        System.out.print("Quantidade de cópias: "); // Adicionada a entrada de quantidade
        int quantidade;
        try {
            quantidade = sc.nextInt();
            sc.nextLine();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Quantidade inválida. Operação cancelada.");
            sc.nextLine();
            return;
        }

        Autor autor = new Autor(nomeAutor, nacionalidadeAutor);
        EnumSetor setor = EnumSetor.valueOf(setorStr.toUpperCase());
        Livro novoLivro = new Livro(titulo, autor, ano, setor, paginas, isbn, quantidade);
        minhaBiblioteca.cadastrarLivro(novoLivro);
        minhaBiblioteca.salvar();
    }

    private static void adicionarCopias() {
        System.out.print("Título do item: ");
        String titulo = sc.nextLine();
        List<ItemDoAcervo> itens = minhaBiblioteca.buscarItemPorTitulo(titulo);
        if (itens.isEmpty()) {
            System.out.println("Item não encontrado.");
            return;
        }
        ItemDoAcervo item = itens.get(0);
        System.out.print("Quantidade a adicionar: ");
        int quantidade;
        try {
            quantidade = sc.nextInt();
            sc.nextLine();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Quantidade inválida. Operação cancelada.");
            sc.nextLine();
            return;
        }

        try {
            minhaBiblioteca.adicionarMaisCopias(item, quantidade);
            minhaBiblioteca.salvar();
            System.out.println(quantidade + " cópias adicionadas com sucesso!");
        } catch (ItemNaoEncontradoException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static void removerItem() {
        System.out.print("Título do item a ser removido: ");
        String titulo = sc.nextLine();
        List<ItemDoAcervo> itens = minhaBiblioteca.buscarItemPorTitulo(titulo);
        if (itens.isEmpty()) {
            System.out.println("Item não encontrado.");
            return;
        }
        ItemDoAcervo item = itens.get(0);
        minhaBiblioteca.removerItem(item);
        minhaBiblioteca.salvar();
        System.out.println("Item removido com sucesso!");
    }

    private static void editarLivro() {
        System.out.print("Título do livro a ser editado: ");
        String titulo = sc.nextLine();
        List<ItemDoAcervo> itens = minhaBiblioteca.buscarItemPorTitulo(titulo);
        if (itens.isEmpty()) {
            System.out.println("Livro não encontrado.");
            return;
        }
        Livro livro = (Livro) itens.get(0);

        System.out.println("Editando livro: " + livro.getTitulo());
        System.out.println("Digite os novos dados (deixe em branco para manter o atual).");

        System.out.print("Novo título (" + livro.getTitulo() + "): ");
        String novoTitulo = sc.nextLine();
        if (!novoTitulo.isEmpty()) {
            // A classe Livro precisa de um setTitulo
        }

        minhaBiblioteca.salvar();
        System.out.println("Livro editado com sucesso!");
    }

    private static void menuGerenciarPessoas() {
        boolean emMenuPessoas = true;
        while (emMenuPessoas) {
            System.out.println("\n--- Gerenciar Pessoas ---");
            System.out.println("[1] Adicionar novo funcionário");
            System.out.println("[2] Remover membro");
            System.out.println("[3] Remover funcionário");
            System.out.println("[4] Editar membro");
            System.out.println("[5] Editar funcionário");
            System.out.println("[0] Voltar");
            System.out.print("Escolha: ");
            int opcao = -1;
            try {
                opcao = sc.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                sc.next();
                continue;
            }
            sc.nextLine();

            switch (opcao) {
                case 1 -> adicionarFuncionario();
                case 2 -> removerMembro();
                case 3 -> removerFuncionario();
                case 4 -> editarMembro();
                case 5 -> editarFuncionario();
                case 0 -> emMenuPessoas = false;
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private static void adicionarFuncionario() {
        System.out.print("Nome: ");
        String nome = sc.nextLine();
        System.out.print("CPF: ");
        String cpf = sc.nextLine();
        System.out.print("Telefone: ");
        String telefone = sc.nextLine();
        System.out.print("Idade: ");
        int idade;
        try {
            idade = sc.nextInt();
            sc.nextLine();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Idade inválida. Operação cancelada.");
            sc.nextLine();
            return;
        }
        System.out.print("Login: ");
        String login = sc.nextLine();
        System.out.print("Senha: ");
        String senha = sc.nextLine();
        System.out.print("Permissão (ADMINISTRADOR, GERENTE, USUARIO_COMUM): ");
        String permissaoStr = sc.nextLine();

        try {
            Permissao permissao = Permissao.valueOf(permissaoStr.toUpperCase());
            Funcionario novoFuncionario = new Funcionario(nome, cpf, telefone, idade, login, senha, permissao);
            minhaBiblioteca.adicionarFuncionario(novoFuncionario);
            minhaBiblioteca.salvar();
            System.out.println("Funcionário cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Permissão inválida. Operação cancelada.");
        }
    }

    private static void removerMembro() {
        System.out.print("CPF do membro a ser removido: ");
        String cpf = sc.nextLine();
        Membro membro = minhaBiblioteca.buscarMembroPorCPF(cpf);
        if (membro == null) {
            System.out.println("Membro não encontrado.");
            return;
        }
        if (minhaBiblioteca.removerMembro(membro)) {
            minhaBiblioteca.salvar();
            System.out.println("Membro removido com sucesso!");
        } else {
            System.out.println("Erro ao remover membro.");
        }
    }

    private static void removerFuncionario() {
        System.out.print("CPF do funcionário a ser removido: ");
        String cpf = sc.nextLine();
        Funcionario funcionario = minhaBiblioteca.buscarFuncionarioPorCPF(cpf);
        if (funcionario == null) {
            System.out.println("Funcionário não encontrado.");
            return;
        }
        if (minhaBiblioteca.removerFuncionario(funcionario)) {
            minhaBiblioteca.salvar();
            System.out.println("Funcionário removido com sucesso!");
        } else {
            System.out.println("Erro ao remover funcionário.");
        }
    }

    private static void editarMembro() {
        System.out.print("CPF do membro a ser editado: ");
        String cpf = sc.nextLine();
        Membro membro = minhaBiblioteca.buscarMembroPorCPF(cpf);
        if (membro == null) {
            System.out.println("Membro não encontrado.");
            return;
        }

        System.out.println("Editando membro: " + membro.getNome());
        System.out.println("Digite os novos dados (deixe em branco para manter o atual).");

        System.out.print("Novo nome (" + membro.getNome() + "): ");
        String novoNome = sc.nextLine();
        if (!novoNome.isEmpty()) {
            membro.setNome(novoNome);
        }

        System.out.print("Novo telefone (" + membro.getTelefone() + "): ");
        String novoTelefone = sc.nextLine();
        if (!novoTelefone.isEmpty()) {
            membro.setTelefone(novoTelefone);
        }

        System.out.print("Nova idade (" + membro.getIdade() + "): ");
        String novaIdadeStr = sc.nextLine();
        if (!novaIdadeStr.isEmpty()) {
            try {
                membro.setIdade(Integer.parseInt(novaIdadeStr));
            } catch (NumberFormatException e) {
                System.out.println("Idade inválida. Idade não alterada.");
            }
        }

        minhaBiblioteca.salvar();
        System.out.println("Membro editado com sucesso!");
    }

    private static void editarFuncionario() {
        System.out.print("CPF do funcionário a ser editado: ");
        String cpf = sc.nextLine();
        Funcionario funcionario = minhaBiblioteca.buscarFuncionarioPorCPF(cpf);
        if (funcionario == null) {
            System.out.println("Funcionário não encontrado.");
            return;
        }

        System.out.println("Editando funcionário: " + funcionario.getNome());
        System.out.println("Digite os novos dados (deixe em branco para manter o atual).");

        System.out.print("Novo nome (" + funcionario.getNome() + "): ");
        String novoNome = sc.nextLine();
        if (!novoNome.isEmpty()) {
            funcionario.setNome(novoNome);
        }

        System.out.print("Novo telefone (" + funcionario.getTelefone() + "): ");
        String novoTelefone = sc.nextLine();
        if (!novoTelefone.isEmpty()) {
            funcionario.setTelefone(novoTelefone);
        }

        System.out.print("Nova idade (" + funcionario.getIdade() + "): ");
        String novaIdadeStr = sc.nextLine();
        if (!novaIdadeStr.isEmpty()) {
            try {
                funcionario.setIdade(Integer.parseInt(novaIdadeStr));
            } catch (NumberFormatException e) {
                System.out.println("Idade inválida. Idade não alterada.");
            }
        }

        System.out.print("Novo login (" + funcionario.getLogin() + "): ");
        String novoLogin = sc.nextLine();
        if (!novoLogin.isEmpty()) {
            funcionario.setLogin(novoLogin);
        }

        System.out.print("Nova permissão (" + funcionario.getPermissao().name() + "): ");
        String novaPermissaoStr = sc.nextLine();
        if (!novaPermissaoStr.isEmpty()) {
            try {
                funcionario.setPermissao(Permissao.valueOf(novaPermissaoStr.toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.out.println("Permissão inválida. Permissão não alterada.");
            }
        }

        minhaBiblioteca.salvar();
        System.out.println("Funcionário editado com sucesso!");
    }

    private static void menuGerenciarEmprestimos() {
        boolean emMenuEmprestimos = true;
        while (emMenuEmprestimos) {
            System.out.println("\n--- Gerenciar Empréstimos e Reservas ---");
            System.out.println("[1] Realizar devolução");
            System.out.println("[2] Verificar débitos pendentes de membro");
            System.out.println("[0] Voltar");
            System.out.print("Escolha: ");
            int opcao = -1;
            try {
                opcao = sc.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                sc.next();
                continue;
            }
            sc.nextLine();

            switch (opcao) {
                case 1 -> realizarDevolucao();
                case 2 -> verificarDebitos();
                case 0 -> emMenuEmprestimos = false;
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private static void realizarDevolucao() {
        System.out.print("CPF do membro: ");
        String cpf = sc.nextLine();
        Membro membro = minhaBiblioteca.buscarMembroPorCPF(cpf);
        if (membro == null) {
            System.out.println("Membro não encontrado.");
            return;
        }

        System.out.print("Título do livro a ser devolvido: ");
        String titulo = sc.nextLine();
        Optional<Emprestimo> emprestimoOpt = minhaBiblioteca.buscarEmprestimo(membro, titulo);

        if (emprestimoOpt.isPresent()) {
            minhaBiblioteca.realizarDevolucao(emprestimoOpt.get());
            minhaBiblioteca.salvar();
            System.out.println("Devolução realizada com sucesso!");
        } else {
            System.out.println("Empréstimo não encontrado para este membro e título.");
        }
    }

    private static void verificarDebitos() {
        System.out.print("CPF do membro: ");
        String cpf = sc.nextLine();
        Membro membro = minhaBiblioteca.buscarMembroPorCPF(cpf);
        if (membro == null) {
            System.out.println("Membro não encontrado.");
            return;
        }

        List<Multa> debitos = minhaBiblioteca.debitosPendentes(membro);
        if (debitos.isEmpty()) {
            System.out.println("Este membro não possui débitos pendentes.");
        } else {
            System.out.println("Débitos pendentes para " + membro.getNome() + ":");
            debitos.forEach(multa -> System.out.println(" - Multa de R$" + multa.getValor() + " por empréstimo de " + multa.getEmprestimo().getItemDoAcervo().getTitulo()));
        }
    }

    private static void menuGerenciarCaixa() {
        boolean emMenuCaixa = true;
        while (emMenuCaixa) {
            System.out.println("\n--- Gerenciar Caixa ---");
            System.out.println("[1] Abrir Caixa");
            System.out.println("[2] Receber Multa");
            System.out.println("[3] Fechar Caixa");
            System.out.println("[0] Voltar");
            System.out.print("Escolha: ");
            int opcao = -1;
            try {
                opcao = sc.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                sc.next();
                continue;
            }
            sc.nextLine();
            switch (opcao) {
                case 1 -> abrirCaixa();
                case 2 -> receberMulta();
                case 3 -> fecharCaixa();
                case 0 -> emMenuCaixa = false;
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private static void abrirCaixa() {
        System.out.print("Digite o saldo inicial (ex: 100,50): ");
        try {
            BigDecimal saldoInicial = sc.nextBigDecimal();
            sc.nextLine();
            minhaBiblioteca.abrirCaixa(saldoInicial);
            minhaBiblioteca.salvar();
        } catch (java.util.InputMismatchException e) {
            System.out.println("ERRO: Valor inválido. Por favor, use a vírgula como separador decimal.");
            sc.nextLine();
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    private static void receberMulta() {
        System.out.print("CPF do membro que irá pagar a multa: ");
        String cpf = sc.nextLine();
        Membro membro = minhaBiblioteca.buscarMembroPorCPF(cpf);
        if (membro == null) {
            System.out.println("Membro não encontrado.");
            return;
        }

        List<Multa> debitos = minhaBiblioteca.debitosPendentes(membro);
        if (debitos.isEmpty()) {
            System.out.println("Este membro não possui multas pendentes.");
            return;
        }

        System.out.println("Multas pendentes para " + membro.getNome() + ":");
        for (int i = 0; i < debitos.size(); i++) {
            System.out.println("[" + (i + 1) + "] Valor: R$" + debitos.get(i).getValor() + " - Livro: " + debitos.get(i).getEmprestimo().getItemDoAcervo().getTitulo());
        }
        System.out.print("Escolha o número da multa a ser paga: ");
        int opcao = sc.nextInt();
        sc.nextLine();

        if (opcao > 0 && opcao <= debitos.size()) {
            Multa multa = debitos.get(opcao - 1);
            minhaBiblioteca.pagarMulta(multa);
            minhaBiblioteca.salvar();
            System.out.println("Multa de R$" + multa.getValor() + " paga com sucesso.");
        } else {
            System.out.println("Opção inválida.");
        }
    }

    private static void fecharCaixa() {
        minhaBiblioteca.fecharCaixa();
        minhaBiblioteca.salvar();
    }

    private static void menuGerarRelatorios() {
        boolean emMenuRelatorios = true;
        while (emMenuRelatorios) {
            System.out.println("\n--- Gerar Relatórios ---");
            System.out.println("[1] Relatório de Atrasos");
            System.out.println("[2] Relatório de Itens Mais Emprestados");
            System.out.println("[3] Relatório de Reservas Ativas");
            System.out.println("[0] Voltar");
            System.out.print("Escolha: ");
            int opcao = -1;
            try {
                opcao = sc.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                sc.next();
                continue;
            }
            sc.nextLine();

            switch (opcao) {
                case 1 -> gerarRelatorioAtrasos();
                case 2 -> gerarRelatorioMaisEmprestados();
                case 3 -> gerarRelatorioReservasAtivas();
                case 0 -> emMenuRelatorios = false;
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private static void gerarRelatorioAtrasos() {
        List<Emprestimo> atrasos = minhaBiblioteca.gerarRelatorioDeAtrasos();
        if (atrasos.isEmpty()) {
            System.out.println("Nenhum empréstimo em atraso no momento.");
        } else {
            System.out.println("=== Relatório de Empréstimos em Atraso ===");
            atrasos.forEach(e -> System.out.println(" - " + e.getItemDoAcervo().getTitulo() + " (Membro: " + e.getMembro().getNome() + ")"));
        }
    }

    private static void gerarRelatorioMaisEmprestados() {
        Map<ItemDoAcervo, Long> maisEmprestados = minhaBiblioteca.gerarRelatorioDeItensMaisEmprestados();
        if (maisEmprestados.isEmpty()) {
            System.out.println("Ainda não há empréstimos registrados.");
        } else {
            System.out.println("=== Relatório de Itens Mais Emprestados ===");
            maisEmprestados.entrySet().stream()
                .sorted(Map.Entry.<ItemDoAcervo, Long>comparingByValue().reversed())
                .forEach(entry -> System.out.println(" - " + entry.getKey().getTitulo() + ": " + entry.getValue() + " vezes"));
        }
    }

    private static void gerarRelatorioReservasAtivas() {
        List<Reserva> reservas = minhaBiblioteca.gerarRelatorioDeReservasAtivas();
        if (reservas.isEmpty()) {
            System.out.println("Nenhuma reserva ativa no momento.");
        } else {
            System.out.println("=== Relatório de Reservas Ativas ===");
            reservas.forEach(r -> System.out.println(" - " + r.getItemdoarcevo().getTitulo() + " (Membro: " + r.getMembro().getNome() + ")"));
        }
    }
}
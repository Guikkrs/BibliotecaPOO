package biblioteca.UI;

import biblioteca.fachada.Fachada;
import biblioteca.negocios.entidade.ItemDoAcervo;
import biblioteca.negocios.entidade.Livro;
import biblioteca.negocios.entidade.Membro;
import biblioteca.negocios.excecoes.acervo.ItemNaoDisponivelException;
import biblioteca.negocios.excecoes.emprestimo.LimiteDeEmprestimosAtingidoException;
import biblioteca.negocios.excecoes.emprestimo.MembroComDebitoException;
import biblioteca.negocios.excecoes.reserva.ItemDisponivelException;
import biblioteca.negocios.excecoes.reserva.ReservaDuplicadaException;
import biblioteca.negocios.enums.EnumSetor;

import java.util.List;
import java.util.Arrays;
import java.util.Optional; // Importe a classe Optional

public class TelaBuscarAcervo {

    public static void exibir(Membro membro) {
        boolean executando = true;
        while (executando) {
            UIUtils.exibirCabecalho("BUSCAR NO ACERVO");
            System.out.println("[1] Buscar por Título");
            System.out.println("[2] Buscar por Autor");
            System.out.println("[3] Listar Todos os Livros");
            System.out.println("[4] Buscar por Setor");
            System.out.println("[0] Voltar ao Menu");

            int opcao = UIUtils.lerOpcao();

            switch (opcao) {
                case 1 -> buscarPorTitulo(membro);
                case 2 -> buscarPorAutor(membro);
                case 3 -> listarTodos(membro);
                case 4 -> buscarPorSetor(membro);
                case 0 -> executando = false;
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void buscarPorTitulo(Membro membro) {
        String titulo = UIUtils.lerString("Digite o título do item: ");
        ItemDoAcervo item = Fachada.getInstance().buscarItemPorTitulo(titulo);
        if (item != null) {
            System.out.println("Item encontrado: " + item);
            oferecerOpcoesItem(membro, item);
        } else {
            System.out.println("Nenhum item encontrado com este título.");
        }
    }

    private static void buscarPorAutor(Membro membro) {
        String autor = UIUtils.lerString("Digite o nome do autor: ");
        List<Livro> livros = Fachada.getInstance().buscarLivroPorAutor(autor);
        if (!livros.isEmpty()) {
            System.out.println("Livros encontrados para o autor '" + autor + "':");
            livros.forEach(livro -> System.out.println("ID: " + livro.getId() + " | " + livro));
            // --- ALTERAÇÃO AQUI ---
            // Após listar, oferece a opção de selecionar um item.
            selecionarItemDaLista(membro, livros);
        } else {
            System.out.println("Nenhum livro encontrado para este autor.");
        }
    }

    private static void listarTodos(Membro membro) {
        UIUtils.exibirCabecalho("TODOS OS LIVROS DO ACERVO");
        List<Livro> livros = Fachada.getInstance().listarTodosLivros();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
        } else {
            livros.forEach(livro -> System.out.println("ID: " + livro.getId() + " | " + livro));
        }
    }

    private static void buscarPorSetor(Membro membro) {
        String setorStr = UIUtils.lerString("Digite o setor: ");
        try {
            EnumSetor setor = EnumSetor.valueOf(setorStr.trim().toUpperCase());
            List<ItemDoAcervo> itens = Fachada.getInstance().buscarPorSetor(setor);

            if (itens.isEmpty()) {
                System.out.println("Nenhum item encontrado para o setor '" + setor + "'.");
            } else {
                System.out.println("Itens encontrados para o setor '" + setor + "':");
                itens.forEach(item -> System.out.println("ID: " + item.getId() + " | " + item));
                // --- ALTERAÇÃO AQUI ---
                // Após listar, oferece a opção de selecionar um item.
                selecionarItemDaLista(membro, itens);
            }

        } catch (IllegalArgumentException e) {
            System.out.println("ERRO: Setor inválido. Os setores disponíveis são: " + Arrays.toString(EnumSetor.values()));
        }
    }

    /**
     * ---- NOVO MÉTODO REUTILIZÁVEL ----
     * Pede ao usuário para digitar um ID da lista de resultados e, se válido,
     * chama o método para oferecer as opções de empréstimo/reserva.
     * @param membro O membro logado
     * @param itens A lista de itens exibida para o usuário
     */
    private static void selecionarItemDaLista(Membro membro, List<? extends ItemDoAcervo> itens) {
        System.out.println(); // Linha em branco para espaçamento
        int idSelecionado = UIUtils.lerInt("Digite o ID do item que deseja selecionar (ou 0 para voltar): ");

        if (idSelecionado == 0) {
            return; // Usuário escolheu voltar
        }

        // Tenta encontrar o item na lista usando o ID fornecido
        Optional<? extends ItemDoAcervo> itemOptional = itens.stream()
                .filter(item -> item.getId() == idSelecionado)
                .findFirst();

        if (itemOptional.isPresent()) {
            // Se encontrou o item, oferece as opções para ele
            oferecerOpcoesItem(membro, itemOptional.get());
        } else {
            // Se o ID não corresponde a nenhum item da lista
            System.out.println("ERRO: ID inválido.");
        }
    }

    private static void oferecerOpcoesItem(Membro membro, ItemDoAcervo item) {
        System.out.println("\nO que deseja fazer?");
        System.out.println("[1] Realizar Empréstimo");
        System.out.println("[2] Realizar Reserva");
        System.out.println("[0] Voltar");

        int opcao = UIUtils.lerOpcao();
        switch (opcao) {
            case 1:
                try {
                    Fachada.getInstance().realizarEmprestimo(membro, item);
                    System.out.println("SUCESSO: Empréstimo realizado!");
                } catch (MembroComDebitoException | ItemNaoDisponivelException | LimiteDeEmprestimosAtingidoException e) {
                    System.out.println("ERRO: " + e.getMessage());
                }
                break;
            case 2:
                try {
                    Fachada.getInstance().realizarReserva(membro, item);
                    System.out.println("SUCESSO: Reserva realizada!");
                } catch (ItemDisponivelException | ReservaDuplicadaException e) {
                    System.out.println("ERRO: " + e.getMessage());
                }
                break;
            default:
                break; // Volta para o menu de busca
        }
    }
}
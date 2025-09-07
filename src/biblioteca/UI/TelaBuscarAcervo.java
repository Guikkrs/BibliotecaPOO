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

import java.util.List;

public class TelaBuscarAcervo {

    public static void exibir(Membro membro) {
        boolean executando = true;
        while (executando) {
            UIUtils.exibirCabecalho("BUSCAR NO ACERVO");
            System.out.println("[1] Buscar por Título");
            System.out.println("[2] Buscar por Autor");
            System.out.println("[3] Listar Todos os Livros");
            System.out.println("[0] Voltar ao Menu");

            int opcao = UIUtils.lerOpcao();

            switch (opcao) {
                case 1 -> buscarPorTitulo(membro);
                case 2 -> buscarPorAutor(membro);
                case 3 -> listarTodos(membro);
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
            livros.forEach(System.out::println);
            // Poderia-se adicionar uma lógica para escolher um dos livros e operar sobre ele
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
            livros.forEach(System.out::println);
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

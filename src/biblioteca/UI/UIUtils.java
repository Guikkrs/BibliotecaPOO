package biblioteca.UI;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UIUtils {

    private static final Scanner sc = new Scanner(System.in);

    public static void exibirCabecalho(String titulo) {
        System.out.println("\n--- " + titulo + " ---");
    }

    public static int lerOpcao() {
        try {
            int opcao = sc.nextInt();
            sc.nextLine(); // Limpa o buffer
            return opcao;
        } catch (InputMismatchException e) {
            sc.nextLine(); // Limpa o buffer em caso de erro
            return -1; // Retorna um valor inválido
        }
    }

    public static String lerString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }

    public static int lerInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int valor = sc.nextInt();
                sc.nextLine();
                return valor;
            } catch (InputMismatchException e) {
                System.out.println("ERRO: Por favor, digite um número inteiro válido.");
                sc.nextLine();
            }
        }
    }

    public static BigDecimal lerBigDecimal(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                BigDecimal valor = sc.nextBigDecimal();
                sc.nextLine();
                return valor;
            } catch (InputMismatchException e) {
                System.out.println("ERRO: Por favor, digite um valor numérico válido (ex: 100.50).");
                sc.nextLine();
            }
        }
    }
}
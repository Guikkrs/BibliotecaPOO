package biblioteca.dados;

import biblioteca.dados.persistencia.*;
import biblioteca.dados.repositorio.*;

/**
 * Classe central que gere qual a implementação de persistência que será usada.
 * Permite trocar entre a persistência por serialização (.dat) e por CSV
 * de forma intercambiável para toda a aplicação.
 */
public class GerenciadorPersistencia {

    // #################### PONTO CENTRAL DE CONTROLE ####################
    // Mude esta variável para true para usar CSV, ou false para usar Serialização (.dat)
    private static final boolean USAR_CSV = true;
    // ###################################################################


    // --- INSTÂNCIAS MESTRAS (SINGLETONS) ---
    // Repositórios dos quais outros dependem são criados apenas uma vez e reutilizados.
    private static final IRepositorioAutor REPO_AUTOR = criarRepositorioAutor();
    private static final IRepositorioMembro REPO_MEMBRO = criarRepositorioMembro();
    private static final IRepositorioItemDoAcervo REPO_ACERVO = criarRepositorioItemDoAcervo();
    private static final IRepositorioEmprestimo REPO_EMPRESTIMO = criarRepositorioEmprestimo();


    // --- MÉTODOS DE CRIAÇÃO (FÁBRICA) ---

    private static IRepositorioAutor criarRepositorioAutor() {
        if (USAR_CSV) {
            return new RepositorioAutorCSV();
        } else {
            return new RepositorioAutorPersistencia();
        }
    }

    private static IRepositorioMembro criarRepositorioMembro() {
        if (USAR_CSV) {
            return new RepositorioMembroCSV();
        } else {
            return new RepositorioMembroPersistencia();
        }
    }

    private static IRepositorioItemDoAcervo criarRepositorioItemDoAcervo() {
        if (USAR_CSV) {
            return new RepositorioItemDoAcervoCSV(REPO_AUTOR);
        } else {
            return new RepositorioItemDoAcervoPersistencia();
        }
    }

    private static IRepositorioEmprestimo criarRepositorioEmprestimo() {
        if (USAR_CSV) {
            return new RepositorioEmprestimoCSV(REPO_MEMBRO, REPO_ACERVO);
        } else {
            return new RepositorioEmprestimoPersistencia();
        }
    }

    private static IRepositorioMulta criarRepositorioMulta() {
        if (USAR_CSV) {
            return new RepositorioMultaCSV(REPO_MEMBRO, REPO_EMPRESTIMO);
        } else {
            return new RepositorioMultaPersistencia();
        }
    }

    private static IRepositorioReserva criarRepositorioReserva() {
        if (USAR_CSV) {
            return new RepositorioReservaCSV(REPO_MEMBRO, REPO_ACERVO);
        } else {
            return new RepositorioReservaPersistencia();
        }
    }

    private static IRepositorioCaixa criarRepositorioCaixa() {
        if (USAR_CSV) {
            return new RepositorioCaixaCSV();
        } else {
            return new RepositorioCaixaPersistencia();
        }
    }

    private static IRepositorioSetor criarRepositorioSetor() {
        if (USAR_CSV) {
            return new RepositorioSetorCSV();
        } else {
            return new RepositorioSetorPersistencia();
        }
    }

    private static IRepositorioFuncionario criarRepositorioFuncionario() {
        if (USAR_CSV) {
            return new RepositorioFuncionarioCSV();
        } else {
            return new RepositorioFuncionarioPersistencia();
        }
    }


    // --- MÉTODOS "GET" PÚBLICOS (Usados pela Fachada) ---

    public static IRepositorioAutor getRepositorioAutor() {
        return REPO_AUTOR;
    }

    public static IRepositorioMembro getRepositorioMembro() {
        return REPO_MEMBRO;
    }

    public static IRepositorioItemDoAcervo getRepositorioItemDoAcervo() {
        return REPO_ACERVO;
    }

    public static IRepositorioEmprestimo getRepositorioEmprestimo() {
        return REPO_EMPRESTIMO;
    }

    public static IRepositorioMulta getRepositorioMulta() {
        return criarRepositorioMulta();
    }

    public static IRepositorioReserva getRepositorioReserva() {
        return criarRepositorioReserva();
    }

    public static IRepositorioCaixa getRepositorioCaixa() {
        return criarRepositorioCaixa();
    }

    public static IRepositorioSetor getRepositorioSetor() {
        return criarRepositorioSetor();
    }

    public static IRepositorioFuncionario getRepositorioFuncionario() {
        return criarRepositorioFuncionario();
    }
}
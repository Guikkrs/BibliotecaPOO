package biblioteca.Negocio;

import java.sql.Date;

public class Emprestimo {

    private Membro membro;
    private ItemDoAcervo item;
    private Date dataEmprestimo;
    private Date dataDevolucao;

    public Emprestimo(Membro membro, ItemDoAcervo item, Date dataEmprestimo, Date dataDevolucao) {
        this.membro = membro;
        this.item = item;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = dataDevolucao;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = dataDevolucao;
    }

}

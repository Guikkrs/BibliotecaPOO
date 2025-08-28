package biblioteca.Negocio;

import biblioteca.Enum.StatusEmprestimo;
import java.time.LocalDate;

public class Emprestimo {
    
    private Membro membro;
    private ItemDoAcervo itemDoAcervo;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate devolucaoRealizada;
    private StatusEmprestimo status;

    public Emprestimo(Membro membro, ItemDoAcervo itemDoAcervo, LocalDate dataEmprestimo) {
        this.membro = membro;
        this.itemDoAcervo = itemDoAcervo;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataEmprestimo.plusDays(14); // Exemplo de 14 dias
        this.status = StatusEmprestimo.ATIVO;
    }

    public Membro getMembro() {
        return membro;
    }

    public ItemDoAcervo getItemDoAcervo() {
        return itemDoAcervo;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDate getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public LocalDate getDevolucaoRealizada() {
        return devolucaoRealizada;
    }
    
    public StatusEmprestimo getStatus() {
        return status;
    }
    
    public void setStatus(StatusEmprestimo status) {
        this.status = status;
    }

    public void setDevolucaoRealizada(LocalDate devolucaoRealizada) {
        this.devolucaoRealizada = devolucaoRealizada;
    }
}
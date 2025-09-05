package biblioteca.negocios;

import java.time.LocalDate;

import biblioteca.negocios.enums.StatusEmprestimo;

public class Emprestimo implements java.io.Serializable {
    
    // Adicionar um identificador único para o empréstimo
    private final String id;
    
    private Membro membro;
    private ItemDoAcervo itemDoAcervo;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate devolucaoRealizada;
    private StatusEmprestimo status;

    public Emprestimo(String id, Membro membro, ItemDoAcervo itemDoAcervo, LocalDate dataEmprestimo) {
        this.id = id;
        this.membro = membro;
        this.itemDoAcervo = itemDoAcervo;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucaoPrevista = dataEmprestimo.plusDays(14); // Exemplo de 14 dias
        this.status = StatusEmprestimo.ATIVO;
    }

    // Método para obter o ID do empréstimo
    public String getId() {
        return this.id;
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

    public boolean estaAtrasado() {
        if (this.devolucaoRealizada != null) {
            return this.devolucaoRealizada.isAfter(
                this.dataDevolucaoPrevista
            );
        }
        return LocalDate.now().isAfter(this.dataDevolucaoPrevista);
    }

    public void finalizarEmprestimo(LocalDate dataDevolucao) {
        setDevolucaoRealizada(dataDevolucao);
        setStatus(StatusEmprestimo.DEVOLVIDO);
    }
}
package biblioteca.Negocio;

import biblioteca.Enum.Permissao;
import biblioteca.Negocio.Pessoa;

public class Membro extends Pessoa {

    private int emprestimosAtivos;
    private String historicoEmprestimos;
    private String login;
    private String senhaHash;
    private Permissao permissao;

    public Membro(String nome, String cpf, String telefone, int idade, String login, String senhaHash, Permissao permissao) {
        super(nome, cpf, telefone, idade);
        this.login = login;
        this.senhaHash = senhaHash;
        this.permissao = permissao;
        this.emprestimosAtivos = 0;
        this.historicoEmprestimos = "";
    }

    public int getEmprestimosAtivos() {
        return this.emprestimosAtivos;
    }

    public String getHistoricoEmprestimos() {
        return this.historicoEmprestimos;
    }

    public String getLogin() {
        return this.login;
    }

    public String getSenhaHash() {
        return this.senhaHash;
    }

    public Permissao getPermissao() {
        return this.permissao;
    }


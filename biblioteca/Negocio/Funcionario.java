package biblioteca.Negocio;

import Enum.Permissao;

public class Funcionario extends Pessoa {

    private String login;
    private String senhaHash;
    private Permissao permissao;

    public Funcionario(String nome, String cpf, String telefone, int idade, String login, String senhaHash, Permissao permissao) {
        super(nome, cpf, telefone, idade);
        this.login = login;
        this.senhaHash = senhaHash;
        this.permissao = permissao;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenhaHash() {
        return this.senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public Permissao getPermissao() {
        return this.permissao;
    }

    public void setPermissao(Permissao permissao) {
        this.permissao = permissao;
    }
}

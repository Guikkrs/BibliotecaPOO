package biblioteca.negocios.entidade;

import biblioteca.negocios.enums.Permissao;

public class Membro extends Pessoa implements java.io.Serializable {

    private String login;
    private String senhaHash;
    private Permissao permissao;

    public Membro(String nome, String cpf, String telefone, int idade, String login, String senhaHash, Permissao permissao) {
        super(nome, cpf, telefone, idade);
        this.login = login;
        this.senhaHash = senhaHash;
        this.permissao = permissao;
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

    @Override
    public String toString() {
        return "Membro [Nome: " + getNome() + ", CPF: " + getCpf() + ", Login: " + this.login + "]";
    }
}

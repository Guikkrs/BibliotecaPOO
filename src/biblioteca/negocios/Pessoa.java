package biblioteca.negocios;

import java.util.Objects;

public class Pessoa implements java.io.Serializable {

    private String nome;
    private String cpf;
    private String telefone;
    private int idade;

    public Pessoa(String nome, String cpf, String telefone, int idade) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.idade = idade;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        if (nome != null && !nome.trim().isEmpty()) {
            this.nome = nome;
        } else {
            throw new IllegalArgumentException("Nome não pode ser nulo ou vazio.");
        }
    }

    public String getCpf() {
        return this.cpf;
    }

    public void setCpf(String cpf) {
        if (cpf != null && cpf.length() == 11) {
            this.cpf = cpf;
        } else {
            throw new IllegalArgumentException("Cpf invalido.");
        }
    }

    public String getTelefone() {
        return this.telefone;
    }

    public void setTelefone(String telefone) {
        if (telefone != null && !telefone.trim().isEmpty()) {
            this.telefone = telefone;
        } else {
            throw new IllegalArgumentException("Numero telefonico nao pode estar vazio");
        }
    }

    public int getIdade() {
        return this.idade;
    }

    public void setIdade(int idade) {
        if (idade > 0) {
            this.idade = idade;
        } else {
            throw new IllegalArgumentException("idade nao pode estar vazio");
        }
    }

    @Override
    public String toString() {
        return "Pessoa{"
                + "nome= '" + nome + '\''
                + ", idade= " + idade
                + "telefone= " + telefone
                + "cpf= " + cpf
                + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Pessoa outraPessoa = (Pessoa) obj;

        return Objects.equals(this.cpf, outraPessoa.cpf);
    }

}

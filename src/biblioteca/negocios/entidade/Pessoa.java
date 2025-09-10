package biblioteca.negocios.entidade;

import java.util.Objects;

public abstract class Pessoa implements java.io.Serializable {

    private String nome;
    private String cpf;
    private String telefone;
    private int idade;
    private int id;

    public Pessoa(String nome, String cpf, String telefone, int idade) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.idade = idade;
    }

    // GETTERS E SETTERS (sem alterações)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        // Você pode adicionar uma validação de CPF mais robusta aqui se desejar
        if (cpf != null && !cpf.trim().isEmpty()) {
            this.cpf = cpf;
        } else {
            throw new IllegalArgumentException("CPF inválido.");
        }
    }

    public String getTelefone() {
        return this.telefone;
    }

    public void setTelefone(String telefone) {
        if (telefone != null && !telefone.trim().isEmpty()) {
            this.telefone = telefone;
        } else {
            throw new IllegalArgumentException("Número telefônico não pode estar vazio");
        }
    }

    public int getIdade() {
        return this.idade;
    }

    public void setIdade(int idade) {
        if (idade > 0) {
            this.idade = idade;
        } else {
            throw new IllegalArgumentException("Idade não pode ser nula ou vazia");
        }
    }

    @Override
    public String toString() {
        return "Pessoa{"
                + "id=" + id
                + ", nome='" + nome + '\''
                + ", cpf='" + cpf + '\''
                + '}';
    }

    /**
     * MODIFICAÇÃO: A igualdade agora é baseada estritamente no ID.
     * Isso garante que cada registro da persistência é único.
     * Se o ID for 0, significa que o objeto ainda não foi persistido,
     * então a comparação é feita pela referência de memória (comportamento padrão).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pessoa pessoa = (Pessoa) o;
        // Se o ID for 0, o objeto não foi persistido. Se ambos tiverem ID 0, são diferentes a menos que sejam o mesmo objeto.
        if (this.id == 0 || pessoa.id == 0) return false;
        return id == pessoa.id;
    }

    /**
     * MODIFICAÇÃO: O hashCode agora é baseado no ID, para ser consistente com o equals.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
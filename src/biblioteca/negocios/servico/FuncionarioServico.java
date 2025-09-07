package biblioteca.negocios.servico;

import biblioteca.dados.repositorio.IRepositorioFuncionario;
import biblioteca.negocios.entidade.Funcionario;
import biblioteca.negocios.enums.Permissao;
import biblioteca.negocios.excecoes.login.CredenciaisInvalidasException;
import biblioteca.negocios.excecoes.login.PermissaoInsuficienteException;
import biblioteca.negocios.excecoes.pessoa.CpfJaExistenteException;
import biblioteca.negocios.excecoes.validacao.ValidacaoException;

import java.util.List;

public class FuncionarioServico {

    private final IRepositorioFuncionario repositorioFuncionario;
    private Funcionario funcionarioLogado;

    public FuncionarioServico(IRepositorioFuncionario repositorioFuncionario) {
        this.repositorioFuncionario = repositorioFuncionario;
        this.funcionarioLogado = null;
    }

    public void login(String login, String senha) throws CredenciaisInvalidasException {
        Funcionario funcionario = repositorioFuncionario.autenticar(login, senha);
        if (funcionario != null) {
            this.funcionarioLogado = funcionario;
        } else {
            throw new CredenciaisInvalidasException();
        }
    }

    public void logout() {
        this.funcionarioLogado = null;
    }

    public Funcionario getFuncionarioLogado() {
        return this.funcionarioLogado;
    }

    public void validarPermissao(Permissao permissaoMinima) throws PermissaoInsuficienteException, ValidacaoException {
        // 1. Verifica se existe alguém logado no sistema.
        if (funcionarioLogado == null) {
            throw new ValidacaoException("Nenhum funcionário logado. Operação não permitida.");
        }

        // 2. Compara o nível de permissão do funcionário com o nível mínimo exigido.
        // O método ordinal() retorna a posição do enum (ADMINISTRADOR=0, GERENTE=1, etc.).
        // Se o ordinal do funcionário for MAIOR, significa que sua permissão é MENOR.
        if (funcionarioLogado.getPermissao().ordinal() > permissaoMinima.ordinal()) {
            throw new PermissaoInsuficienteException();
        }
    }

    // MÉTODO MOVIDO PARA O NÍVEL DA CLASSE
    public void cadastrarFuncionario(Funcionario funcionario) throws CpfJaExistenteException, ValidacaoException {
        if (repositorioFuncionario.buscarPorCpf(funcionario.getCpf()) != null) {
            throw new CpfJaExistenteException(funcionario.getCpf());
        }
        repositorioFuncionario.adicionar(funcionario);
    }

    // MÉTODO MOVIDO PARA O NÍVEL DA CLASSE
    public List<Funcionario> listarTodosFuncionarios() {
        return repositorioFuncionario.listarTodos();
    }

}
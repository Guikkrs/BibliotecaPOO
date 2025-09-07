package biblioteca.negocios.servico;

import biblioteca.dados.repositorio.IRepositorioEmprestimo;
import biblioteca.dados.repositorio.IRepositorioMembro;
import biblioteca.negocios.entidade.Membro;
import biblioteca.negocios.enums.StatusEmprestimo;
import biblioteca.negocios.excecoes.login.CredenciaisInvalidasException; // IMPORT ADICIONADO
import biblioteca.negocios.excecoes.pessoa.CpfJaExistenteException;
import biblioteca.negocios.excecoes.pessoa.MembroComPendenciasException;
import biblioteca.negocios.excecoes.pessoa.PessoaNaoEncontradaException;
import biblioteca.negocios.excecoes.validacao.ValidacaoException;

import java.util.List;

public class MembroServico {

    private final IRepositorioMembro repositorioMembro;
    private final IRepositorioEmprestimo repositorioEmprestimo;
    private Membro membroLogado;

    public MembroServico(IRepositorioMembro repositorioMembro, IRepositorioEmprestimo repositorioEmprestimo) {
        this.repositorioMembro = repositorioMembro;
        this.repositorioEmprestimo = repositorioEmprestimo;
        this.membroLogado = null;
    }

    // MÉTODOS DE SESSÃO ADICIONADOS
    public void login(String login, String senha) throws CredenciaisInvalidasException {
        Membro membro = repositorioMembro.autenticar(login, senha);
        if (membro != null) {
            this.membroLogado = membro;
        } else {
            throw new CredenciaisInvalidasException();
        }
    }

    public void logout() {
        this.membroLogado = null;
    }

    public Membro getMembroLogado() {
        return this.membroLogado;
    }

    public void cadastrarMembro(Membro membro) throws CpfJaExistenteException, ValidacaoException {
        if (membro.getNome() == null || membro.getNome().trim().isEmpty()) {
            throw new ValidacaoException("O nome do membro não pode ser vazio.");
        }
        if (repositorioMembro.buscarPorCpf(membro.getCpf()) != null) {
            throw new CpfJaExistenteException(membro.getCpf());
        }
        repositorioMembro.adicionar(membro);
    }

    public void removerMembro(String cpf) throws PessoaNaoEncontradaException, MembroComPendenciasException {
        Membro membro = repositorioMembro.buscarPorCpf(cpf);
        if (membro == null) {
            throw new PessoaNaoEncontradaException(cpf);
        }

        boolean temPendencias = repositorioEmprestimo.listarTodos().stream()
                .anyMatch(e -> e.getMembro().equals(membro) && e.getStatus() == StatusEmprestimo.ATIVO);

        if (temPendencias) {
            throw new MembroComPendenciasException(membro.getNome());
        }

        repositorioMembro.remover(membro);
    }

    public Membro buscarMembroPorCpf(String cpf) {
        return repositorioMembro.buscarPorCpf(cpf);
    }

    public List<Membro> listarTodosMembros() {
        return repositorioMembro.listarTodos();
    }
}
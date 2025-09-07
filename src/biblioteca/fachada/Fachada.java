package biblioteca.fachada;

import biblioteca.dados.persistencia.*;
import biblioteca.dados.repositorio.*;
import biblioteca.negocios.entidade.*;
import biblioteca.negocios.enums.Permissao;
import biblioteca.negocios.excecoes.acervo.ItemComPendenciasException;
import biblioteca.negocios.excecoes.acervo.ItemNaoDisponivelException;
import biblioteca.negocios.excecoes.caixa.CaixaAbertoException;
import biblioteca.negocios.excecoes.caixa.CaixaFechadoException;
import biblioteca.negocios.excecoes.emprestimo.EmprestimoNaoEncontradoException;
import biblioteca.negocios.excecoes.emprestimo.LimiteDeEmprestimosAtingidoException;
import biblioteca.negocios.excecoes.emprestimo.MembroComDebitoException;
import biblioteca.negocios.excecoes.itemDoAcervo.ItemNaoEncontradoException;
import biblioteca.negocios.excecoes.login.CredenciaisInvalidasException;
import biblioteca.negocios.excecoes.login.PermissaoInsuficienteException;
import biblioteca.negocios.excecoes.pessoa.CpfJaExistenteException;
import biblioteca.negocios.excecoes.pessoa.MembroComPendenciasException;
import biblioteca.negocios.excecoes.pessoa.PessoaNaoEncontradaException;
import biblioteca.negocios.excecoes.reserva.ItemDisponivelException;
import biblioteca.negocios.excecoes.reserva.ReservaDuplicadaException;
import biblioteca.negocios.excecoes.validacao.ValidacaoException;
import biblioteca.negocios.servico.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class Fachada {

    private static Fachada instancia;

    // ... (declaração dos serviços) ...
    private final FuncionarioServico funcionarioServico;
    private final MembroServico membroServico;
    private final AcervoServico acervoServico;
    private final EmprestimoServico emprestimoServico;
    private final CaixaServico caixaServico;
    private final ReservaServico reservaServico;
    private final RelatorioServico relatorioServico;

    private Fachada() {
        // ... (inicialização dos repositórios e serviços) ...
        IRepositorioFuncionario repositorioFuncionario = new RepositorioFuncionarioPersistencia();
        IRepositorioMembro repositorioMembro = new RepositorioMembroPersistencia();
        IRepositorioItemDoAcervo repositorioAcervo = new RepositorioItemDoAcervoPersistencia();
        IRepositorioEmprestimo repositorioEmprestimo = new RepositorioEmprestimoPersistencia();
        IRepositorioMulta repositorioMulta = new RepositorioMultaPersistencia();
        IRepositorioCaixa repositorioCaixa = new RepositorioCaixaPersistencia();
        IRepositorioReserva repositorioReserva = new RepositorioReservaPersistencia();

        this.funcionarioServico = new FuncionarioServico(repositorioFuncionario);
        this.membroServico = new MembroServico(repositorioMembro, repositorioEmprestimo);
        this.acervoServico = new AcervoServico(repositorioAcervo, repositorioEmprestimo);
        this.caixaServico = new CaixaServico(repositorioCaixa, repositorioMulta);
        this.emprestimoServico = new EmprestimoServico(repositorioEmprestimo, repositorioMulta, repositorioAcervo);
        this.reservaServico = new ReservaServico(repositorioReserva, repositorioAcervo);
        this.relatorioServico = new RelatorioServico(repositorioEmprestimo, repositorioReserva);
    }

    public static synchronized Fachada getInstance() {
        if (instancia == null) {
            instancia = new Fachada();
        }
        return instancia;
    }

    // ... (métodos de login/sessão) ...
    public void login(String login, String senha) throws CredenciaisInvalidasException {
        funcionarioServico.login(login, senha);
    }

    public void logout() {
        funcionarioServico.logout();
    }

    public Funcionario getFuncionarioLogado() {
        return funcionarioServico.getFuncionarioLogado();
    }

    public void loginMembro(String login, String senha) throws CredenciaisInvalidasException {
        membroServico.login(login, senha);
    }

    public void logoutMembro() {
        membroServico.logout();
    }

    public Membro getMembroLogado() {
        return membroServico.getMembroLogado();
    }

    // ========== SERVIÇO DE ACERVO ==========
    public void cadastrarLivro(Livro livro) throws ValidacaoException, PermissaoInsuficienteException {
        funcionarioServico.validarPermissao(Permissao.USUARIO_COMUM);
        acervoServico.cadastrarLivro(livro);
    }

    // CORREÇÃO: Adicionada ValidacaoException
    public void adicionarCopias(int idItem, int quantidade) throws ItemNaoEncontradoException, ValidacaoException, PermissaoInsuficienteException {
        funcionarioServico.validarPermissao(Permissao.GERENTE);
        acervoServico.adicionarCopias(idItem, quantidade);
    }

    // CORREÇÃO: Adicionada ValidacaoException
    public void removerItem(int idItem) throws ItemNaoEncontradoException, ItemComPendenciasException, PermissaoInsuficienteException, ValidacaoException {
        funcionarioServico.validarPermissao(Permissao.ADMINISTRADOR);
        acervoServico.removerItem(idItem);
    }

    // ... (outros métodos do acervo) ...
    public ItemDoAcervo buscarItemPorTitulo(String titulo) {
        return acervoServico.buscarItemPorTitulo(titulo);
    }

    public List<Livro> buscarLivroPorAutor(String nomeAutor) {
        return acervoServico.buscarLivroPorAutor(nomeAutor);
    }

    public List<Livro> listarTodosLivros() {
        return acervoServico.listarTodosLivros();
    }

    // ========== SERVIÇO DE PESSOAS (MEMBRO E FUNCIONÁRIO) ==========
    public void cadastrarMembro(Membro membro) throws CpfJaExistenteException, ValidacaoException {
        membroServico.cadastrarMembro(membro);
    }

    // CORREÇÃO: Adicionada ValidacaoException
    public void cadastrarFuncionario(Funcionario funcionario) throws CpfJaExistenteException, ValidacaoException, PermissaoInsuficienteException {
        funcionarioServico.validarPermissao(Permissao.ADMINISTRADOR);
        funcionarioServico.cadastrarFuncionario(funcionario);
    }

    // ... (outros métodos de pessoas) ...
    public Membro buscarMembroPorCpf(String cpf) {
        return membroServico.buscarMembroPorCpf(cpf);
    }

    public void removerMembro(String cpf) throws PessoaNaoEncontradaException, MembroComPendenciasException {
        membroServico.removerMembro(cpf);
    }

    public List<Membro> listarTodosMembros() {
        return membroServico.listarTodosMembros();
    }

    public List<Funcionario> listarTodosFuncionarios() {
        return funcionarioServico.listarTodosFuncionarios();
    }

    // ... (outros métodos de empréstimo e reserva) ...
    public void realizarEmprestimo(Membro membro, ItemDoAcervo item) throws MembroComDebitoException, ItemNaoDisponivelException, LimiteDeEmprestimosAtingidoException {
        emprestimoServico.realizarEmprestimo(membro, item);
    }

    public void realizarDevolucao(Emprestimo emprestimo) {
        emprestimoServico.realizarDevolucao(emprestimo);
    }

    public List<Multa> debitosPendentes(Membro membro) {
        return emprestimoServico.debitosPendentes(membro);
    }

    public Emprestimo buscarEmprestimoAtivo(Membro membro, ItemDoAcervo item) throws EmprestimoNaoEncontradoException {
        return emprestimoServico.buscarEmprestimoAtivo(membro, item);
    }

    public List<Emprestimo> listarEmprestimosAtivosPorMembro(Membro membro) {
        return emprestimoServico.listarEmprestimosAtivosPorMembro(membro);
    }

    public void realizarReserva(Membro membro, ItemDoAcervo item) throws ItemDisponivelException, ReservaDuplicadaException {
        reservaServico.realizarReserva(membro, item);
    }

    public List<Reserva> listarReservasAtivasPorMembro(Membro membro) {
        return reservaServico.listarReservasAtivasPorMembro(membro);
    }

    // ========== SERVIÇO DE CAIXA ==========
    // CORREÇÃO: Adicionada ValidacaoException
    public void abrirCaixa(BigDecimal saldoInicial) throws CaixaAbertoException, PermissaoInsuficienteException, ValidacaoException {
        funcionarioServico.validarPermissao(Permissao.GERENTE);
        caixaServico.abrirCaixa(saldoInicial);
    }

    // CORREÇÃO: Adicionada ValidacaoException
    public void pagarMulta(Multa multa) throws CaixaFechadoException, PermissaoInsuficienteException, ValidacaoException {
        funcionarioServico.validarPermissao(Permissao.USUARIO_COMUM);
        caixaServico.registrarPagamentoDeMulta(multa);
    }

    // CORREÇÃO: Adicionada ValidacaoException
    public Caixa fecharCaixa() throws CaixaFechadoException, PermissaoInsuficienteException, ValidacaoException {
        funcionarioServico.validarPermissao(Permissao.GERENTE);
        return caixaServico.fecharCaixa();
    }

    // ... (outros métodos de caixa e relatório) ...
    public Caixa getCaixaAberto() {
        return caixaServico.getCaixaAberto();
    }

    public List<Emprestimo> gerarRelatorioDeAtrasos() {
        return relatorioServico.gerarRelatorioDeAtrasos();
    }

    public Map<ItemDoAcervo, Long> gerarRelatorioDeItensMaisEmprestados() {
        return relatorioServico.gerarRelatorioDeItensMaisEmprestados();
    }

    public List<Reserva> gerarRelatorioDeReservasAtivas() {
        return relatorioServico.gerarRelatorioDeReservasAtivas();
    }
}
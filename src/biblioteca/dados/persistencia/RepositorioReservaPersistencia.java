package biblioteca.dados.persistencia;

import biblioteca.dados.repositorio.IRepositorioReserva;
import biblioteca.negocios.entidade.Reserva;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RepositorioReservaPersistencia implements IRepositorioReserva {

    private ArrayList<Reserva> reservas;
    private int proximoId = 1;
    private static final String NOME_ARQUIVO = "reservas.dat";

    public RepositorioReservaPersistencia() {
        this.reservas = new ArrayList<>();
        carregarDoArquivo();
    }

    @Override
    public void adicionar(Reserva reserva) {
        reserva.setId(this.proximoId++);
        this.reservas.add(reserva);
        salvarNoArquivo();
    }

    @Override
    public void atualizar(Reserva reserva) {
        for (int i = 0; i < reservas.size(); i++) {
            if (reservas.get(i).getId() == reserva.getId()) {
                reservas.set(i, reserva);
                salvarNoArquivo();
                return;
            }
        }
    }

    @Override
    public void remover(Reserva reserva) {
        if (this.reservas.remove(reserva)) {
            salvarNoArquivo();
        }
    }

    @Override
    public Reserva buscarPorId(int id) {
        for (Reserva r : this.reservas) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

    @Override
    public List<Reserva> listarTodos() {
        return new ArrayList<>(this.reservas);
    }

    private void salvarNoArquivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(NOME_ARQUIVO)))) {
            oos.writeObject(this.reservas);
        } catch (IOException e) {
            System.err.println("Erro ao salvar reservas: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void carregarDoArquivo() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) {
            this.reservas = new ArrayList<>();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(NOME_ARQUIVO)))) {
            this.reservas = (ArrayList<Reserva>) ois.readObject();
            atualizarProximoId();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar reservas: " + e.getMessage());
            this.reservas = new ArrayList<>();
        }
    }

    private void atualizarProximoId() {
        if (this.reservas.isEmpty()) {
            this.proximoId = 1;
        } else {
            int maiorId = 0;
            for (Reserva r : this.reservas) {
                if (r.getId() > maiorId) {
                    maiorId = r.getId();
                }
            }
            this.proximoId = maiorId + 1;
        }
    }
}
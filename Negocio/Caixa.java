package Negocio;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import Enum.StatusCaixa;

public class Caixa {

    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;
    private BigDecimal saldoInicial;
    private BigDecimal saldoFinal;
    private StatusCaixa status;

    public Caixa(BigDecimal saldoInicial){
        this.saldoInicial = saldoInicial;
        this.status = StatusCaixa.FECHADO;
    }

    public void abrirCaixa(){

    }

    public void fecharCaixa(){

    }

    public void registrarEntrada(){
        
    }


    
}

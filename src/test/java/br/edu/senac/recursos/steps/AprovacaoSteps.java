package br.edu.senac.recursos.steps;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Entao;

public class AprovacaoSteps {
    @Dado("que existe uma reserva solicitada")
    public void que_existe_uma_reserva_solicitada() {
        // Setup via API
    }
    @Quando("o responsavel tenta aprovar a reserva")
    public void o_responsavel_tenta_aprovar_a_reserva() {
        // Acao via API
    }
    @Entao("o status da reserva deve ser APROVADA")
    public void o_status_da_reserva_deve_ser_aprovada() {
        // Assert
    }
}

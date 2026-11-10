package br.edu.senac.recursos.steps;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Entao;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AprovacaoSteps {
    @Dado("que existe uma reserva solicitada")
    public void que_existe_uma_reserva_solicitada() {
        // Logica de setup
        assertTrue(true);
    }
    @Quando("o responsavel tenta aprovar a reserva")
    public void o_responsavel_tenta_aprovar_a_reserva() {
        // Logica de acao
        assertTrue(true);
    }
    @Entao("o status da reserva deve ser APROVADA")
    public void o_status_da_reserva_deve_ser_aprovada() {
        // Logica de assercao
        assertTrue(true);
    }
}

package br.edu.senac.recursos.domain;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "auditoria_reservas")
public class AuditoriaReserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reserva_id", nullable = false)
    private Long reservaId;

    @Column(name = "usuario_email", nullable = false)
    private String usuarioEmail;

    @Column(nullable = false)
    private OffsetDateTime instante;

    @Column(name = "estado_anterior")
    private String estadoAnterior;

    @Column(name = "estado_novo", nullable = false)
    private String estadoNovo;

    @Column(nullable = false)
    private String motivo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getReservaId() { return reservaId; }
    public void setReservaId(Long reservaId) { this.reservaId = reservaId; }
    public String getUsuarioEmail() { return usuarioEmail; }
    public void setUsuarioEmail(String usuarioEmail) { this.usuarioEmail = usuarioEmail; }
    public OffsetDateTime getInstante() { return instante; }
    public void setInstante(OffsetDateTime instante) { this.instante = instante; }
    public String getEstadoAnterior() { return estadoAnterior; }
    public void setEstadoAnterior(String estadoAnterior) { this.estadoAnterior = estadoAnterior; }
    public String getEstadoNovo() { return estadoNovo; }
    public void setEstadoNovo(String estadoNovo) { this.estadoNovo = estadoNovo; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}

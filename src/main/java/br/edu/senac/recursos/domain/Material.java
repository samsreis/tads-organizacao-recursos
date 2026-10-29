package br.edu.senac.recursos.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "materiais")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private Boolean emManutencao = false;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    public Boolean getEmManutencao() { return emManutencao; }
    public void setEmManutencao(Boolean emManutencao) { this.emManutencao = emManutencao; }
}

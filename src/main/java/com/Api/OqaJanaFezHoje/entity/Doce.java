package com.Api.OqaJanaFezHoje.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "doces")
public class Doce {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_doce")
    private Integer idDoce;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(name = "quantidade_estoque", columnDefinition = "INT DEFAULT 0")
    private Integer quantidadeEstoque = 0;

    // Getters e Setters
    public Integer getIdDoce() { return idDoce; }
    public void setIdDoce(Integer idDoce) { this.idDoce = idDoce; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }
    public Integer getQuantidadeEstoque() { return quantidadeEstoque; }
    public void setQuantidadeEstoque(Integer quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }
}

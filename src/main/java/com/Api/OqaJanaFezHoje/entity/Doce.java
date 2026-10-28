package com.Api.OqaJanaFezHoje.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "doces")
public class Doce {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_doce")
    private Long idDoce;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;


    // Getters e Setters
    public Long getIdDoce() {
        return idDoce;
    }

    public void setIdDoce(Long idDoce) {
        this.idDoce = idDoce;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

}

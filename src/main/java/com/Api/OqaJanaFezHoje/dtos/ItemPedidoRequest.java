package com.Api.OqaJanaFezHoje.dtos;

public record ItemPedidoRequest(
        String nomeDoce,
        Integer quantidade,
        Double precoUnitario)
{
}

package com.Api.OqaJanaFezHoje.dtos;

public record PedidoRequest(
        Long idCliente,
        Iterable<ItemPedidoRequest> itens
)
{

}

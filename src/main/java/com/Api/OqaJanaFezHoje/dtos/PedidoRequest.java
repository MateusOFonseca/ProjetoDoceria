package com.Api.OqaJanaFezHoje.dtos;

public record PedidoRequest(
        Integer idCliente,
        Iterable<ItemPedidoRequest> itens
)
{

}

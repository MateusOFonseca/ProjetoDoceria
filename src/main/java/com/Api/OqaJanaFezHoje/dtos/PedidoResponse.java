package com.Api.OqaJanaFezHoje.dtos;

import com.Api.OqaJanaFezHoje.entity.Pedido;

public record PedidoResponse(
        Long idPedido,
        Long idCliente,
        String nomeCliente,
        String dataPedido,
        String status,
        Iterable<ItemPedidoRequest> itens,
        Double valorTotal   ){ }

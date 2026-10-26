package com.Api.OqaJanaFezHoje.dtos;

import com.Api.OqaJanaFezHoje.entity.Pedido;
import com.Api.OqaJanaFezHoje.entity.enums.StatusPedido;

public record PedidoResponse(
        Long idPedido,
        Long idCliente,
        String nomeCliente,
        String dataPedido,
        StatusPedido status,
        Iterable<ItemPedidoRequest> itens,
        Double valorTotal   ){ }

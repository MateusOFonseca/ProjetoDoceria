package com.Api.OqaJanaFezHoje.mapper;

import com.Api.OqaJanaFezHoje.dtos.ItemPedidoRequest;
import com.Api.OqaJanaFezHoje.dtos.PedidoResponse;
import com.Api.OqaJanaFezHoje.entity.Pedido;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PedidoMapper {

    public PedidoResponse toResponse(Pedido pedido) {
        if (pedido == null) {
            return null;
        }

        List<ItemPedidoRequest> itensDto = pedido.getItens().stream()
                .map(item -> new ItemPedidoRequest(
                        item.getDoce().getNome(),
                        item.getQuantidade()
                ))
                .collect(Collectors.toList());

        return new PedidoResponse(
                Long.valueOf(pedido.getIdPedido()),
                Long.valueOf(pedido.getCliente().getIdCliente()),
                pedido.getCliente().getNome(),
                pedido.getDataPedido() != null ? pedido.getDataPedido().toString() : null,
                pedido.getStatus(),
                itensDto,
                pedido.getValorTotal() != null ? pedido.getValorTotal().doubleValue() : 0.0
        );
    }
}

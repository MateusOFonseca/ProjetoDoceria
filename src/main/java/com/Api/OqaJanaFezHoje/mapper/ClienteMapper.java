package com.Api.OqaJanaFezHoje.mapper;

import com.Api.OqaJanaFezHoje.dtos.ClienteResumoResponse;
import com.Api.OqaJanaFezHoje.entity.Cliente;
import com.Api.OqaJanaFezHoje.entity.Pedido;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ClienteMapper {

    public ClienteResumoResponse toResumoResponse(Cliente cliente) {
        List<Pedido> pedidos = cliente.getPedidos() == null
                ? List.of()
                : cliente.getPedidos();

        BigDecimal valorTotalPedidos = pedidos.stream()
                .map(Pedido::getValorTotal)
                .filter(valor -> valor != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        LocalDateTime ultimoPedido = pedidos.stream()
                .map(Pedido::getDataPedido)
                .filter(data -> data != null)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        return new ClienteResumoResponse(
                cliente.getIdCliente().longValue(),
                cliente.getNome(),
                cliente.getTelefone(),
                cliente.getRegiao(),
                cliente.getDataCadastro(),
                (long) pedidos.size(),
                valorTotalPedidos,
                ultimoPedido
        );
    }
}
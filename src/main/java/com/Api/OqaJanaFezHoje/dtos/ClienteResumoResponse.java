package com.Api.OqaJanaFezHoje.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ClienteResumoResponse(
        Long idCliente,
        String nome,
        String telefone,
        String regiao,
        LocalDateTime dataCadastro,
        Long totalPedidos,
        BigDecimal valorTotalPedidos,
        LocalDateTime ultimoPedido
) {
}
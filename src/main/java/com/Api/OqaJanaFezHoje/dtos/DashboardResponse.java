package com.Api.OqaJanaFezHoje.dtos;

import java.math.BigDecimal;
import java.util.List;

public record DashboardResponse(
        int ano,
        int mes,
        long totalPedidos,
        BigDecimal faturamentoTotal,
        List<String> clientesRecorrentes,
        List<DoceVendido> docesVendidos
) {
    public record DoceVendido(
            String nome,
            long quantidadeVendida
    ) {
    }
}
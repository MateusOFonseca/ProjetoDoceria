package com.Api.OqaJanaFezHoje.service;

import com.Api.OqaJanaFezHoje.dtos.DashboardResponse;
import com.Api.OqaJanaFezHoje.entity.enums.StatusPedido;
import com.Api.OqaJanaFezHoje.repository.DashboardRepository;
import com.Api.OqaJanaFezHoje.repository.DoceVendido;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
public class DashboardService {

    private final DashboardRepository dashboardRepository;

    public DashboardService(DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    @Transactional(readOnly = true)
    public DashboardResponse obterDashboard(int ano, int mes) {
        if (ano < 1 || mes < 1 || mes > 12) {
            throw new DateTimeException(
                    "Ano e mês devem representar um período válido."
            );
        }
        YearMonth periodo = YearMonth.of(ano, mes);

        LocalDateTime inicio = periodo
                .atDay(1)
                .atStartOfDay();

        LocalDateTime fim = periodo
                .plusMonths(1)
                .atDay(1)
                .atStartOfDay();

        StatusPedido status = StatusPedido.CONCLUIDO;

        long totalPedidos = dashboardRepository.contarPedidosDoPeriodo(
                status,
                inicio,
                fim
        );

        BigDecimal faturamentoTotal =
                dashboardRepository.somarFaturamentoDoPeriodo(
                        status,
                        inicio,
                        fim
                );

        List<String> clientesRecorrentes =
                dashboardRepository.buscarNomesClientesRecorrentes(
                        status,
                        inicio,
                        fim
                );

        List<DashboardResponse.DoceVendido> docesVendidos =
                dashboardRepository.buscarTodosOsDocesVendidos(
                                status,
                                inicio,
                                fim
                        )
                        .stream()
                        .map(this::converterDoceVendido)
                        .toList();

        return new DashboardResponse(
                ano,
                mes,
                totalPedidos,
                faturamentoTotal,
                clientesRecorrentes,
                docesVendidos
        );
    }

    private DashboardResponse.DoceVendido converterDoceVendido(
            DoceVendido projection
    ) {
        return new DashboardResponse.DoceVendido(
                projection.getNomeDoce(),
                projection.getQuantidadeVendida()
        );
    }
}
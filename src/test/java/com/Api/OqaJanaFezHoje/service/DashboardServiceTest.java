package com.Api.OqaJanaFezHoje.service;

import com.Api.OqaJanaFezHoje.dtos.DashboardResponse;
import com.Api.OqaJanaFezHoje.entity.enums.StatusPedido;
import com.Api.OqaJanaFezHoje.repository.DashboardRepository;
import com.Api.OqaJanaFezHoje.repository.DoceVendido;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private DashboardRepository dashboardRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @Test
    void deveObterDashboardComDadosDoPeriodo() {
        DoceVendido doceVendido = mock(DoceVendido.class);

        when(dashboardRepository.contarPedidosDoPeriodo(
                eq(StatusPedido.CONCLUIDO),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(3L);

        when(dashboardRepository.somarFaturamentoDoPeriodo(
                eq(StatusPedido.CONCLUIDO),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(new BigDecimal("250.00"));

        when(dashboardRepository.buscarNomesClientesRecorrentes(
                eq(StatusPedido.CONCLUIDO),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(List.of("Mateus"));

        when(doceVendido.getNomeDoce()).thenReturn("Brownie");
        when(doceVendido.getQuantidadeVendida()).thenReturn(4L);

        when(dashboardRepository.buscarTodosOsDocesVendidos(
                eq(StatusPedido.CONCLUIDO),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(List.of(doceVendido));

        DashboardResponse resultado =
                dashboardService.obterDashboard(2026, 10);

        assertEquals(2026, resultado.ano());
        assertEquals(10, resultado.mes());
        assertEquals(3L, resultado.totalPedidos());
        assertEquals(new BigDecimal("250.00"), resultado.faturamentoTotal());
        assertEquals(List.of("Mateus"), resultado.clientesRecorrentes());
        assertEquals(1, resultado.docesVendidos().size());
        assertEquals("Brownie", resultado.docesVendidos().get(0).nome());
        assertEquals(4L, resultado.docesVendidos().get(0).quantidadeVendida());
    }

    @Test
    void deveAceitarMesDeJaneiro() {
        configurarRepositorioSemResultados();

        DashboardResponse resultado =
                dashboardService.obterDashboard(2026, 1);

        assertEquals(1, resultado.mes());
    }

    @Test
    void deveAceitarMesDeDezembro() {
        configurarRepositorioSemResultados();

        DashboardResponse resultado =
                dashboardService.obterDashboard(2026, 12);

        assertEquals(12, resultado.mes());
    }

    @Test
    void deveRejeitarMesMenorQueUm() {
        DateTimeException excecao = assertThrows(
                DateTimeException.class,
                () -> dashboardService.obterDashboard(2026, 0)
        );

        assertEquals(
                "Ano e mês devem representar um período válido.",
                excecao.getMessage()
        );

        verifyNoInteractions(dashboardRepository);
    }

    @Test
    void deveRejeitarMesMaiorQueDoze() {
        assertThrows(
                DateTimeException.class,
                () -> dashboardService.obterDashboard(2026, 13)
        );

        verifyNoInteractions(dashboardRepository);
    }

    @Test
    void deveRejeitarAnoMenorQueUm() {
        assertThrows(
                DateTimeException.class,
                () -> dashboardService.obterDashboard(0, 10)
        );

        verifyNoInteractions(dashboardRepository);
    }

    private void configurarRepositorioSemResultados() {
        when(dashboardRepository.contarPedidosDoPeriodo(
                eq(StatusPedido.CONCLUIDO),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(0L);

        when(dashboardRepository.somarFaturamentoDoPeriodo(
                eq(StatusPedido.CONCLUIDO),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(BigDecimal.ZERO);

        when(dashboardRepository.buscarNomesClientesRecorrentes(
                eq(StatusPedido.CONCLUIDO),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(List.of());

        when(dashboardRepository.buscarTodosOsDocesVendidos(
                eq(StatusPedido.CONCLUIDO),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(List.of());
    }
}
package com.Api.OqaJanaFezHoje.controller;

import com.Api.OqaJanaFezHoje.dtos.PedidoResponse;
import com.Api.OqaJanaFezHoje.entity.enums.StatusPedido;
import com.Api.OqaJanaFezHoje.service.PedidoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PedidoController.class)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PedidoService pedidoService;

    @Test
    void deveListarPedidosPendentes() throws Exception {
        given(pedidoService.buscarPedidosPendentes())
                .willReturn(List.of());

        mockMvc.perform(get("/pedidos/pendentes"))
                .andExpect(status().isOk());

        verify(pedidoService).buscarPedidosPendentes();
    }

    @Test
    void deveListarPedidosConcluidos() throws Exception {
        given(pedidoService.buscarPedidosConcluidos())
                .willReturn(List.of());

        mockMvc.perform(get("/pedidos/concluidos"))
                .andExpect(status().isOk());

        verify(pedidoService).buscarPedidosConcluidos();
    }

    @Test
    void deveRetornarPedidoPorId() throws Exception {
        PedidoResponse response = new PedidoResponse(
                1L,
                2L,
                "Mateus",
                "2026-10-20T20:22:15",
                StatusPedido.PENDENTE,
                List.of(),
                140.0
        );

        given(pedidoService.buscarPedidoPorId(1L))
                .willReturn(Optional.of(response));

        mockMvc.perform(get("/pedidos/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar404QuandoPedidoNaoExistir() throws Exception {
        given(pedidoService.buscarPedidoPorId(999L))
                .willReturn(Optional.empty());

        mockMvc.perform(get("/pedidos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveConcluirPedido() throws Exception {
        PedidoResponse response = new PedidoResponse(
                1L,
                2L,
                "Mateus",
                "2026-10-20T20:22:15",
                StatusPedido.CONCLUIDO,
                List.of(),
                140.0
        );

        given(pedidoService.concluirPedido(1L))
                .willReturn(Optional.of(response));

        mockMvc.perform(patch("/pedidos/1/concluir"))
                .andExpect(status().isOk());

        verify(pedidoService).concluirPedido(1L);
    }

    @Test
    void deveRetornar404AoConcluirPedidoInexistente() throws Exception {
        given(pedidoService.concluirPedido(999L))
                .willReturn(Optional.empty());

        mockMvc.perform(patch("/pedidos/999/concluir"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveDeletarPedido() throws Exception {
        mockMvc.perform(delete("/pedidos/1"))
                .andExpect(status().isNoContent());

        verify(pedidoService).deletarPedido(1L);
    }

    @Test
    void deveListarTodosOsPedidos() throws Exception {
        given(pedidoService.listarPedidos())
                .willReturn(List.of());

        mockMvc.perform(get("/pedidos"))
                .andExpect(status().isOk());
    }

    @Test
    void deveBuscarPedidosPorCliente() throws Exception {
        given(pedidoService.buscarPedidosPorCliente(2L))
                .willReturn(List.of());

        mockMvc.perform(
                        get("/pedidos/buscar-cliente")
                                .param("idCliente", "2")
                )
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar400ParaIdDeClienteInvalido() throws Exception {
        mockMvc.perform(
                        get("/pedidos/buscar-cliente")
                                .param("idCliente", "abc")
                )
                .andExpect(status().isBadRequest());
    }
}
package com.Api.OqaJanaFezHoje.controller;

import com.Api.OqaJanaFezHoje.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.BDDMockito.given;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    @Test
    void deveListarClientes() throws Exception {
        given(clienteService.listarCliente())
                .willReturn(List.of());

        mockMvc.perform(get("/cliente"))
                .andExpect(status().isOk());
    }

    @Test
    void deveBuscarClientePorNome() throws Exception {
        given(clienteService.buscarPorNome("Mateus"))
                .willReturn(List.of());

        mockMvc.perform(
                        get("/cliente/buscar")
                                .param("nome", "Mateus")
                )
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornarListaVaziaParaNomeEmBranco() throws Exception {
        given(clienteService.buscarPorNome(""))
                .willReturn(List.of());

        mockMvc.perform(
                        get("/cliente/buscar")
                                .param("nome", "")
                )
                .andExpect(status().isOk());
    }

    @Test
    void deveBuscarClientePorTelefone() throws Exception {
        given(clienteService.buscarClientePeloTelefone("11999999999"))
                .willReturn(List.of());

        mockMvc.perform(
                        get("/cliente/buscar-telefone")
                                .param("telefone", "11999999999")
                )
                .andExpect(status().isOk());
    }

    @Test
    void deveBuscarClientePorRegiao() throws Exception {
        given(clienteService.buscarClientePelaRegiao("Osasco"))
                .willReturn(List.of());

        mockMvc.perform(
                        get("/cliente/buscar-regiao")
                                .param("regiao", "Osasco")
                )
                .andExpect(status().isOk());
    }
}
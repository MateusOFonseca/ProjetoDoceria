package com.Api.OqaJanaFezHoje.controller;

import com.Api.OqaJanaFezHoje.service.DoceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;


import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DoceController.class)
class DoceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DoceService doceService;

    @Test
    void deveListarDoces() throws Exception {
        given(doceService.listarDoces())
                .willReturn(org.springframework.http.ResponseEntity.ok(List.of()));

        mockMvc.perform(get("/doces"))
                .andExpect(status().isOk());
    }

    @Test
    void deveBuscarDocesPorNome() throws Exception {
        given(doceService.buscarDocesPorNome("Brownie"))
                .willReturn(org.springframework.http.ResponseEntity.ok(List.of()));

        mockMvc.perform(
                        get("/doces/buscar")
                                .param("nome", "Brownie")
                )
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar400QuandoIdDoceForInvalido() throws Exception {
        mockMvc.perform(get("/doces/abc"))
                .andExpect(status().isBadRequest());
    }
}
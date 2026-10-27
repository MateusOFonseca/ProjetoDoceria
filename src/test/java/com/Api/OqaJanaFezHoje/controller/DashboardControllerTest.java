package com.Api.OqaJanaFezHoje.controller;

import com.Api.OqaJanaFezHoje.dtos.DashboardResponse;
import com.Api.OqaJanaFezHoje.service.DashboardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DashboardController.class)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DashboardService dashboardService;

    @Test
    void deveRetornarDashboard() throws Exception {
        DashboardResponse response = new DashboardResponse(
                2026,
                10,
                10,
                BigDecimal.valueOf(1400),
                List.of("Mateus"),
                List.of()
        );

        given(dashboardService.obterDashboard(2026, 10))
                .willReturn(response);

        mockMvc.perform(
                        get("/dashboard")
                                .param("ano", "2026")
                                .param("mes", "10")
                )
                .andExpect(status().isOk());
    }


    @Test
    void deveRetornar400ParaMesInvalido() throws Exception {
        given(dashboardService.obterDashboard(2026, 13))
                .willThrow(new DateTimeException("Mês inválido."));

        mockMvc.perform(
                        get("/dashboard")
                                .param("ano", "2026")
                                .param("mes", "13")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400QuandoAnoNaoForNumerico() throws Exception {
        mockMvc.perform(
                        get("/dashboard")
                                .param("ano", "abc")
                                .param("mes", "10")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400QuandoMesNaoForInformado() throws Exception {
        mockMvc.perform(
                        get("/dashboard")
                                .param("ano", "2026")
                )
                .andExpect(status().isBadRequest());
    }
}
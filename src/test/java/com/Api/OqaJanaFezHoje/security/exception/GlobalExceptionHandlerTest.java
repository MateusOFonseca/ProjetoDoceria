package com.Api.OqaJanaFezHoje.security.exception;


import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void deveTratarRecursoNaoEncontrado() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI())
                .thenReturn("/pedidos/999");

        var response = handler.handleNotFound(
                new ResourceNotFoundException("Pedido não encontrado."),
                request
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(
                "Pedido não encontrado.",
                response.getBody().message()
        );
        assertEquals(
                "/pedidos/999",
                response.getBody().path()
        );
    }

    @Test
    void deveTratarErroDeNegocio() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getRequestURI())
                .thenReturn("/pedidos/1/concluir");

        var response = handler.handleBusiness(
                new BusinessException("Operação inválida."),
                request
        );

        assertEquals(
                HttpStatus.UNPROCESSABLE_ENTITY,
                response.getStatusCode()
        );
    }
}
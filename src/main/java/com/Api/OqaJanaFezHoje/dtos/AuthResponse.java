package com.Api.OqaJanaFezHoje.dtos;

public record AuthResponse(
        String token,
        String tipo,
        long expiraEmSegundos,
        UsuarioResponse usuario
) {
}
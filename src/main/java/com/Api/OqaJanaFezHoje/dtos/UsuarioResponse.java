package com.Api.OqaJanaFezHoje.dtos;

public record UsuarioResponse(
        Long idUsuario,
        String nome,
        String email,
        String perfil
) {
}
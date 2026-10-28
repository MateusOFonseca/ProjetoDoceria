package com.Api.OqaJanaFezHoje.service;

import com.Api.OqaJanaFezHoje.dtos.AuthResponse;
import com.Api.OqaJanaFezHoje.dtos.LoginRequest;
import com.Api.OqaJanaFezHoje.dtos.UsuarioResponse;
import com.Api.OqaJanaFezHoje.entity.Usuario;

import com.Api.OqaJanaFezHoje.repository.UsuarioRepository;
import com.Api.OqaJanaFezHoje.security.JwtService;
import com.Api.OqaJanaFezHoje.security.exception.BusinessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String email = request.email().trim().toLowerCase();

        Usuario usuario = usuarioRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new BusinessException(
                                "E-mail ou senha inválidos."
                        )
                );

        if (!passwordEncoder.matches(
                request.senha(),
                usuario.getSenhaHash()
        )) {
            throw new BusinessException(
                    "E-mail ou senha inválidos."
            );
        }

        String token = jwtService.gerarToken(
                usuario.getIdUsuario(),
                usuario.getEmail(),
                usuario.getPerfil()
        );

        return new AuthResponse(
                token,
                "Bearer",
                jwtService.getExpirationSeconds(),
                toResponse(usuario)
        );
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getIdUsuario(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil()
        );
    }
}
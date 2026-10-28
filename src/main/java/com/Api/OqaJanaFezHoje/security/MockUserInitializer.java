package com.Api.OqaJanaFezHoje.security;

import com.Api.OqaJanaFezHoje.entity.Usuario;
import com.Api.OqaJanaFezHoje.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class MockUserInitializer {

    @Bean
    CommandLineRunner criarUsuarioInicial(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.mock-user.name:Admin}") String nome,
            @Value("${app.mock-user.email:admin@oqajanafezhoje.com}") String email,
            @Value("${app.mock-user.password}") String senha
    ) {
        return args -> {
            if (usuarioRepository.existsByEmailIgnoreCase(email)) {
                return;
            }

            Usuario usuario = new Usuario();
            usuario.setNome(nome);
            usuario.setEmail(email.toLowerCase());
            usuario.setSenhaHash(passwordEncoder.encode(senha));
            usuario.setPerfil("ADMIN");

            usuarioRepository.save(usuario);
        };
    }
}
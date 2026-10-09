package com.Api.OqaJanaFezHoje.repository;

import com.Api.OqaJanaFezHoje.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
    List<Cliente> findByTelefoneContaining(String telefone);
    List<Cliente> findByRegiaoContainingIgnoreCase(String regiao);
}

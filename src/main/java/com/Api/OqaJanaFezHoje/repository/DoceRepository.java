package com.Api.OqaJanaFezHoje.repository;

import com.Api.OqaJanaFezHoje.entity.Doce;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoceRepository extends JpaRepository<Doce, Long> {
    Optional<Doce> findByNomeEqualsIgnoreCase(String NomeDoce);
}

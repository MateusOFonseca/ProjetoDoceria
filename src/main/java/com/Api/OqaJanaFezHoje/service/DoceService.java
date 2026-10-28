package com.Api.OqaJanaFezHoje.service;

import com.Api.OqaJanaFezHoje.entity.Doce;
import com.Api.OqaJanaFezHoje.repository.DoceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DoceService {

    @Autowired
    private DoceRepository doceRepository;

    public Doce salvarDoce(Doce doce) {
        return doceRepository.save(doce);
    }

    @Transactional
    public Optional<Doce> atualizarDoce(Long id, Doce doceAtualizado) {
        return doceRepository.findById(id)
                .map(doce -> {
                    if (doceAtualizado.getNome() != null && !doceAtualizado.getNome().trim().isEmpty()) {
                        doce.setNome(doceAtualizado.getNome());
                    }
                    if (doceAtualizado.getPreco() != null) {
                        doce.setPreco(doceAtualizado.getPreco());
                    }
                    if (doceAtualizado.getDescricao() != null && !doceAtualizado.getDescricao().trim().isEmpty()) {
                        doce.setDescricao(doceAtualizado.getDescricao());
                    }

                    return doceRepository.save(doce);
                });
    }

    public ResponseEntity<Void> deletarDoce(Long id) {
        return doceRepository.findById(id)
                .map(doce -> {
                    doceRepository.delete(doce);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Iterable<Doce>> listarDoces() {
        return ResponseEntity.ok(doceRepository.findAll());
    }

    public ResponseEntity<Iterable<Doce>> buscarDocesPorNome(String nome) {
        Iterable<Doce> doces = doceRepository.findAll().stream()
                .filter(doce -> doce.getNome().toLowerCase().contains(nome.toLowerCase()))
                .toList();
        return ResponseEntity.ok(doces);
    }

    public Optional<Doce> buscarDocePorId(Long id) {
        return doceRepository.findById(id);
    }
}

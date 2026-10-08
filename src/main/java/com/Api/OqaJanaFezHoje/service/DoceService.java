package com.Api.OqaJanaFezHoje.service;

import com.Api.OqaJanaFezHoje.entity.Doce;
import com.Api.OqaJanaFezHoje.repository.DoceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DoceService {

    @Autowired
    private DoceRepository doceRepository;

    public Doce salvarDoce(Doce doce) {
        return doceRepository.save(doce);
    }
    public ResponseEntity<Doce> atualizarDoce(Long id, Doce doceAtualizado) {
        return doceRepository.findById(id)
                .map(doce -> {
                    doce.setNome(doceAtualizado.getNome());
                    doce.setPreco(doceAtualizado.getPreco());
                    Doce doceSalvo = doceRepository.save(doce);
                    return ResponseEntity.ok(doceSalvo);
                })
                .orElse(ResponseEntity.notFound().build());
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
}

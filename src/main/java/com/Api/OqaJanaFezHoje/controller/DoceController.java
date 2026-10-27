package com.Api.OqaJanaFezHoje.controller;

import com.Api.OqaJanaFezHoje.entity.Doce;
import com.Api.OqaJanaFezHoje.service.DoceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/doces")
public class DoceController {

    @Autowired
    private DoceService service;

    @PostMapping
    public ResponseEntity<Doce> salvarDoce(@RequestBody Doce doce){
        var doceCriado = service.salvarDoce(doce);
        return ResponseEntity.ok(doceCriado);
    }

    @GetMapping
    public ResponseEntity<Iterable<Doce>> listarDoces() {
        return service.listarDoces();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarDoce(@PathVariable Long id) {
        return service.deletarDoce(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Doce> atualizarDoce(@PathVariable Long id, @RequestBody Doce doceAtualizado) {
        return service.atualizarDoce(id, doceAtualizado)
                .map(doceSalvo -> ResponseEntity.ok(doceSalvo))
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Doce> buscarDocePorId(@PathVariable Long id) {
        return service.buscarDocePorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public ResponseEntity<Iterable<Doce>> buscarDocesPorNome(@RequestParam String nome) {
        return service.buscarDocesPorNome(nome);
    }
}

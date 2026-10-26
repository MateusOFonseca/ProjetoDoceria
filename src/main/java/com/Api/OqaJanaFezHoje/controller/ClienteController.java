package com.Api.OqaJanaFezHoje.controller;

import com.Api.OqaJanaFezHoje.dtos.ClienteResumoResponse;
import com.Api.OqaJanaFezHoje.entity.Cliente;
import com.Api.OqaJanaFezHoje.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Cliente> salvarCliente(
            @RequestBody Cliente cliente
    ) {
        return ResponseEntity.ok(service.salvarCliente(cliente));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResumoResponse>> listarClientes() {
        return ResponseEntity.ok(service.listarCliente());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ClienteResumoResponse>> buscarClientesPorNome(
            @RequestParam String nome
    ) {
        return ResponseEntity.ok(service.buscarPorNome(nome));
    }

    @GetMapping("/buscar-telefone")
    public ResponseEntity<List<ClienteResumoResponse>> buscarClientesPorTelefone(
            @RequestParam String telefone
    ) {
        return ResponseEntity.ok(
                service.buscarClientePeloTelefone(telefone)
        );
    }

    @GetMapping("/buscar-regiao")
    public ResponseEntity<List<ClienteResumoResponse>> buscarClientesPorRegiao(
            @RequestParam String regiao
    ) {
        return ResponseEntity.ok(
                service.buscarClientePelaRegiao(regiao)
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Cliente> atualizarCliente(
            @PathVariable Long id,
            @RequestBody Cliente cliente
    ) {
        service.atualizarParcial(id, cliente);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(
            @PathVariable Long id
    ) {
        service.deletarCliente(id);
        return ResponseEntity.ok().build();
    }
}
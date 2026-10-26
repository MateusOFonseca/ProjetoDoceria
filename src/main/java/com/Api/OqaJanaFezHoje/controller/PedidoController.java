package com.Api.OqaJanaFezHoje.controller;

import com.Api.OqaJanaFezHoje.dtos.PedidoRequest;
import com.Api.OqaJanaFezHoje.dtos.PedidoResponse;
import com.Api.OqaJanaFezHoje.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> salvarPedido(
            @RequestBody PedidoRequest pedido
    ) {
        PedidoResponse pedidoSalvo = service.criarPedido(pedido);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pedidoSalvo);
    }

    @GetMapping("/pendentes")
    public ResponseEntity<List<PedidoResponse>> buscarPedidosPendentes() {
        return ResponseEntity.ok(
                service.buscarPedidosPendentes()
        );
    }

    @GetMapping("/concluidos")
    public ResponseEntity<List<PedidoResponse>> buscarPedidosConcluidos() {
        return ResponseEntity.ok(
                service.buscarPedidosConcluidos()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPedido(@PathVariable Long id) {
        service.deletarPedido(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PedidoResponse> atualizarPedido(
            @PathVariable Long id,
            @RequestBody PedidoRequest pedidoAtualizado
    ) {
        PedidoResponse pedidoSalvo = service.modificarPedido(
                id,
                pedidoAtualizado
        );

        return ResponseEntity.ok(pedidoSalvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPedidoPorId(
            @PathVariable Long id
    ) {
        Optional<PedidoResponse> pedido =
                service.buscarPedidoPorId(id);

        return pedido
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listarPedidos() {
        return ResponseEntity.ok(service.listarPedidos());
    }

    @GetMapping("/buscar-cliente")
    public ResponseEntity<List<PedidoResponse>> buscarPedidosPorCliente(
            @RequestParam Long idCliente
    ) {
        return ResponseEntity.ok(
                service.buscarPedidosPorCliente(idCliente)
        );
    }

    @PatchMapping("/{id}/concluir")
    public ResponseEntity<PedidoResponse> concluirPedido(
            @PathVariable Long id
    ) {
        return service.concluirPedido(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
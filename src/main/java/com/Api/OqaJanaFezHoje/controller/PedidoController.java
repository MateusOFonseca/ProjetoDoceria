package com.Api.OqaJanaFezHoje.controller;

import com.Api.OqaJanaFezHoje.dtos.PedidoRequest;
import com.Api.OqaJanaFezHoje.dtos.PedidoResponse;
import com.Api.OqaJanaFezHoje.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService service;

    @PostMapping
    public ResponseEntity<PedidoResponse> salvarPedido(@RequestBody PedidoRequest pedido) {
        PedidoResponse pedidoSalvo = service.criarPedido(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoSalvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPedido(@PathVariable Long id) {
        service.deletarPedido(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PedidoResponse> atualizarPedido(@PathVariable Long id, @RequestBody PedidoRequest pedidoAtualizado) {
        PedidoResponse pedidoSalvo = service.modificarPedido(id, pedidoAtualizado);
        if (pedidoSalvo != null) {
            return ResponseEntity.ok(pedidoSalvo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<PedidoResponse>> buscarPedidoPorId(@PathVariable Long id) {
        Optional<PedidoResponse> pedido = service.buscarPedidoPorId(id);
        if (pedido != null) {
            return ResponseEntity.ok(pedido);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listarPedidos() {
        List<PedidoResponse> pedidos = service.listarPedidos();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/buscar-cliente")
    public ResponseEntity<List<PedidoResponse>> buscarPedidosPorCliente(@RequestParam Long idCliente) {
        List<PedidoResponse> pedidos = service.buscarPedidosPorCliente(idCliente);
        return ResponseEntity.ok(pedidos);
    }

}


package com.Api.OqaJanaFezHoje.controller;

import com.Api.OqaJanaFezHoje.dtos.PedidoRequest;
import com.Api.OqaJanaFezHoje.entity.Pedido;
import com.Api.OqaJanaFezHoje.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<PedidoRequest> salvarPedido(@RequestBody PedidoRequest pedido) {
        service.criarPedido(pedido);
        return ResponseEntity.ok().body(pedido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPedido(@PathVariable Long id) {
        service.deletarPedido(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Pedido> atualizarPedido(@PathVariable Long id, @RequestBody PedidoRequest pedidoAtualizado) {
        Pedido pedidoSalvo = service.modificarPedido(id, pedidoAtualizado);
        if (pedidoSalvo != null) {
            return ResponseEntity.ok(pedidoSalvo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Pedido>> buscarPedidoPorId(@PathVariable Long id) {
        Optional<Pedido> pedido = service.buscarPedidoPorId(id);
        if (pedido != null) {
            return ResponseEntity.ok(pedido);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listarPedidos() {
        List<Pedido> pedidos = service.listarPedidos();
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/buscar-cliente")
    public ResponseEntity<List<Pedido>> buscarPedidosPorCliente(@RequestParam Long idCliente) {
        List<Pedido> pedidos = service.buscarPedidosPorCliente(idCliente);
        return ResponseEntity.ok(pedidos);
    }

}


package com.Api.OqaJanaFezHoje.controller;

import com.Api.OqaJanaFezHoje.entity.Cliente;
import com.Api.OqaJanaFezHoje.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService service;

    @PostMapping
    public ResponseEntity<Cliente> salvarCliente(@RequestBody Cliente cliente){
       var clienteCriado = service.salvarCliente(cliente);
        return ResponseEntity.ok(clienteCriado);
    };
    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes (Cliente cliente){
        return service.listarCliente();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletarCliente(@PathVariable Long id){
        var deletado = service.deletarCliente(id);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }
    @GetMapping("/buscar")
    public ResponseEntity<List<Cliente>> buscarClientesPorNome(@RequestParam String nome) {
        List<Cliente> clientes = service.buscarPorNome(nome);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/buscar-telefone")
    public ResponseEntity<List<Cliente>> buscarClientesPorTelefone(@RequestParam String telefone) {
        List<Cliente> clientes = service.buscarClientePeloTelefone(telefone);
        return ResponseEntity.ok(clientes);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Long id, @RequestBody Cliente clienteAtualizado) {
        return service.atualizarParcial(id, clienteAtualizado)
                .map(clienteSalvo -> ResponseEntity.ok(clienteSalvo))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar-regiao")
    public ResponseEntity<List<Cliente>> buscarClientesPorRegiao(@RequestParam String regiao) {
        List<Cliente> clientes = service.buscarClientePelaRegiao(regiao);
        return ResponseEntity.ok(clientes);
    }


}

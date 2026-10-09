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

    @DeleteMapping
    public ResponseEntity<HttpStatus> deletarCliente(@RequestBody Long id){
        var deletado = service.deletarCliente(id);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }
    @GetMapping("/buscarPorNome")
    public ResponseEntity<List<Cliente>> buscarPeloNome(@RequestBody String nome){
        var cliente = service.buscarClientesPorNome(nome);
        return ResponseEntity.ok().body(cliente).getBody();
    }





}

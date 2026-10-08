package com.Api.OqaJanaFezHoje.service;

import com.Api.OqaJanaFezHoje.entity.Cliente;
import com.Api.OqaJanaFezHoje.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente salvarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public ResponseEntity<Cliente> atualizarCliente(Long id, Cliente clienteAtualizado) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    cliente.setNome(clienteAtualizado.getNome());
                    cliente.setTelefone(clienteAtualizado.getTelefone());
                    Cliente clienteSalvo = clienteRepository.save(cliente);
                    return ResponseEntity.ok(clienteSalvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Void> deletarCliente(Long id) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    clienteRepository.delete(cliente);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<Cliente>> listarCliente(){
        return ResponseEntity.ok(clienteRepository.findAll());
    }

    public ResponseEntity<List<Cliente>> buscarClientesPorNome(String nome) {
        List<Cliente> clientes = clienteRepository.findAll().stream()
                .filter(cliente -> cliente.getNome().toLowerCase().contains(nome.toLowerCase()))
                .toList();
        return ResponseEntity.ok(clientes);
    }

    public ResponseEntity<List<Cliente>> buscarClientePeloTelefone(String telefone) {
        List<Cliente> clientes = clienteRepository.findAll().stream()
                .filter(cliente -> cliente.getTelefone() != null && cliente.getTelefone().contains(telefone))
                .toList();
        return ResponseEntity.ok(clientes);
    }
}

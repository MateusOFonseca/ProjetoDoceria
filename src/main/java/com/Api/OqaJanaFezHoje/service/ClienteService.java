package com.Api.OqaJanaFezHoje.service;

import com.Api.OqaJanaFezHoje.entity.Cliente;
import com.Api.OqaJanaFezHoje.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente salvarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Optional<Cliente> atualizarParcial(Long id, Cliente dadosNovos) {
        return clienteRepository.findById(id).map(clienteOriginal -> {

            if (dadosNovos.getNome() != null && !dadosNovos.getNome().trim().isEmpty()) {
                clienteOriginal.setNome(dadosNovos.getNome());
            }

            if (dadosNovos.getTelefone() != null && !dadosNovos.getTelefone().trim().isEmpty()) {
                clienteOriginal.setTelefone(dadosNovos.getTelefone());
            }

            if (dadosNovos.getRegiao() != null && !dadosNovos.getRegiao().trim().isEmpty()) {
                clienteOriginal.setRegiao(dadosNovos.getRegiao());
            }
            return clienteRepository.save(clienteOriginal);
        });
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

    public List<Cliente> buscarPorNome(String nome) {

        if (nome == null || nome.trim().isEmpty()) {
            return List.of();
        }

        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Cliente> buscarClientePeloTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return List.of();
        }
        return clienteRepository.findByTelefoneContaining(telefone);
    }

    public List<Cliente> buscarClientePelaRegiao(String regiao) {
        if (regiao == null || regiao.trim().isEmpty()) {
            return List.of();
        }
        return clienteRepository.findByRegiaoContainingIgnoreCase(regiao);
    }



}

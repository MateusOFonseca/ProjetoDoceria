package com.Api.OqaJanaFezHoje.service;

import com.Api.OqaJanaFezHoje.dtos.ClienteResumoResponse;
import com.Api.OqaJanaFezHoje.entity.Cliente;
import com.Api.OqaJanaFezHoje.mapper.ClienteMapper;
import com.Api.OqaJanaFezHoje.repository.ClienteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteService(
            ClienteRepository clienteRepository,
            ClienteMapper clienteMapper
    ) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    public Cliente salvarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Transactional(readOnly = true)
    public List<ClienteResumoResponse> listarCliente() {
        return clienteRepository.findAll()
                .stream()
                .map(clienteMapper::toResumoResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ClienteResumoResponse> buscarPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return List.of();
        }

        return clienteRepository
                .findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(clienteMapper::toResumoResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ClienteResumoResponse> buscarClientePeloTelefone(
            String telefone
    ) {
        if (telefone == null || telefone.isBlank()) {
            return List.of();
        }

        return clienteRepository
                .findByTelefoneContaining(telefone)
                .stream()
                .map(clienteMapper::toResumoResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ClienteResumoResponse> buscarClientePelaRegiao(
            String regiao
    ) {
        if (regiao == null || regiao.isBlank()) {
            return List.of();
        }

        return clienteRepository
                .findByRegiaoContainingIgnoreCase(regiao)
                .stream()
                .map(clienteMapper::toResumoResponse)
                .toList();
    }

    @Transactional
    public Optional<Cliente> atualizarParcial(
            Long id,
            Cliente dadosNovos
    ) {
        return clienteRepository.findById(id)
                .map(clienteOriginal -> {
                    if (dadosNovos.getNome() != null
                            && !dadosNovos.getNome().isBlank()) {
                        clienteOriginal.setNome(dadosNovos.getNome());
                    }

                    if (dadosNovos.getTelefone() != null
                            && !dadosNovos.getTelefone().isBlank()) {
                        clienteOriginal.setTelefone(dadosNovos.getTelefone());
                    }

                    if (dadosNovos.getRegiao() != null
                            && !dadosNovos.getRegiao().isBlank()) {
                        clienteOriginal.setRegiao(dadosNovos.getRegiao());
                    }

                    return clienteRepository.save(clienteOriginal);
                });
    }

    @Transactional
    public ResponseEntity<Void> deletarCliente(Long id) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    clienteRepository.delete(cliente);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



}
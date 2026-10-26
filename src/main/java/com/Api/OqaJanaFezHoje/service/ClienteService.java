package com.Api.OqaJanaFezHoje.service;

import com.Api.OqaJanaFezHoje.dtos.ClienteResumoResponse;
import com.Api.OqaJanaFezHoje.entity.Cliente;
import com.Api.OqaJanaFezHoje.mapper.ClienteMapper;
import com.Api.OqaJanaFezHoje.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public void atualizarParcial(Long id, Cliente cliente) {
        clienteRepository.findById(id).ifPresent(c -> {
            if (cliente.getNome() != null) {
                c.setNome(cliente.getNome());
            }
            if (cliente.getTelefone() != null) {
                c.setTelefone(cliente.getTelefone());
            }
            if (cliente.getRegiao() != null) {
                c.setRegiao(cliente.getRegiao());
            }
            clienteRepository.save(c);
        });
    }

    public void deletarCliente(Long id) {
        clienteRepository.deleteById(id);
    }
}
package com.Api.OqaJanaFezHoje.service;

import com.Api.OqaJanaFezHoje.dtos.ClienteResumoResponse;
import com.Api.OqaJanaFezHoje.entity.Cliente;
import com.Api.OqaJanaFezHoje.mapper.ClienteMapper;
import com.Api.OqaJanaFezHoje.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void deveSalvarCliente() {
        Cliente cliente = new Cliente();
        cliente.setNome("Mateus");

        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente resultado = clienteService.salvarCliente(cliente);

        assertSame(cliente, resultado);
        verify(clienteRepository).save(cliente);
    }

    @Test
    void deveListarClientesConvertidosParaResumo() {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);
        cliente.setNome("Mateus");

        ClienteResumoResponse resumo = new ClienteResumoResponse(
                1L,
                "Mateus",
                "11999999999",
                "Osasco",
                null,
                0L,
                java.math.BigDecimal.ZERO,
                null
        );

        when(clienteRepository.findAll()).thenReturn(List.of(cliente));
        when(clienteMapper.toResumoResponse(cliente)).thenReturn(resumo);

        List<ClienteResumoResponse> resultado =
                clienteService.listarCliente();

        assertEquals(1, resultado.size());
        assertEquals(resumo, resultado.get(0));
        verify(clienteMapper).toResumoResponse(cliente);
    }

    @Test
    void deveRetornarListaVaziaQuandoNomeForNulo() {
        List<ClienteResumoResponse> resultado =
                clienteService.buscarPorNome(null);

        assertTrue(resultado.isEmpty());
        verifyNoInteractions(clienteRepository, clienteMapper);
    }

    @Test
    void deveRetornarListaVaziaQuandoNomeForBranco() {
        List<ClienteResumoResponse> resultado =
                clienteService.buscarPorNome("   ");

        assertTrue(resultado.isEmpty());
        verifyNoInteractions(clienteRepository, clienteMapper);
    }

    @Test
    void deveBuscarClientePorNome() {
        Cliente cliente = new Cliente();
        cliente.setNome("Mateus");

        ClienteResumoResponse resumo = new ClienteResumoResponse(
                1L,
                "Mateus",
                null,
                null,
                null,
                0L,
                java.math.BigDecimal.ZERO,
                null
        );

        when(clienteRepository.findByNomeContainingIgnoreCase("Mateus"))
                .thenReturn(List.of(cliente));
        when(clienteMapper.toResumoResponse(cliente)).thenReturn(resumo);

        List<ClienteResumoResponse> resultado =
                clienteService.buscarPorNome("Mateus");

        assertEquals(List.of(resumo), resultado);
        verify(clienteRepository)
                .findByNomeContainingIgnoreCase("Mateus");
    }

    @Test
    void deveRetornarListaVaziaQuandoTelefoneForInvalido() {
        assertTrue(clienteService.buscarClientePeloTelefone("").isEmpty());
        assertTrue(clienteService.buscarClientePeloTelefone(null).isEmpty());

        verifyNoInteractions(clienteRepository, clienteMapper);
    }

    @Test
    void deveBuscarClientePorTelefone() {
        Cliente cliente = new Cliente();
        cliente.setTelefone("11999999999");

        ClienteResumoResponse resumo = new ClienteResumoResponse(
                1L,
                "Mateus",
                "11999999999",
                null,
                null,
                0L,
                java.math.BigDecimal.ZERO,
                null
        );

        when(clienteRepository.findByTelefoneContaining("11999999999"))
                .thenReturn(List.of(cliente));
        when(clienteMapper.toResumoResponse(cliente)).thenReturn(resumo);

        List<ClienteResumoResponse> resultado =
                clienteService.buscarClientePeloTelefone("11999999999");

        assertEquals(1, resultado.size());
        assertEquals(resumo, resultado.get(0));
    }

    @Test
    void deveAtualizarClienteParcialmente() {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);
        cliente.setNome("Nome antigo");
        cliente.setTelefone("11111111111");
        cliente.setRegiao("Centro");

        Cliente dadosNovos = new Cliente();
        dadosNovos.setNome("Nome novo");
        dadosNovos.setTelefone("11999999999");
        dadosNovos.setRegiao("Osasco");

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Optional<Cliente> resultado =
                clienteService.atualizarParcial(1L, dadosNovos);

        assertTrue(resultado.isPresent());
        assertEquals("Nome novo", cliente.getNome());
        assertEquals("11999999999", cliente.getTelefone());
        assertEquals("Osasco", cliente.getRegiao());
        verify(clienteRepository).save(cliente);
    }

    @Test
    void naoDeveAtualizarClienteInexistente() {
        Cliente dadosNovos = new Cliente();

        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Cliente> resultado =
                clienteService.atualizarParcial(999L, dadosNovos);

        assertTrue(resultado.isEmpty());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void deveDeletarClienteExistente() {
        Cliente cliente = new Cliente();

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        var resposta = clienteService.deletarCliente(1L);

        assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());
        verify(clienteRepository).delete(cliente);
    }

    @Test
    void deveRetornar404AoDeletarClienteInexistente() {
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        var resposta = clienteService.deletarCliente(999L);

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
        verify(clienteRepository, never()).delete(any());
    }
}
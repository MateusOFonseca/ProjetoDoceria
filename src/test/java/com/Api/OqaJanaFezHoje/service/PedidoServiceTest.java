package com.Api.OqaJanaFezHoje.service;

import com.Api.OqaJanaFezHoje.dtos.ItemPedidoRequest;
import com.Api.OqaJanaFezHoje.dtos.PedidoRequest;
import com.Api.OqaJanaFezHoje.dtos.PedidoResponse;
import com.Api.OqaJanaFezHoje.entity.Cliente;
import com.Api.OqaJanaFezHoje.entity.Doce;
import com.Api.OqaJanaFezHoje.entity.Pedido;
import com.Api.OqaJanaFezHoje.entity.enums.StatusPedido;
import com.Api.OqaJanaFezHoje.mapper.PedidoMapper;
import com.Api.OqaJanaFezHoje.repository.ClienteRepository;
import com.Api.OqaJanaFezHoje.repository.DoceRepository;
import com.Api.OqaJanaFezHoje.repository.PedidoRepository;
import com.Api.OqaJanaFezHoje.security.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private DoceRepository doceRepository;

    @Mock
    private PedidoMapper pedidoMapper;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void deveCriarPedidoComClienteEDoceExistentes() {
        Cliente cliente = criarCliente(1L, "Mateus");
        Doce doce = criarDoce(1L, "Brownie", "10.00");

        Pedido pedidoSalvo = new Pedido();
        pedidoSalvo.setIdPedido(1L);
        pedidoSalvo.setCliente(cliente);
        pedidoSalvo.setStatus(StatusPedido.PENDENTE);
        pedidoSalvo.setValorTotal(new BigDecimal("20.00"));

        PedidoResponse response = mock(PedidoResponse.class);

        PedidoRequest request = new PedidoRequest(
                1L,
                List.of(new ItemPedidoRequest("Brownie", 2, null))
        );

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(doceRepository.findByNomeEqualsIgnoreCase("Brownie"))
                .thenReturn(Optional.of(doce));
        when(pedidoRepository.save(any(Pedido.class)))
                .thenReturn(pedidoSalvo);
        when(pedidoMapper.toResponse(pedidoSalvo))
                .thenReturn(response);

        PedidoResponse resultado = pedidoService.criarPedido(request);

        assertSame(response, resultado);
        verify(pedidoRepository).save(any(Pedido.class));
        verify(pedidoMapper).toResponse(pedidoSalvo);
    }

    @Test
    void naoDeveCriarPedidoQuandoClienteNaoExistir() {
        PedidoRequest request = new PedidoRequest(
                999L,
                List.of()
        );

        when(clienteRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> pedidoService.criarPedido(request)
        );

        verifyNoInteractions(doceRepository, pedidoRepository, pedidoMapper);
    }

    @Test
    void naoDeveCriarPedidoQuandoDoceNaoExistir() {
        Cliente cliente = criarCliente(1L, "Mateus");

        PedidoRequest request = new PedidoRequest(
                1L,
                List.of(new ItemPedidoRequest("Doce inexistente", 1, null))
        );

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));
        when(doceRepository.findByNomeEqualsIgnoreCase("Doce inexistente"))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> pedidoService.criarPedido(request)
        );

        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void deveListarPedidosConvertidos() {
        Pedido pedido = criarPedido(1L);
        PedidoResponse response = mock(PedidoResponse.class);

        when(pedidoRepository.findAll()).thenReturn(List.of(pedido));
        when(pedidoMapper.toResponse(pedido)).thenReturn(response);

        List<PedidoResponse> resultado =
                pedidoService.listarPedidos();

        assertEquals(List.of(response), resultado);
        verify(pedidoMapper).toResponse(pedido);
    }

    @Test
    void deveRetornarPedidoPorId() {
        Pedido pedido = criarPedido(1L);
        PedidoResponse response = mock(PedidoResponse.class);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoMapper.toResponse(pedido)).thenReturn(response);

        Optional<PedidoResponse> resultado =
                pedidoService.buscarPedidoPorId(1L);

        assertTrue(resultado.isPresent());
        assertSame(response, resultado.get());
    }

    @Test
    void deveRetornarVazioQuandoPedidoNaoExistir() {
        when(pedidoRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<PedidoResponse> resultado =
                pedidoService.buscarPedidoPorId(999L);

        assertTrue(resultado.isEmpty());
        verifyNoInteractions(pedidoMapper);
    }

    @Test
    void deveBuscarPedidosPorCliente() {
        Pedido pedido = criarPedido(1L);
        PedidoResponse response = mock(PedidoResponse.class);

        when(pedidoRepository.findByClienteIdCliente(2L))
                .thenReturn(List.of(pedido));
        when(pedidoMapper.toResponse(pedido)).thenReturn(response);

        List<PedidoResponse> resultado =
                pedidoService.buscarPedidosPorCliente(2L);

        assertEquals(List.of(response), resultado);
    }

    @Test
    void deveListarPedidosPendentes() {
        Pedido pedido = criarPedido(1L);
        pedido.setStatus(StatusPedido.PENDENTE);

        PedidoResponse response = mock(PedidoResponse.class);

        when(pedidoRepository.findByStatus(StatusPedido.PENDENTE))
                .thenReturn(List.of(pedido));
        when(pedidoMapper.toResponse(pedido)).thenReturn(response);

        List<PedidoResponse> resultado =
                pedidoService.buscarPedidosPendentes();

        assertEquals(List.of(response), resultado);
    }

    @Test
    void deveConcluirPedido() {
        Pedido pedido = criarPedido(1L);
        pedido.setStatus(StatusPedido.PENDENTE);

        PedidoResponse response = mock(PedidoResponse.class);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(pedido)).thenReturn(pedido);
        when(pedidoMapper.toResponse(pedido)).thenReturn(response);

        Optional<PedidoResponse> resultado =
                pedidoService.concluirPedido(1L);

        assertTrue(resultado.isPresent());
        assertSame(response, resultado.get());
        assertEquals(StatusPedido.CONCLUIDO, pedido.getStatus());
        verify(pedidoRepository).save(pedido);
    }

    @Test
    void naoDeveConcluirPedidoInexistente() {
        when(pedidoRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<PedidoResponse> resultado =
                pedidoService.concluirPedido(999L);

        assertTrue(resultado.isEmpty());
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void deveDeletarPedidoExistente() {
        Pedido pedido = criarPedido(1L);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        assertDoesNotThrow(() -> pedidoService.deletarPedido(1L));

        verify(pedidoRepository).delete(pedido);
    }

    @Test
    void deveLancarExcecaoAoDeletarPedidoInexistente() {
        when(pedidoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> pedidoService.deletarPedido(999L)
        );

        verify(pedidoRepository, never()).delete(any());
    }

    private Cliente criarCliente(Long id, String nome) {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(id);
        cliente.setNome(nome);
        return cliente;
    }

    private Doce criarDoce(Long id, String nome, String preco) {
        Doce doce = new Doce();
        doce.setIdDoce(id);
        doce.setNome(nome);
        doce.setPreco(new BigDecimal(preco));
        return doce;
    }

    private Pedido criarPedido(Long id) {
        Pedido pedido = new Pedido();
        pedido.setIdPedido(id);
        pedido.setCliente(criarCliente(1L, "Mateus"));
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setValorTotal(BigDecimal.ZERO);
        pedido.setItens(List.of());
        return pedido;
    }
}
package com.Api.OqaJanaFezHoje.service;

import com.Api.OqaJanaFezHoje.entity.Doce;
import com.Api.OqaJanaFezHoje.repository.DoceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoceServiceTest {

    @Mock
    private DoceRepository doceRepository;

    @InjectMocks
    private DoceService doceService;

    @Test
    void deveSalvarDoce() {
        Doce doce = criarDoce(1L, "Brownie", "10.00", 5);

        when(doceRepository.save(doce)).thenReturn(doce);

        Doce resultado = doceService.salvarDoce(doce);

        assertSame(doce, resultado);
        verify(doceRepository).save(doce);
    }

    @Test
    void deveListarDoces() {
        Doce doce = criarDoce(1L, "Brownie", "10.00", 5);

        when(doceRepository.findAll()).thenReturn(List.of(doce));

        var resposta = doceService.listarDoces();

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertNotNull(resposta.getBody());
        assertEquals(List.of(doce), resposta.getBody());
    }

    @Test
    void deveBuscarDocePorId() {
        Doce doce = criarDoce(1L, "Brownie", "10.00", 5);

        when(doceRepository.findById(1L)).thenReturn(Optional.of(doce));

        Optional<Doce> resultado = doceService.buscarDocePorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(doce, resultado.get());
    }

    @Test
    void deveRetornarVazioQuandoDoceNaoExistir() {
        when(doceRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Doce> resultado = doceService.buscarDocePorId(999L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveBuscarDocesPorNomeSemDiferenciarMaiusculas() {
        Doce brownie = criarDoce(1L, "Brownie", "10.00", 5);
        Doce bolo = criarDoce(2L, "Bolo", "20.00", 3);

        when(doceRepository.findAll()).thenReturn(List.of(brownie, bolo));

        var resposta = doceService.buscarDocesPorNome("BROw");

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertNotNull(resposta.getBody());
        assertEquals(List.of(brownie), resposta.getBody());
    }

    @Test
    void deveRetornarListaVaziaQuandoNomeNaoForEncontrado() {
        Doce brownie = criarDoce(1L, "Brownie", "10.00", 5);

        when(doceRepository.findAll()).thenReturn(List.of(brownie));

        var resposta = doceService.buscarDocesPorNome("Torta");

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertNotNull(resposta.getBody());
        assertFalse(resposta.getBody().iterator().hasNext());
    }

    @Test
    void deveAtualizarDoce() {
        Doce doce = criarDoce(1L, "Brownie", "10.00", 5);

        Doce dadosNovos = new Doce();
        dadosNovos.setNome("Brownie especial");
        dadosNovos.setDescricao("Com chocolate");
        dadosNovos.setPreco(new BigDecimal("15.00"));

        when(doceRepository.findById(1L)).thenReturn(Optional.of(doce));
        when(doceRepository.save(doce)).thenReturn(doce);

        Optional<Doce> resultado =
                doceService.atualizarDoce(1L, dadosNovos);

        assertTrue(resultado.isPresent());
        assertEquals("Brownie especial", doce.getNome());
        assertEquals("Com chocolate", doce.getDescricao());
        assertEquals(new BigDecimal("15.00"), doce.getPreco());
        verify(doceRepository).save(doce);
    }

    @Test
    void naoDeveAtualizarDoceInexistente() {
        when(doceRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Doce> resultado =
                doceService.atualizarDoce(999L, new Doce());

        assertTrue(resultado.isEmpty());
        verify(doceRepository, never()).save(any());
    }

    @Test
    void deveDeletarDoceExistente() {
        Doce doce = criarDoce(1L, "Brownie", "10.00", 5);

        when(doceRepository.findById(1L)).thenReturn(Optional.of(doce));

        var resposta = doceService.deletarDoce(1L);

        assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());
        verify(doceRepository).delete(doce);
    }

    @Test
    void deveRetornar404AoDeletarDoceInexistente() {
        when(doceRepository.findById(999L)).thenReturn(Optional.empty());

        var resposta = doceService.deletarDoce(999L);

        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
        verify(doceRepository, never()).delete(any());
    }

    private Doce criarDoce(
            Long id,
            String nome,
            String preco,
            Integer estoque
    ) {
        Doce doce = new Doce();
        doce.setIdDoce(id);
        doce.setNome(nome);
        doce.setPreco(new BigDecimal(preco));
        return doce;
    }
}
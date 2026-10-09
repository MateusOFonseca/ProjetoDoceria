package com.Api.OqaJanaFezHoje.service;

import com.Api.OqaJanaFezHoje.dtos.ItemPedidoRequest;
import com.Api.OqaJanaFezHoje.dtos.PedidoRequest;
import com.Api.OqaJanaFezHoje.entity.Cliente;
import com.Api.OqaJanaFezHoje.entity.Doce;
import com.Api.OqaJanaFezHoje.entity.ItemPedido;
import com.Api.OqaJanaFezHoje.entity.Pedido;
import com.Api.OqaJanaFezHoje.repository.ClienteRepository;
import com.Api.OqaJanaFezHoje.repository.DoceRepository;
import com.Api.OqaJanaFezHoje.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private DoceRepository doceRepository;

    @Transactional
    public Pedido criarPedido(PedidoRequest dto) {
        Cliente cliente = clienteRepository.findById(Long.valueOf(dto.idCliente()))
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setStatus("Pendente");

        List<ItemPedido> listaItensDoPedido = new ArrayList<>();
        BigDecimal valorTotalPedido = BigDecimal.ZERO;

        for (ItemPedidoRequest itemDto : dto.itens()) {
            Doce doce = doceRepository.findByNomeEqualsIgnoreCase(itemDto.nomeDoce())
                    .orElseThrow(() -> new RuntimeException("Doce não encontrado com o nome: " + itemDto.nomeDoce()));

            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setDoce(doce);
            item.setQuantidade(itemDto.quantidade());
            item.setPrecoUnitario(doce.getPreco());

            listaItensDoPedido.add(item);

            BigDecimal subtotal = doce.getPreco().multiply(BigDecimal.valueOf(itemDto.quantidade()));
            valorTotalPedido = valorTotalPedido.add(subtotal);
        }

        pedido.setItens(listaItensDoPedido);
        pedido.setValorTotal(valorTotalPedido);

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public Pedido modificarPedido(Integer idPedido, PedidoRequest dto) {
        // 1. Localiza o pedido existente
        Pedido pedido = pedidoRepository.findById(Long.valueOf(idPedido))
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com o ID: " + idPedido));

        if (!pedido.getCliente().getIdCliente().equals(dto.idCliente())) {
            Cliente novoCliente = clienteRepository.findById(Long.valueOf(dto.idCliente()))
                    .orElseThrow(() -> new RuntimeException("Novo cliente não encontrado!"));
            pedido.setCliente(novoCliente);
        }

        List<ItemPedido> itensAtualizados = new ArrayList<>();

        for (ItemPedidoRequest itemDto : dto.itens()) {
            Doce doce = doceRepository.findByNomeEqualsIgnoreCase(itemDto.nomeDoce())
                    .orElseThrow(() -> new RuntimeException("Doce não encontrado: " + itemDto.nomeDoce()));

            Optional<ItemPedido> itemExistente = pedido.getItens().stream()
                    .filter(item -> item.getDoce().getIdDoce().equals(doce.getIdDoce()))
                    .findFirst();

            if (itemExistente.isPresent()) {
                ItemPedido item = itemExistente.get();
                item.setQuantidade(itemDto.quantidade());
                item.setPrecoUnitario(doce.getPreco());
                itensAtualizados.add(item);
            } else {

                ItemPedido novoItem = new ItemPedido();
                novoItem.setPedido(pedido);
                novoItem.setDoce(doce);
                novoItem.setQuantidade(itemDto.quantidade());
                novoItem.setPrecoUnitario(doce.getPreco());
                itensAtualizados.add(novoItem);
            }
        }

        pedido.getItens().removeIf(itemAntigo -> !itensAtualizados.contains(itemAntigo));

        for (ItemPedido item : itensAtualizados) {
            if (!pedido.getItens().contains(item)) {
                pedido.getItens().add(item);
            }
        }

        BigDecimal novoValorTotal = pedido.getItens().stream()
                .map(item -> item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        pedido.setValorTotal(novoValorTotal);

        return pedidoRepository.save(pedido);
    }

    public void deletarPedido(Integer idPedido) {
        Pedido pedido = pedidoRepository.findById(Long.valueOf(idPedido))
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com o ID: " + idPedido));
        pedidoRepository.delete(pedido);
    }


}

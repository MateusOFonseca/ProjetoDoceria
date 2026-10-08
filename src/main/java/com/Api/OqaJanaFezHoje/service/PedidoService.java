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

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private DoceRepository doceRepository;

        public PedidoService(PedidoRepository pedidoRepository,
                             ClienteRepository clienteRepository,
                             DoceRepository doceRepository) {
            this.pedidoRepository = pedidoRepository;
            this.clienteRepository = clienteRepository;
            this.doceRepository = doceRepository;
        }

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



}

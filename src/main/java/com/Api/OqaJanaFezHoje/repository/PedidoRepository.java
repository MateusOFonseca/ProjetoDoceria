package com.Api.OqaJanaFezHoje.repository;

import com.Api.OqaJanaFezHoje.entity.Pedido;
import com.Api.OqaJanaFezHoje.entity.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("""
            SELECT p
            FROM Pedido p
            WHERE p.cliente.idCliente = :idCliente
            """)
    List<Pedido> findByClienteIdCliente(
            @Param("idCliente") Long idCliente
    );

    List<Pedido> findByStatus(StatusPedido status);
}
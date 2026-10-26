package com.Api.OqaJanaFezHoje.repository;

import com.Api.OqaJanaFezHoje.entity.Pedido;
import com.Api.OqaJanaFezHoje.entity.enums.StatusPedido;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface DashboardRepository extends Repository<Pedido, Long> {

    @Query("""
            SELECT COUNT(p)
            FROM Pedido p
            WHERE p.status = :status
              AND p.dataPedido >= :inicio
              AND p.dataPedido < :fim
            """)
    long contarPedidosDoPeriodo(
            @Param("status") StatusPedido status,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

    @Query("""
            SELECT COALESCE(SUM(p.valorTotal), 0)
            FROM Pedido p
            WHERE p.status = :status
              AND p.dataPedido >= :inicio
              AND p.dataPedido < :fim
            """)
    BigDecimal somarFaturamentoDoPeriodo(
            @Param("status") StatusPedido status,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

    @Query("""
            SELECT c.nome
            FROM Pedido p
            JOIN p.cliente c
            WHERE p.status = :status
              AND p.dataPedido >= :inicio
              AND p.dataPedido < :fim
            GROUP BY c.idCliente, c.nome
            HAVING COUNT(p.idPedido) > 1
            ORDER BY c.nome
            """)
    List<String> buscarNomesClientesRecorrentes(
            @Param("status") StatusPedido status,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

    @Query("""
            SELECT d.nome AS nomeDoce,
                   SUM(i.quantidade) AS quantidadeVendida
            FROM ItemPedido i
            JOIN i.pedido p
            JOIN i.doce d
            WHERE p.status = :status
              AND p.dataPedido >= :inicio
              AND p.dataPedido < :fim
            GROUP BY d.idDoce, d.nome
            ORDER BY d.nome
            """)
    List<DoceVendido> buscarTodosOsDocesVendidos(
            @Param("status") StatusPedido status,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );
}
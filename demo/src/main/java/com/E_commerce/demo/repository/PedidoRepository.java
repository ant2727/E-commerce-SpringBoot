package com.E_commerce.demo.repository;

import com.E_commerce.demo.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("""
            SELECT DISTINCT p FROM Pedido p
            LEFT JOIN FETCH p.itens i
            LEFT JOIN FETCH i.produto
            LEFT JOIN FETCH p.cliente
            WHERE p.id = :id
            """)
    Optional<Pedido> findByIdWithItens(@Param("id") Long id);

    @Query("""
            SELECT DISTINCT p FROM Pedido p
            LEFT JOIN FETCH p.itens i
            LEFT JOIN FETCH i.produto
            LEFT JOIN FETCH p.cliente
            WHERE p.cliente.id = :clienteId
            ORDER BY p.dataCriacao DESC
            """)
    List<Pedido> findByClienteIdWithItens(@Param("clienteId") Long clienteId);
}

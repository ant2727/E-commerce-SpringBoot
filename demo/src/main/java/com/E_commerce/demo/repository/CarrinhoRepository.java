package com.E_commerce.demo.repository;

import com.E_commerce.demo.entity.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {

    Optional<Carrinho> findByClienteId(Long clienteId);

    @Query("""
            SELECT DISTINCT c FROM Carrinho c
            LEFT JOIN FETCH c.itens i
            LEFT JOIN FETCH i.produto
            LEFT JOIN FETCH c.cliente
            WHERE c.cliente.id = :clienteId
            """)
    Optional<Carrinho> findByClienteIdWithItens(@Param("clienteId") Long clienteId);
}

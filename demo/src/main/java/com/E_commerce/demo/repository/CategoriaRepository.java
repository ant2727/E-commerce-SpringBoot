package com.E_commerce.demo.repository;

import com.E_commerce.demo.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    @Query("""
            SELECT DISTINCT c FROM Categoria c
            LEFT JOIN FETCH c.produtos
            WHERE c.id = :id
            """)
    Optional<Categoria> findByIdWithProdutos(@Param("id") Long id);
}

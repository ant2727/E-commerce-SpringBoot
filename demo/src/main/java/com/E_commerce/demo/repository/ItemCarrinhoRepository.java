package com.E_commerce.demo.repository;

import com.E_commerce.demo.entity.ItemCarrinho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemCarrinhoRepository extends JpaRepository<ItemCarrinho, Long> {

    Optional<ItemCarrinho> findByCarrinhoIdAndProdutoId(
            Long carrinhoId,
            Long produtoId
    );

    void deleteByCarrinhoId(Long carrinhoId);
}

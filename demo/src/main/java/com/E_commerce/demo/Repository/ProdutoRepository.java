package com.E_commerce.demo.Repository;

import com.E_commerce.demo.Entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}

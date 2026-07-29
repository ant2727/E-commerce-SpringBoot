package com.E_commerce.demo.Service;

import com.E_commerce.demo.Entity.Produto;
import com.E_commerce.demo.Repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class ProdutoService {

        private final ProdutoRepository repository;

        public ProdutoService(ProdutoRepository repository) {
            this.repository = repository;
        }

        public List<Produto> listarTodos() {
            return repository.findAll();
        }

    public Produto salvar(Produto produto) {
        return repository.save(produto);
    }
}

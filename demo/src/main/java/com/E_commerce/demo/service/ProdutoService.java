package com.E_commerce.demo.service;

import com.E_commerce.demo.dto.ProdutoRequest;
import com.E_commerce.demo.entity.Produto;
import com.E_commerce.demo.repository.ProdutoRepository;
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

    public Produto salvar(ProdutoRequest request) {

        Produto produto = new Produto();

        produto.setNome(request.getNome());
        produto.setPreco(request.getPreco());
        produto.setEstoque(request.getEstoque());

        return repository.save(produto);
    }

}

package com.E_commerce.demo.service;

import com.E_commerce.demo.dto.request.ClienteRequest;
import com.E_commerce.demo.dto.response.ClienteResponse;
import com.E_commerce.demo.entity.Cliente;
import com.E_commerce.demo.exception.ClienteNaoEncontradoException;
import com.E_commerce.demo.mapper.ClienteMapper;
import com.E_commerce.demo.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;
    private final ClienteMapper mapper;

    @Transactional
    public ClienteResponse cadastrar(ClienteRequest request) {
        repository.findByEmail(request.getEmail())
                .ifPresent(cliente -> {
                    throw new IllegalArgumentException("E-mail já cadastrado.");
                });

        Cliente cliente = mapper.toEntity(request);
        cliente = repository.save(cliente);
        return mapper.toResponse(cliente);
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Long id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));
        return mapper.toResponse(cliente);
    }

    @Transactional(readOnly = true)
    public Page<ClienteResponse> listar(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Transactional
    public ClienteResponse atualizar(Long id, ClienteRequest request) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));

        repository.findByEmail(request.getEmail())
                .ifPresent(outro -> {
                    if (!outro.getId().equals(id)) {
                        throw new IllegalArgumentException("E-mail já cadastrado.");
                    }
                });

        cliente.setNome(request.getNome());
        cliente.setEmail(request.getEmail());
        cliente.setTelefone(request.getTelefone());

        return mapper.toResponse(repository.save(cliente));
    }

    @Transactional
    public void excluir(Long id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException(id));
        repository.delete(cliente);
    }
}

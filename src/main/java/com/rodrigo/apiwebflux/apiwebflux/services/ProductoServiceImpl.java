package com.rodrigo.apiwebflux.apiwebflux.services;

import com.rodrigo.apiwebflux.apiwebflux.documents.Producto;
import com.rodrigo.apiwebflux.apiwebflux.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository repository;
    @Override
    public Flux<Producto> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Producto> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Mono<Producto> save(Producto producto) {
        return repository.save(producto);
    }

    @Override
    public Mono<Void> delete(Producto producto) {
        return repository.delete(producto);
    }

    @Override
    public Flux<Producto> findAllConNombreUpperCase() {
        return null;
    }

    @Override
    public Flux<Producto> findAllConNombreUpperCaseRepeat() {
        return null;
    }
}

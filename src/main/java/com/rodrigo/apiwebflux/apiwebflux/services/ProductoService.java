package com.rodrigo.apiwebflux.apiwebflux.services;

import com.rodrigo.apiwebflux.apiwebflux.documents.Producto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoService {

    public Flux<Producto> findAll();

    public Mono<Producto> findById(String id);

    public Mono<Producto> save(Producto producto);

    public Mono<Void> delete(Producto producto);

    public Flux<Producto> findAllConNombreUpperCase();
    public Flux<Producto> findAllConNombreUpperCaseRepeat();
}

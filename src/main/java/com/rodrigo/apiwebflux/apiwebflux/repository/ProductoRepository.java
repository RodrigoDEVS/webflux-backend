package com.rodrigo.apiwebflux.apiwebflux.repository;

import com.rodrigo.apiwebflux.apiwebflux.documents.Producto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends ReactiveMongoRepository<Producto, String> {
}

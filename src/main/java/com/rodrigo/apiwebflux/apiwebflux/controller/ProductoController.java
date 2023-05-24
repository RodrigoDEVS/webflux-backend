package com.rodrigo.apiwebflux.apiwebflux.controller;

import com.rodrigo.apiwebflux.apiwebflux.documents.Producto;
import com.rodrigo.apiwebflux.apiwebflux.repository.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository repository;

    private static final Logger log = LoggerFactory.getLogger(ProductoRepository.class);

    //Obtener todos los Productos
    @GetMapping
    //@ResponseBody
    public Flux<Producto> index(){
        Flux<Producto> productos = repository.findAll();
        productos.subscribe(producto -> log.info("RequestBody: " + producto.getNombre() + producto.getPrecio()));

        return productos;
    }

    //Get product by id
    @GetMapping("/{id}")
    public Mono<Producto> getById(@PathVariable String id){
        Mono<Producto> producto = repository.findById(id)
                .doOnNext(e -> log.info("RequestBody: " + e.getNombre() + e.getPrecio()));
        return producto;
    }

}

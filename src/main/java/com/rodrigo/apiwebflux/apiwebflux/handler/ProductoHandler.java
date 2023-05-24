package com.rodrigo.apiwebflux.apiwebflux.handler;

import com.rodrigo.apiwebflux.apiwebflux.ApiWebfluxApplication;
import com.rodrigo.apiwebflux.apiwebflux.documents.Producto;
import com.rodrigo.apiwebflux.apiwebflux.services.ProductoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Date;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
public class ProductoHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiWebfluxApplication.class);
    @Autowired
    private ProductoService service;

    @Autowired
    private Validator validator;

    public Mono<ServerResponse> listar(ServerRequest request){
            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(service.findAll(), Producto.class);
    }

    public Mono<ServerResponse> verPorId(ServerRequest request){
        String id = request.pathVariable("id");
        return service.findById(id)
                .flatMap(producto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(producto)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> crear(ServerRequest request){
        Mono<Producto> producto = request.bodyToMono(Producto.class);
        return producto.flatMap(p -> {

            Errors errors = new BeanPropertyBindingResult(p, Producto.class.getName());
            validator.validate(p, errors);

            if(errors.hasErrors()){
                return Flux.fromIterable(errors.getFieldErrors())
                        .map(e -> "El campo " + e.getField() + " " + e.getDefaultMessage())
                        .collectList()
                        .flatMap(list -> ServerResponse.badRequest()
                                .body(BodyInserters.fromValue(list)));
            }else{
                if(p.getCreateAt() == null){
                    p.setCreateAt(new Date());
                }
                return service.save(p).flatMap(pbd -> ServerResponse
                                .created(URI.create("api/v2/productos/".concat(pbd.getId())))
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromValue(pbd)));
            }
        });
    }

    public Mono<ServerResponse> editar(ServerRequest request){
        Mono<Producto> producto = request.bodyToMono(Producto.class);
        String id = request.pathVariable("id");

        Mono<Producto> productoDB = service.findById(id);

        return productoDB.zipWith(producto, (p1, p2) -> {
            p1.setNombre(p2.getNombre());
            p1.setPrecio(p2.getPrecio());
            return p1;
        }).flatMap(p -> ServerResponse.created(URI.create("api/v2/productos/".concat(p.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.save(p), Producto.class))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> eliminar(ServerRequest request){
        String id = request.pathVariable("id");
        Mono<Producto> productoDB = service.findById(id);

        return productoDB.flatMap(p -> service.delete(p)
                .then(ServerResponse.noContent().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}

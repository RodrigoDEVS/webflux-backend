package com.rodrigo.apiwebflux.apiwebflux.config;

import com.rodrigo.apiwebflux.apiwebflux.handler.ProductoHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;


import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterFunctionConfig {

    @Bean
    public RouterFunction<ServerResponse> rutas(ProductoHandler handler){
        return route(GET("/api/v2/productos"), handler::listar)
                .andRoute(GET("/api/v2/productos/{id}"), handler::verPorId)
                .andRoute(POST("/api/v2/productos"), handler::crear)
                .andRoute(PUT("/api/v2/productos/{id}"), handler::editar)
                .andRoute(DELETE("/api/v2/productos/{id}"), handler::eliminar);
    }
}

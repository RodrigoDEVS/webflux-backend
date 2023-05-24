package com.rodrigo.apiwebflux.apiwebflux;

import com.rodrigo.apiwebflux.apiwebflux.documents.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import reactor.core.publisher.Flux;
import com.rodrigo.apiwebflux.apiwebflux.repository.ProductoRepository;

import java.util.Date;

@SpringBootApplication
@EnableReactiveMongoRepositories(basePackages = "com.rodrigo.apiwebflux.apiwebflux.repository")
public class ApiWebfluxApplication implements CommandLineRunner {

	@Autowired
	private ProductoRepository repo;

	@Autowired
	private ReactiveMongoTemplate mongoTemplate;

	private static final Logger log = LoggerFactory.getLogger(ApiWebfluxApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(ApiWebfluxApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		mongoTemplate.dropCollection("productos").subscribe();

		Flux.just(
				new Producto("Televisor LG 50", 500.00),
				new Producto("Nevera Mabe", 250.00),
				new Producto("Impresora Hawlett Packard", 200.00),
				new Producto("Computador Portatil Nitro 5", 499.00)
		).flatMap(producto ->
						{
							producto.setCreateAt(new Date());
							return repo.save(producto);
						}
				)
				.subscribe(producto -> log.info("Insert: " +
						producto.getNombre() + "Creado: " +
						producto.getCreateAt()));
	}
}

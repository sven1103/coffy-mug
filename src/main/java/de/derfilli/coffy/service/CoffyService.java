package de.derfilli.coffy.service;

import de.derfilli.coffy.service.Concepts.Coffee;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

/**
 * <b><class short description - 1 Line!></b>
 *
 * <p><More detailed description - When to use, what it solves, etc.></p>
 *
 * @since <version tag>
 */
@Service
public class CoffyService {

  private final WebClient client;

  @Autowired
  public CoffyService(WebClient client) {
    this.client = Objects.requireNonNull(client);
  }

  public Flux<Coffee> getCoffees() {
    return queryCoffees();
  }

  private Flux<Coffee> queryCoffees() {
    return client.get().uri("/coffees")
        .retrieve().bodyToFlux(Coffee.class)
        .onErrorResume(Cache::coffees);
  }

}

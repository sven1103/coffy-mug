package de.derfilli.coffy.service;

import de.derfilli.coffy.service.Concepts.Account;
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
  private final Cache coffeeCache = new Cache();

  @Autowired
  public CoffyService(WebClient client) {
    this.client = Objects.requireNonNull(client);
  }

  public Flux<Coffee> getCoffees() {
    return queryCoffees();
  }

  public Flux<Account> getAccounts() {
    return queryAccounts();
  }

  private Flux<Account> queryAccounts() {
    return client.get().uri("/accounts")
        .retrieve()
        .bodyToFlux(Account.class);
  }

  private Flux<Coffee> queryCoffees() {
    return client.get().uri("/coffees")
        .retrieve().bodyToFlux(Coffee.class)
        .doOnNext(coffeeCache::add)
        .onErrorResume(coffeeCache::coffees);
  }


}

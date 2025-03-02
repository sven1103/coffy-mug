package de.derfilli.coffy.service;

import de.derfilli.coffy.VirtualThreadScheduler;
import de.derfilli.coffy.api.CoffyService;
import de.derfilli.coffy.api.Concepts.Account;
import de.derfilli.coffy.api.Concepts.AccountCreationRequest;
import de.derfilli.coffy.api.Concepts.Coffee;
import java.time.Duration;
import java.util.Objects;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * <b><class short description - 1 Line!></b>
 *
 * <p><More detailed description - When to use, what it solves, etc.></p>
 *
 * @since <version tag>
 */
@Service
public class CoffyServiceImpl implements CoffyService {

  private final WebClient client;
  private final Cache coffeeCache = new Cache();

  @Autowired
  public CoffyServiceImpl(WebClient client) {
    this.client = Objects.requireNonNull(client);
  }

  @Override
  public Flux<Coffee> getCoffees() {
    return queryCoffees();
  }

  @Override
  public Flux<Account> getAccounts() {
    return queryAccounts();
  }

  @Override
  public Mono<Account> createAccount(AccountCreationRequest request) {
    if (request == null) {
      return Mono.error(new IllegalArgumentException("request cannot be null"));
    }
    return Mono.defer(createNewAccount(request));
  }

  private Supplier<Mono<Account>> createNewAccount(
      AccountCreationRequest request) {
    return () -> client.post().uri("/accounts")
        .bodyValue(request)
        .retrieve()
        .bodyToMono(Account.class)
        .subscribeOn(VirtualThreadScheduler.getScheduler());
  }

  private Flux<Account> queryAccounts() {
    return client.get().uri("/accounts")
        .retrieve()
        .bodyToFlux(Account.class)
        .subscribeOn(VirtualThreadScheduler.getScheduler());
  }

  private Flux<Coffee> queryCoffees() {
    return client.get().uri("/coffees")
        .retrieve().bodyToFlux(Coffee.class)
        .delayElements(Duration.ofMillis(1000))
        .doOnNext(coffeeCache::add)
        .onErrorResume(coffeeCache::coffees)
        .subscribeOn(VirtualThreadScheduler.getScheduler());
  }


}

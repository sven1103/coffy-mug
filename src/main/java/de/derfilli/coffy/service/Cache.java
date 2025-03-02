package de.derfilli.coffy.service;

import de.derfilli.coffy.api.Concepts.Coffee;
import java.util.HashSet;
import java.util.Set;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

/**
 * <b><class short description - 1 Line!></b>
 *
 * <p><More detailed description - When to use, what it solves, etc.></p>
 *
 * @since <version tag>
 */
public class Cache {

  private static final Logger log = LoggerFactory.getLogger(Cache.class);

  private final Set<Coffee> coffees;

  public Cache() {
    this.coffees = new HashSet<>();
  }

  private Flux<Coffee> coffeesFlux() {
    return Flux.fromIterable(coffees);
  }

  public Publisher<Coffee> coffees(Throwable throwable) {
    log.error(throwable.getMessage(), throwable);
    return coffeesFlux();
  }

  public void add(Coffee coffee) {
    coffees.add(coffee);
  }
}

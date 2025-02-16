package de.derfilli.coffy.service;

import de.derfilli.coffy.service.Concepts.Coffee;
import de.derfilli.coffy.service.Concepts.CoffeeInfo;
import java.util.HashMap;
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

  public static Flux<Coffee> coffeesFlux() {
    return Flux.just(new Coffee("123", "Cached Coffee", 0.0f, 0,
        new CoffeeInfo("A fallback coffee entry", "", "", new HashMap<>())));
  }

  public static Publisher<Coffee> coffees(Throwable throwable) {
    log.error(throwable.getMessage(), throwable);
    return coffeesFlux();
  }
}

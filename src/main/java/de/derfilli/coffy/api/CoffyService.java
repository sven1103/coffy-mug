package de.derfilli.coffy.api;

import de.derfilli.coffy.api.Concepts.Account;
import de.derfilli.coffy.api.Concepts.AccountCreationRequest;
import de.derfilli.coffy.api.Concepts.Coffee;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * <b><interface short description - 1 Line!></b>
 *
 * <p><More detailed description - When to use, what it solves, etc.></p>
 *
 * @since <version tag>
 */
public interface CoffyService {

  Flux<Coffee> getCoffees();

  Flux<Account> getAccounts();

  Mono<Account> createAccount(AccountCreationRequest request);
}

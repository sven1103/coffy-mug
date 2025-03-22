package de.derfilli.coffy.api;

import de.derfilli.coffy.api.Concepts.Account;
import de.derfilli.coffy.api.Concepts.AccountCreationRequest;
import de.derfilli.coffy.api.Concepts.Coffee;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service API to interact with the coffee server (aka coffy machine).
 * <p>
 * Together with {@link Concepts}, this interface provides clients with various interaction
 * possibilities with the coffee server.
 *
 * @since 1.0.0
 */
public interface CoffyService {

  /**
   * Retrieves the coffees available in coffy.
   *
   * @return a {@link Flux} of known {@link Coffee} to coffy
   * @since 1.0.0
   */
  Flux<Coffee> getCoffees();

  /**
   * Retrieves the accounts available in coffy.
   *
   * @return a {@link Flux} of existing {@link Account} in coffy
   * @since 1.0.0
   */
  Flux<Account> getAccounts();

  /**
   * Creates a new account in coffy.
   *
   * @param request a {@link AccountCreationRequest} with information about the account creation
   *                request
   * @return a {@link Mono} emitting the newly created {@link Account} or
   * {@link Mono#error(Throwable)} with one of the described exceptions.
   * @throws AccountExistsException in case an account with the provided information already exists
   * @since 1.0.0
   */
  Mono<Account> createAccount(AccountCreationRequest request);


  class AccountExistsException extends RuntimeException {

    AccountExistsException(String message) {
      super(message);
    }
  }

}

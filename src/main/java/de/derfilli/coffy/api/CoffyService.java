package de.derfilli.coffy.api;

import de.derfilli.coffy.api.Concepts.Account;
import de.derfilli.coffy.api.Concepts.AccountCreationRequest;
import de.derfilli.coffy.api.Concepts.Coffee;
import de.derfilli.coffy.api.Concepts.PurchaseReceipt;
import de.derfilli.coffy.api.Concepts.PurchaseRequest;
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
   * @throws AccountExistsException   in case an account with the provided information already
   *                                  exists
   * @throws RequestFailedException   in case the request cannot be processed by the service
   * @throws IllegalArgumentException in case the request is invalid (e.g. null, empty information)
   * @since 1.0.0
   */
  Mono<Account> createAccount(AccountCreationRequest request);

  /**
   * Submits a purchase request to the server.
   *
   * @param request the {@link PurchaseRequest} with information about the purchase
   * @return a {@link Mono} with the {@link PurchaseReceipt} after successful transaction. In case
   * something went wrong or {@link Mono#error(Throwable)} with one of the described exceptions.
   * @throws IllegalArgumentException if the request is invalid (e.g. null, empty)
   * @throws RequestFailedException   in case the request cannot be processed by the service
   * @throws UnknownAccountException  if the account ID does not match a known account
   * @throws UnknownProductException  if the product ID does not match a known product
   * @since 1.0.0
   */
  Mono<PurchaseReceipt> purchase(PurchaseRequest request);


  class AccountExistsException extends RuntimeException {

    AccountExistsException(String message) {
      super(message);
    }
  }

  class UnknownAccountException extends RuntimeException {

    UnknownAccountException(String message) {
      super(message);
    }
  }

  class UnknownProductException extends RuntimeException {

    UnknownProductException(String message) {
      super(message);
    }
  }

  class RequestFailedException extends RuntimeException {

    public RequestFailedException(String message) {
      super(message);
    }
  }

}

package de.derfilli.coffy.api;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.time.Instant;
import java.util.Map;

/**
 * CoffyService request and response query containers to interact with the coffy backend service.
 *
 * @since 1.0.0
 */
public class Concepts {

  /**
   * A coffee representation in coffy, providing basic information about what the coffee is about,
   * like price, quality assessment and much more.
   *
   * @param id           the coffee's unique resource ID
   * @param name         the name of the coffee
   * @param price        the current coffee price per cup
   * @param cuppingScore the cupping score based on the CVA evaluation
   * @param info         a lot more about the coffee described with {@link CoffeeInfo}
   * @since 1.0.0
   */
  public record Coffee(@JsonAlias("id") String id,
                       @JsonAlias("name") String name,
                       @JsonAlias("price") Float price,
                       @JsonAlias("cupping_score") Integer cuppingScore,
                       @JsonAlias("info") CoffeeInfo info) {

  }

  /**
   * Provides more detailed information about {@link Coffee}.
   *
   * @param description detailed description from the coffee supplier about the coffee
   * @param origin      the origin of the coffee beans
   * @param roastHouse  where the beans have been roasted
   * @param misc        unstructured additional information in a simple key-value format
   * @since 1.0.0
   */
  public record CoffeeInfo(@JsonAlias("description") String description,
                           @JsonAlias("origin") String origin,
                           @JsonAlias("roast_house") String roastHouse,
                           @JsonAlias("misc") Map<String, String> misc) {

  }

  /**
   * A user's account information to give a current overview of its financial status.
   *
   * @param id            the user's unique account ID
   * @param owner         the owner of the account
   * @param balance       the current account balance
   * @param consumedTotal the amount of coffee cups consumed so far
   * @since 1.0.0
   */
  public record Account(@JsonAlias("id") String id,
                        @JsonAlias("owner") String owner,
                        @JsonAlias("balance") Float balance,
                        @JsonAlias("consumed_total") Integer consumedTotal) {

  }

  /**
   * Request to create a new account for a future owner.
   *
   * @param owner the account owner's name
   * @since 1.0.0
   */
  public record AccountCreationRequest(@JsonAlias("owner") String owner) {

  }

  /**
   * A receipt to confirm the consumption of coffee.
   *
   * @param amount    the amount of coffee purchased
   * @param date      the date of the transaction
   * @param purpose   the purpose of the transaction
   * @param recipient who received the purchase request (the seller)
   * @param submitter who submitted the purchase request (the buyer)
   * @since 1.0.0
   */
  public record PurchaseReceipt(@JsonAlias("amount") Integer amount,
                                @JsonAlias("date") Instant date,
                                @JsonAlias("purpose") String purpose,
                                @JsonAlias("recipient") String recipient,
                                @JsonAlias("submitter") String submitter) {

  }

  /**
   * A simple purchase in coffy.
   *
   * @param accountID the account ID to debit with the purchase
   * @param productID the product ID of the product purchased
   * @param quantity  the amount of the product to purchase
   * @since 1.0.0
   */
  public record PurchaseRequest(@JsonAlias("account_id") String accountID,
                                @JsonAlias("product_id") String productID,
                                @JsonAlias("quantity") Integer quantity) {

  }

}

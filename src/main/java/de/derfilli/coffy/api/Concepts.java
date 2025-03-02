package de.derfilli.coffy.api;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.Map;

/**
 * CoffyService request and response query containers to interact with the coffy backend service.
 *
 * @since 1.0.0
 */
public class Concepts {

  public record Coffee(@JsonAlias("id") String id,
                       @JsonAlias("name") String name,
                       @JsonAlias("price") Float price,
                       @JsonAlias("cupping_score") Integer cuppingScore,
                       @JsonAlias("info") CoffeeInfo info) {

  }

  public record CoffeeInfo(@JsonAlias("description") String description,
                           @JsonAlias("origin") String origin,
                           @JsonAlias("roast_house") String roastHouse,
                           @JsonAlias("misc") Map<String, String> misc) {

  }

  public record Account(@JsonAlias("id") String id,
                        @JsonAlias("owner") String owner,
                        @JsonAlias("balance") Float balance,
                        @JsonAlias("consumed_total") Integer consumedTotal) {
  }

  public record AccountCreationRequest(@JsonAlias("owner") String owner){}

}

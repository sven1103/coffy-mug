package de.derfilli.coffy.service;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.Map;

/**
 * Service request and response query containers to interact with the coffy backend service.
 *
 * @since 1.0.0
 */
public class Concepts {

  public record Coffee (@JsonAlias("id") String id,
                 @JsonAlias("name") String name,
                 @JsonAlias("price") Float price,
                 @JsonAlias("cupping_score") Integer cuppingScore,
                 @JsonAlias("info") CoffeeInfo info) {
  }

  public record CoffeeInfo (@JsonAlias("description") String description,
                     @JsonAlias("origin") String origin,
                     @JsonAlias("roast_house") String roastHouse,
                     @JsonAlias("misc") Map<String, String> misc) {

  }

}

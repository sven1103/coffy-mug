package de.derfilli.coffy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * <b><class short description - 1 Line!></b>
 *
 * <p><More detailed description - When to use, what it solves, etc.></p>
 *
 * @since <version tag>
 */
@Configuration
public class Config {

  @Bean
  public WebClient webClient() {
    return WebClient.create("http://localhost:8080/api/v1");
  }

}

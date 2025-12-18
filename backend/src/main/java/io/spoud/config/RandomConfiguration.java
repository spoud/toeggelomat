package io.spoud.config;

import java.security.SecureRandom;
import java.util.Random;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

/**
 * This is needed for native compilation
 */
@ApplicationScoped
public class RandomConfiguration {

  @Produces
  public Random random() {
    return new SecureRandom();
  }
}

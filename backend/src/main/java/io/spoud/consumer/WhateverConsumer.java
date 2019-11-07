package io.spoud.consumer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class WhateverConsumer {

  @Incoming("results")
  public CompletionStage<Void> consumeAsync(String input) {
    return CompletableFuture.runAsync(() -> {
      System.out.println(input);
    });
  }
}

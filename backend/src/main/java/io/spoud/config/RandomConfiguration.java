package io.spoud.config;

import java.security.SecureRandom;
import java.util.Random;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class RandomConfiguration {

    @Produces
    public Random random(){
        return new SecureRandom();
    }
}

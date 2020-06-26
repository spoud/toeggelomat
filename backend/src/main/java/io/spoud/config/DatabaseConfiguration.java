package io.spoud.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class DatabaseConfiguration {

  @PersistenceContext private EntityManager entityManager;

  @Produces
  public JPAQueryFactory jpaQueryFactory() {
    return new JPAQueryFactory(entityManager);
  }
}

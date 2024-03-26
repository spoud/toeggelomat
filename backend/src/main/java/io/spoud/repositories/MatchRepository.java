package io.spoud.repositories;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.spoud.entities.MatchEO;
import io.spoud.entities.QMatchEO;
import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class MatchRepository {

  public static final QMatchEO MATCH = QMatchEO.matchEO;

  @Inject JPAQueryFactory jpaQueryFactory;

  @Inject EntityManager em;

  public List<MatchEO> getLastMatches(int nb) {
    return jpaQueryFactory.selectFrom(MATCH).orderBy(MATCH.matchTime.desc()).limit(nb).fetch();
  }

  public MatchEO addMatch(MatchEO match) {
    match.setUuid(null);
    em.persist(match);
    return match;
  }
}

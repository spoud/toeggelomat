package io.spoud.repositories;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.spoud.entities.MatchEO;
import io.spoud.entities.QMatchEO;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@ApplicationScoped
public class MatchRepository {

  public static final QMatchEO MATCH = QMatchEO.matchEO;

  @Inject private JPAQueryFactory jpaQueryFactory;

  @Inject private EntityManager em;

  public List<MatchEO> getLastMatches(int nb) {
    return jpaQueryFactory.selectFrom(MATCH).orderBy(MATCH.matchTime.desc()).limit(nb).fetch();
  }

  public MatchEO addMatch(MatchEO match) {
    match.setUuid(null);
    em.persist(match);
    return match;
  }
}

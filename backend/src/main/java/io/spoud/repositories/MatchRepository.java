package io.spoud.repositories;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.spoud.entities.MatchEO;
import io.spoud.entities.QMatchEO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class MatchRepository {

  public static final QMatchEO MATCH = QMatchEO.matchEO;

  private final JPAQueryFactory jpaQueryFactory;
  private final EntityManager em;

  public List<MatchEO> getLastMatches(int nb) {
    return jpaQueryFactory.selectFrom(MATCH).orderBy(MATCH.matchTime.desc()).limit(nb).fetch();
  }

  public MatchEO addMatch(MatchEO match) {
    match.setUuid(null);
    em.persist(match);
    return match;
  }
}

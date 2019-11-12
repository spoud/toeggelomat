package io.spoud.repositories;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import io.spoud.entities.MatchEO;
import io.spoud.entities.QMatchEO;

@Repository
public class MatchRepository {

  public static final QMatchEO MATCH = QMatchEO.matchEO;

  @Autowired
  private JPAQueryFactory jpaQueryFactory;

  @Autowired
  private EntityManager em;

  public List<MatchEO> getLastMatches(int nb) {
    return jpaQueryFactory.selectFrom(MATCH).orderBy(MATCH.resultTime.desc()).limit(nb).fetch();
  }

  public MatchEO addMatch(MatchEO match){
    match.setUuid(null);
    em.persist(match);
    return match;
  }
}

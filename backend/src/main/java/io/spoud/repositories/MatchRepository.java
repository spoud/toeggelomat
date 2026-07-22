package io.spoud.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import io.spoud.entities.MatchEO;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class MatchRepository implements PanacheRepositoryBase<MatchEO, UUID> {

  public List<MatchEO> getLastMatches(UUID seasonUuid) {
    if (seasonUuid == null) {
      return findAll(Sort.descending("matchTime")).list();
    }
    return find("seasonUuid", Sort.descending("matchTime"), seasonUuid).list();
  }

  public List<MatchEO> findBySeasonOrderedByTime(UUID seasonUuid) {
    return find("seasonUuid", Sort.ascending("matchTime"), seasonUuid).list();
  }
}

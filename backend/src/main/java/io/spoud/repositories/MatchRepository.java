package io.spoud.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.spoud.entities.MatchEO;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class MatchRepository implements PanacheRepositoryBase<MatchEO, UUID> {

  public List<MatchEO> getLastMatches(int nb, UUID seasonUuid) {
    if (seasonUuid == null) {
      return findAll(Sort.descending("matchTime")).page(Page.ofSize(nb)).list();
    }
    return find("seasonUuid", Sort.descending("matchTime"), seasonUuid)
        .page(Page.ofSize(nb))
        .list();
  }
}

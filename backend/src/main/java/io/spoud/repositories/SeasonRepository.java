package io.spoud.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.spoud.entities.SeasonEO;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class SeasonRepository implements PanacheRepositoryBase<SeasonEO, UUID> {

  public Optional<SeasonEO> findActive() {
    return find("endTime is null").firstResultOptional();
  }

  public List<SeasonEO> findAllOrdered() {
    return list("ORDER BY startTime DESC");
  }
}

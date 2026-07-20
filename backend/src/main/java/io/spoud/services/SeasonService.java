package io.spoud.services;

import io.spoud.entities.SeasonEO;
import io.spoud.repositories.SeasonRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.UUID;

@ApplicationScoped
@Transactional
public class SeasonService {

  @Inject SeasonRepository seasonRepository;

  public SeasonEO createSeason(String label) {
    ZonedDateTime now = ZonedDateTime.now();
    seasonRepository
        .findActive()
        .ifPresent(
            active -> {
              active.endTime = now;
              seasonRepository.persist(active);
            });

    SeasonEO season = new SeasonEO();
    season.uuid = UUID.randomUUID();
    season.label = label;
    season.startTime = now;
    seasonRepository.persistAndFlush(season);
    return season;
  }

  public boolean closeSeason(UUID uuid) {
    SeasonEO season = seasonRepository.findById(uuid);
    if (season == null || season.endTime != null) {
      return false;
    }
    season.endTime = ZonedDateTime.now();
    seasonRepository.persistAndFlush(season);
    return true;
  }
}

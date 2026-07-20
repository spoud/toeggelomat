package io.spoud.api.data;

import io.spoud.entities.SeasonEO;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.NonNull;

import java.time.ZonedDateTime;
import java.util.UUID;

@Name("Season")
public record SeasonTO(
  @NonNull UUID uuid,
  @NonNull String label,
  @NonNull ZonedDateTime startTime,
  ZonedDateTime endTime
) {
  public static SeasonTO from(SeasonEO season) {
    return new SeasonTO(season.uuid, season.label, season.startTime, season.endTime);
  }
}

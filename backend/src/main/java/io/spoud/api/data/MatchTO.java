package io.spoud.api.data;

import io.spoud.entities.MatchEO;
import jakarta.persistence.Transient;
import org.eclipse.microprofile.graphql.Ignore;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.NonNull;

import java.time.ZonedDateTime;
import java.util.UUID;

@Name("Match")
public record MatchTO(
  @NonNull UUID uuid,
  ZonedDateTime matchTime,
  Integer redScore,
  Integer blueScore,
  Integer points,
  @Ignore UUID playerRedDefenseUuid,
  @Ignore UUID playerRedOffenseUuid,
  @Ignore UUID playerBlueDefenseUuid,
  @Ignore UUID playerBlueOffenseUuid,
  Integer potentialBluePoints,
  Integer potentialRedPoints
) {
  public static MatchTO from(MatchEO match) {
    return new MatchTO(
      match.uuid,
      match.matchTime,
      match.redScore,
      match.blueScore,
      match.points,
      match.playerRedDefenseUuid,
      match.playerRedOffenseUuid,
      match.playerBlueDefenseUuid,
      match.playerBlueOffenseUuid,
      match.potentialBluePoints,
      match.potentialRedPoints);
  }

}

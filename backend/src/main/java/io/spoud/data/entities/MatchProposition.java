package io.spoud.data.entities;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.spoud.data.definition.Match;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RegisterForReflection
public class MatchProposition implements Match {

  private UUID uuid;

  private ZonedDateTime matchTime;

  private Integer redScore;

  private Integer blueScore;

  private Integer points;

  private UUID blueDefense;

  private UUID blueOffense;

  private UUID redDefense;

  private UUID redOffense;

  private Integer potentialBluePoints;

  private Integer potentialRedPoints;

}

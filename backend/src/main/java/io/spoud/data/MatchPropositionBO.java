package io.spoud.data;

import io.quarkus.runtime.annotations.RegisterForReflection;

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
public class MatchPropositionBO implements Match {

  private UUID uuid;

  private ZonedDateTime matchTime;

  private Integer redScore;

  private Integer blueScore;

  private Integer points;

  private UUID playerBlueDefenseUuid;

  private UUID playerBlueOffenseUuid;

  private UUID playerRedDefenseUuid;

  private UUID playerRedOffenseUuid;

  private Integer potentialBluePoints;

  private Integer potentialRedPoints;

}

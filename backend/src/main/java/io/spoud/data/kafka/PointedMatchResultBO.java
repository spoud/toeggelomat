package io.spoud.data.kafka;

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
public class PointedMatchResultBO {
  private UUID matchUuid;
  private ZonedDateTime matchTime;
  private Integer redScore;
  private Integer blueScore;
  private Integer points;

  private PlayerBO winnerDefense;
  private PlayerBO winnerOffense;
  private PlayerBO loserDefense;
  private PlayerBO loserOffense;

  public static PointedMatchResultBO from(MatchResultBO other) {
    return PointedMatchResultBO.builder()
        .matchUuid(other.getMatchUuid())
        .matchTime(other.getMatchTime())
        .redScore(other.getRedScore())
        .blueScore(other.getBlueScore())
        .build();
  }
}

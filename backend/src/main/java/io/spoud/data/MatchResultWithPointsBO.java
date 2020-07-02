package io.spoud.data;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RegisterForReflection
public class MatchResultWithPointsBO {
  private UUID uuid;
  private ZonedDateTime matchTime;
  private Integer redScore;
  private Integer blueScore;
  private Integer points;

  private UUID playerBlueDefenseUuid;
  private UUID playerBlueOffenseUuid;
  private UUID playerRedDefenseUuid;
  private UUID playerRedOffenseUuid;

  // this helps do the computation in ksql
  private UUID playerWinnerDefenseUuid;
  private UUID playerWinnerOffenseUuid;
  private UUID playerLoserDefenseUuid;
  private UUID playerLoserOffenseUuid;

  public static MatchResultWithPointsBO from(MatchResultBO other) {
    return MatchResultWithPointsBO.builder()
        .uuid(other.getUuid())
        .matchTime(other.getMatchTime())
        .redScore(other.getRedScore())
        .blueScore(other.getBlueScore())
        .playerBlueDefenseUuid(other.getPlayerBlueDefenseUuid())
        .playerBlueOffenseUuid(other.getPlayerBlueOffenseUuid())
        .playerRedDefenseUuid(other.getPlayerRedDefenseUuid())
        .playerRedOffenseUuid(other.getPlayerRedOffenseUuid())
        .build();
  }

  public static class Deserializer extends ObjectMapperDeserializer<MatchResultWithPointsBO> {
    public Deserializer() {
      super(MatchResultWithPointsBO.class);
    }
  }
}

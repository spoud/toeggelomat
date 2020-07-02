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
public class MatchResultBO implements Match {

  private UUID uuid;
  private ZonedDateTime matchTime;
  private Integer redScore;
  private Integer blueScore;
  private Integer points;

  private UUID playerBlueDefenseUuid;
  private UUID playerBlueOffenseUuid;
  private UUID playerRedDefenseUuid;
  private UUID playerRedOffenseUuid;

  public static class Deserializer extends ObjectMapperDeserializer<MatchResultBO> {
    public Deserializer(){
      super(MatchResultBO.class);
    }
  }
}

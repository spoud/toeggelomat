package io.spoud.data.kafka;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.spoud.data.definition.Match;
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

  private UUID blueDefense;
  private UUID blueOffense;
  private UUID redDefense;
  private UUID redOffense;

  public static class Deserializer extends ObjectMapperDeserializer<MatchResultBO> {
    public Deserializer(){
      super(MatchResultBO.class);
    }
  }
}

package io.spoud.data;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class PointChangesBO {
  @JsonProperty("UUID")
  private UUID matchUuid;

  @JsonProperty("POINTSDEFENSE")
  private int pointsDefense;

  @JsonProperty("POINTSOFFENSE")
  private int pointsOffense;

  @JsonProperty("MATCHTIME")
  private ZonedDateTime matchTime;

  @JsonProperty("PLAYERUUID")
  private UUID playerUuid;

  public static class Deserializer extends ObjectMapperDeserializer<PointChangesBO> {
    public Deserializer() {
      super(PointChangesBO.class);
    }
  }
}

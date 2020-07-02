package io.spoud.data.kafka;

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
public class PlayerBO {

  private UUID uuid;
  private String nickName;
  private String email;
  private Integer defensePoints;
  private Integer offensePoints;
  private ZonedDateTime lastMatchTime;

  public static class Deserializer extends ObjectMapperDeserializer<PlayerBO> {
    public Deserializer() {
      super(PlayerBO.class);
    }
  }
}

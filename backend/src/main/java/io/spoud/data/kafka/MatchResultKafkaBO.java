package io.spoud.data.kafka;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.spoud.data.entities.PlayerEO;
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
public class MatchResultKafkaBO {

  private UUID matchUuid;
  private ZonedDateTime matchTime;
  private Integer redScore;
  private Integer blueScore;
  private Integer points;

  private PlayerEO blueDefense;
  private PlayerEO blueOffense;
  private PlayerEO redDefense;
  private PlayerEO redOffense;
}

package io.spoud.data.kafka;

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
public class MatchResultKafkaBO {

  private UUID matchUuid;
  private ZonedDateTime matchTime;
  private Integer redScore;
  private Integer blueScore;
  private Integer points;

  private UUID blueDefense;
  private UUID blueOffense;
  private UUID redDefense;
  private UUID redOffense;
}

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
public class Player {

  private UUID uuid;
  private String nickName;
  private String email;
  private String slackId;
  private Integer defensePoints;
  private Integer offensePoints;
  private ZonedDateTime lastMatchTime;
}

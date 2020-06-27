package io.spoud.data.kafka;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.spoud.data.entities.PlayerEO;
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
public class PlayerBO {
  private UUID uuid;
  private String nickName;
  private String email;
  private String slackId;
  private Integer defensePoints;
  private Integer offensePoints;

  public static PlayerBO from(PlayerEO other) {
    return PlayerBO.builder()
        .uuid(other.getUuid())
        .email(other.getEmail())
        .nickName(other.getNickName())
        .slackId(other.getNickName())
        .offensePoints(other.getOffensePoints())
        .defensePoints(other.getDefensePoints())
        .build();
  }
}

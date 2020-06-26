package io.spoud.producer;

import io.quarkus.runtime.annotations.RegisterForReflection;
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
}

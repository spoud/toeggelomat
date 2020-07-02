package io.spoud.data;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface Match {
  UUID getUuid();

  ZonedDateTime getMatchTime();

  Integer getRedScore();

  Integer getBlueScore();

  UUID getPlayerBlueDefenseUuid();

  UUID getPlayerBlueOffenseUuid();

  UUID getPlayerRedDefenseUuid();

  UUID getPlayerRedOffenseUuid();
}

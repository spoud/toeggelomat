package io.spoud.data;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface Match extends MatchResult{
  UUID getUuid();

  ZonedDateTime getMatchTime();

  Integer getRedScore();

  Integer getBlueScore();

  UUID getPlayerBlueDefenseUuid();

  UUID getPlayerBlueOffenseUuid();

  UUID getPlayerRedDefenseUuid();

  UUID getPlayerRedOffenseUuid();
}

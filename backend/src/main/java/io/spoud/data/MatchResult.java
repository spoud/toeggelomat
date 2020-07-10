package io.spoud.data;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface MatchResult {
  UUID getUuid();

  ZonedDateTime getMatchTime();

  Integer getRedScore();

  Integer getBlueScore();
}

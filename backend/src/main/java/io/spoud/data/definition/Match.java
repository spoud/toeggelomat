package io.spoud.data.definition;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface Match {
  UUID getUuid();

  ZonedDateTime getMatchTime();

  Integer getRedScore();

  Integer getBlueScore();

  UUID getBlueDefense();

  UUID getBlueOffense();

  UUID getRedDefense();

  UUID getRedOffense();
}

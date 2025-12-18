package io.spoud.api.data;

import io.spoud.entities.PlayerEO;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.NonNull;

import java.time.ZonedDateTime;
import java.util.UUID;

@Name("Player")
public record PlayerTO(
  @NonNull UUID uuid,
  @NonNull String nickName,
  int defensePoints,
  int offensePoints,
  ZonedDateTime lastMatchTime
) {
  public static PlayerTO from(PlayerEO player) {
    return new PlayerTO(player.uuid, player.nickName, player.defensePoints, player.offensePoints, player.lastMatchTime);
  }
}

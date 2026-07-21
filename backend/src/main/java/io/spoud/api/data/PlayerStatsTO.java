package io.spoud.api.data;

import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.NonNull;

import java.util.UUID;

@Name("PlayerStats")
public record PlayerStatsTO(
  @NonNull UUID uuid,
  @NonNull String nickName,
  int wins,
  int losses,
  Double winRate,
  int currentStreak,
  int goalDifference,
  int offensePoints,
  int defensePoints
) {
}

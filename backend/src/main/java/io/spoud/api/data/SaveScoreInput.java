package io.spoud.api.data;

import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.NonNull;

import java.util.UUID;

@Name("SaveScoreInput")
public record SaveScoreInput(
  int redScore,
  int blueScore,
  @NonNull UUID playerRedDefenseUuid,
  @NonNull UUID playerRedOffenseUuid,
  @NonNull UUID playerBlueDefenseUuid,
  @NonNull UUID playerBlueOffenseUuid

) {

}

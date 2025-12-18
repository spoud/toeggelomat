package io.spoud.api.data;

import org.eclipse.microprofile.graphql.Ignore;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.NonNull;

import java.util.UUID;

@Name("Team")
public record TeamTO(
  @NonNull @Ignore UUID playerDefenseUuid,
  @NonNull @Ignore UUID playerOffenseUuid
) {
}

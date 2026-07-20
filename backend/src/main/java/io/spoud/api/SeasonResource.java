package io.spoud.api;

import io.spoud.api.data.SeasonTO;
import io.spoud.repositories.SeasonRepository;
import io.spoud.services.SeasonService;
import jakarta.inject.Inject;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Query;

import java.util.List;
import java.util.UUID;

@GraphQLApi
public class SeasonResource {

  @Inject SeasonRepository seasonRepository;

  @Inject SeasonService seasonService;

  @Query("allSeasons")
  public @NonNull List<@NonNull SeasonTO> allSeasons() {
    return seasonRepository.findAllOrdered().stream().map(SeasonTO::from).toList();
  }

  @Mutation("createSeason")
  public @NonNull SeasonTO createSeason(@NonNull String label) {
    return SeasonTO.from(seasonService.createSeason(label));
  }

  @Mutation("closeSeason")
  public @NonNull Boolean closeSeason(@NonNull UUID uuid) {
    return seasonService.closeSeason(uuid);
  }
}

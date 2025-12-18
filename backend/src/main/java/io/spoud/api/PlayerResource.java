package io.spoud.api;

import io.spoud.api.data.MatchTO;
import io.spoud.api.data.PlayerTO;
import io.spoud.api.data.TeamTO;
import io.spoud.entities.PlayerEO;
import io.spoud.repositories.PlayerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.graphql.Source;

import java.util.List;

@GraphQLApi
public class PlayerResource {

  @Inject PlayerRepository playerRepository;

  @Query("allPlayers")
  public @NonNull List<@NonNull PlayerTO> findAll() {
    return playerRepository.findAll().list().stream().map(PlayerTO::from).toList();
  }

  public @NonNull PlayerTO offensePlayer(@Source @NonNull TeamTO teamTO) {
    return PlayerTO.from(playerRepository.findById(teamTO.playerOffenseUuid()));
  }

  public @NonNull PlayerTO defensePlayer(@Source @NonNull TeamTO teamTO) {
    return PlayerTO.from(playerRepository.findById(teamTO.playerDefenseUuid()));
  }

}

package io.spoud.api;

import io.spoud.api.data.MatchTO;
import io.spoud.api.data.PlayerTO;
import io.spoud.api.data.TeamTO;
import io.spoud.entities.PlayerEO;
import io.spoud.repositories.PlayerRepository;
import io.spoud.services.PlayerService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.graphql.Source;

import java.util.List;
import java.util.UUID;

@GraphQLApi
public class PlayerResource {

  @Inject PlayerRepository playerRepository;

  @Inject PlayerService playerService;

  @Query("allPlayers")
  public @NonNull List<@NonNull PlayerTO> findAll() {
    return playerRepository.findAllActive().stream().map(PlayerTO::from).toList();
  }

  @Mutation("createPlayer")
  public @NonNull PlayerTO createPlayer(@NonNull String nickName) {
    return PlayerTO.from(playerService.createPlayer(nickName));
  }

  @Mutation("deletePlayer")
  public @NonNull Boolean deletePlayer(@NonNull UUID uuid) {
    return playerService.archivePlayer(uuid);
  }

  public @NonNull PlayerTO offensePlayer(@Source @NonNull TeamTO teamTO) {
    return PlayerTO.from(playerRepository.findById(teamTO.playerOffenseUuid()));
  }

  public @NonNull PlayerTO defensePlayer(@Source @NonNull TeamTO teamTO) {
    return PlayerTO.from(playerRepository.findById(teamTO.playerDefenseUuid()));
  }

}

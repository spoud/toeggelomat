package io.spoud.api;

import io.spoud.api.data.MatchTO;
import io.spoud.api.data.SaveScoreInput;
import io.spoud.api.data.TeamTO;
import io.spoud.services.MatchService;
import jakarta.inject.Inject;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.graphql.Source;

import java.util.List;
import java.util.UUID;

@GraphQLApi
public class MatchResource {
  @Inject
  MatchService matchService;

  @Mutation("saveScore")
  public @NonNull MatchTO finishMatch(@NonNull SaveScoreInput scores) {
    return MatchTO.from(matchService.saveMatchResults(scores));
  }

  @Mutation("randomizeMatch")
  public @NonNull MatchTO startMatchWithPlayers(@NonNull List<@NonNull UUID> players) {
    return MatchTO.from(matchService.randomizeMatch(players));
  }

  @Query("lastMatches")
  public @NonNull List<@NonNull MatchTO> lastMaches() {
    return matchService.getLastMatchOfTheSeason().stream().map(MatchTO::from).toList();
  }

  public @NonNull TeamTO redTeam(@Source @NonNull MatchTO match) {
    return new TeamTO(match.playerRedDefenseUuid(), match.playerRedOffenseUuid());
  }

  public @NonNull TeamTO blueTeam(@Source @NonNull MatchTO match) {
    return new TeamTO(match.playerBlueDefenseUuid(), match.playerBlueOffenseUuid());
  }


}

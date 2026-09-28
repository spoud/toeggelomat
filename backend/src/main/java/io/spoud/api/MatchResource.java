package io.spoud.api;

import io.spoud.api.data.MatchTO;
import io.spoud.api.data.SaveScoreInput;
import io.spoud.api.data.TeamTO;
import io.spoud.services.MatchService;
import jakarta.inject.Inject;
import org.eclipse.microprofile.graphql.DefaultValue;
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

  private static final int DEFAULT_LAST_MATCHES_LIMIT = 20;
  private static final int MAX_LAST_MATCHES_LIMIT = 200;

  // limit/offset are boxed so an explicit null from a client falls back to the default
  // instead of failing; out-of-range values are clamped rather than producing an invalid range.
  @Query("lastMatches")
  public @NonNull List<@NonNull MatchTO> lastMaches(
      UUID seasonUuid,
      @DefaultValue("20") Integer limit,
      @DefaultValue("0") Integer offset) {
    int clampedLimit =
        Math.clamp(limit == null ? DEFAULT_LAST_MATCHES_LIMIT : limit, 1, MAX_LAST_MATCHES_LIMIT);
    int clampedOffset = Math.max(offset == null ? 0 : offset, 0);
    return matchService.getLastMatches(seasonUuid, clampedLimit, clampedOffset).stream()
        .map(MatchTO::from)
        .toList();
  }

  public @NonNull TeamTO redTeam(@Source @NonNull MatchTO match) {
    return new TeamTO(match.playerRedDefenseUuid(), match.playerRedOffenseUuid());
  }

  public @NonNull TeamTO blueTeam(@Source @NonNull MatchTO match) {
    return new TeamTO(match.playerBlueDefenseUuid(), match.playerBlueOffenseUuid());
  }


}

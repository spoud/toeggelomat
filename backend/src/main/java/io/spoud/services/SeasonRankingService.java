package io.spoud.services;

import io.spoud.entities.MatchEO;
import io.spoud.repositories.MatchRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Reconstructs player standings for a given season by replaying its matches in order, starting
 * every participant at the same baseline used when a season starts. This intentionally does not
 * read PlayerEO.offensePoints/defensePoints, since those are a single live running total that
 * gets reset at the start of each new season and would only reflect the current season.
 */
@ApplicationScoped
public class SeasonRankingService {

  private static final int STARTING_POINTS = 500;

  @Inject MatchRepository matchRepository;

  public static class RankedPlayer {
    public final UUID uuid;
    public int offensePoints = STARTING_POINTS;
    public int defensePoints = STARTING_POINTS;
    public ZonedDateTime lastMatchTime;
    public int wins = 0;
    public int losses = 0;
    public int goalDifference = 0;

    /** Positive = current win streak, negative = current loss streak, 0 = no matches yet. */
    public int currentStreak = 0;

    public RankedPlayer(UUID uuid) {
      this.uuid = uuid;
    }

    void applyOutcome(boolean won, int goalDiff) {
      goalDifference += goalDiff;
      if (won) {
        wins++;
        currentStreak = currentStreak >= 0 ? currentStreak + 1 : 1;
      } else {
        losses++;
        currentStreak = currentStreak <= 0 ? currentStreak - 1 : -1;
      }
    }
  }

  public List<RankedPlayer> computeRanking(UUID seasonUuid) {
    List<MatchEO> matches = matchRepository.findBySeasonOrderedByTime(seasonUuid);
    Map<UUID, RankedPlayer> ranking = new LinkedHashMap<>();

    for (MatchEO match : matches) {
      RankedPlayer blueDefense =
          ranking.computeIfAbsent(match.playerBlueDefenseUuid, RankedPlayer::new);
      RankedPlayer blueOffense =
          ranking.computeIfAbsent(match.playerBlueOffenseUuid, RankedPlayer::new);
      RankedPlayer redDefense =
          ranking.computeIfAbsent(match.playerRedDefenseUuid, RankedPlayer::new);
      RankedPlayer redOffense =
          ranking.computeIfAbsent(match.playerRedOffenseUuid, RankedPlayer::new);

      boolean wonByBlue =
          match.redScore != null && match.blueScore != null
              ? match.blueScore > match.redScore
              : true;

      RankedPlayer winnerDefense = wonByBlue ? blueDefense : redDefense;
      RankedPlayer winnerOffense = wonByBlue ? blueOffense : redOffense;
      RankedPlayer looserDefense = wonByBlue ? redDefense : blueDefense;
      RankedPlayer looserOffense = wonByBlue ? redOffense : blueOffense;

      int winnerTotal = winnerDefense.defensePoints + winnerOffense.offensePoints;
      int looserTotal = looserDefense.defensePoints + looserOffense.offensePoints;
      boolean sevenToZero = MatchPointsService.isSevenToZero(match);
      int points = MatchPointsService.calcPoints(winnerTotal, looserTotal, sevenToZero);

      winnerDefense.defensePoints += points + MatchPointsService.ADDITIONAL_POINT_FOR_WINNING;
      winnerOffense.offensePoints += points + MatchPointsService.ADDITIONAL_POINT_FOR_WINNING;
      looserDefense.defensePoints += -points + MatchPointsService.ADDITIONAL_POINT_FOR_LOSING;
      looserOffense.offensePoints += -points + MatchPointsService.ADDITIONAL_POINT_FOR_LOSING;

      int blueGoalDiff =
          match.blueScore != null && match.redScore != null ? match.blueScore - match.redScore : 0;
      blueDefense.applyOutcome(wonByBlue, blueGoalDiff);
      blueOffense.applyOutcome(wonByBlue, blueGoalDiff);
      redDefense.applyOutcome(!wonByBlue, -blueGoalDiff);
      redOffense.applyOutcome(!wonByBlue, -blueGoalDiff);

      blueDefense.lastMatchTime = match.matchTime;
      blueOffense.lastMatchTime = match.matchTime;
      redDefense.lastMatchTime = match.matchTime;
      redOffense.lastMatchTime = match.matchTime;
    }

    return new ArrayList<>(ranking.values());
  }
}

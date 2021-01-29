package io.spoud.services;

import io.spoud.data.Match;
import io.spoud.data.MatchPropositionBO;
import io.spoud.data.MatchResult;
import io.spoud.data.MatchResultBO;
import io.spoud.data.MatchResultWithPointsBO;
import io.spoud.data.PlayerBO;
import io.spoud.repositories.PlayerRepository;
import java.util.Arrays;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class MatchPointsService {

  public static final int ADDITIONAL_POINT_FOR_PLAYING = 1;

  @Inject
  PlayerRepository playerRepository;

  public MatchResultWithPointsBO computePoints(MatchResultBO matchEO) {
    MatchResultWithPointsBO resultBO = MatchResultWithPointsBO.from(matchEO);
    PlayersHelper playersHelper = new PlayersHelper(playerRepository, matchEO);

    resultBO.setPlayerWinnerDefenseUuid(playersHelper.getWinnerDefense().getUuid());
    resultBO.setPlayerWinnerOffenseUuid(playersHelper.getWinnerOffense().getUuid());
    resultBO.setPlayerLoserDefenseUuid(playersHelper.getLooserDefense().getUuid());
    resultBO.setPlayerLoserOffenseUuid(playersHelper.getLooserOffense().getUuid());

    int points = calcPoints(playersHelper);
    resultBO.setPoints(points);
    return resultBO;
  }

  public MatchPropositionBO computePotentialPoints(MatchPropositionBO matchPropositionBO) {
    PlayersHelper playersHelper = new PlayersHelper(playerRepository, matchPropositionBO);
    playersHelper.setWonByBlue(true);
    matchPropositionBO.setPotentialBluePoints(calcPoints(playersHelper));
    playersHelper.setWonByBlue(false);
    matchPropositionBO.setPotentialRedPoints(calcPoints(playersHelper));
    return matchPropositionBO;
  }

  private static final double LI_POINTS_SLOPE = 4;
  private static final double LI_POINTS_OFFSET = -1.5;

  private static final int BASE_POINTS = 40;

  private int calcPoints(PlayersHelper playersHelper) {
    double factor = 1.0;
    factor *= zeroMultiplier(playersHelper.getMatch());
    factor *= birthdayMultiplier(playersHelper.getWinnerOffense());
    factor *= birthdayMultiplier(playersHelper.getWinnerDefense());
    factor *= birthdayMultiplier(playersHelper.getLooserOffense());
    factor *= birthdayMultiplier(playersHelper.getLooserDefense());
    double winnerPoints =
      playersHelper.getWinnerDefense().getDefensePoints()
        + playersHelper.getWinnerOffense().getOffensePoints();
    double looserPoints =
      playersHelper.getLooserDefense().getDefensePoints()
        + playersHelper.getLooserOffense().getOffensePoints();
    double total = winnerPoints + looserPoints;
    double slope = slope(looserPoints, total);
    return (int) Math.round(slope * factor * BASE_POINTS);
  }

  private static double clamp(double d) {
    return Math.max(Math.min(d, 1.0), 0);
  }

  public static double slope(double looserPoints, double totalPoints) {
    return clamp(
      LI_POINTS_OFFSET + LI_POINTS_SLOPE * (double) (looserPoints) / (double) (totalPoints));
  }

  private double birthdayMultiplier(PlayerBO player) {
    return 1; // TODO add birthdays rules
  }

  private double zeroMultiplier(MatchResult match) {
    return match.getBlueScore() != null
      && match.getRedScore() != null
      && (match.getBlueScore() == 0 || match.getRedScore() == 0)
      ? 2
      : 1;
  }

  @Getter
  public static class PlayersHelper {

    private final MatchResult match;
    private final PlayerBO blueOffense;
    private final PlayerBO blueDefense;
    private final PlayerBO redOffense;
    private final PlayerBO redDefense;

    @Setter
    private boolean wonByBlue;

    public PlayersHelper(PlayerRepository playerRepository, Match match) {
      this.match = match;
      blueDefense = playerRepository.findByUuid(match.getPlayerBlueDefenseUuid());
      blueOffense = playerRepository.findByUuid(match.getPlayerBlueOffenseUuid());
      redDefense = playerRepository.findByUuid(match.getPlayerRedDefenseUuid());
      redOffense = playerRepository.findByUuid(match.getPlayerRedOffenseUuid());
      this.wonByBlue =
        match.getRedScore() == null
          || match.getBlueScore() == null
          || match.getBlueScore() > match.getRedScore();
    }

    public PlayersHelper(PlayerRepository playerRepository, MatchResultBO match) {
      this.match = match;
      blueDefense = match.getPlayerBlueDefense();
      blueOffense = match.getPlayerBlueOffense();
      redDefense = match.getPlayerRedDefense();
      redOffense = match.getPlayerRedOffense();
      this.wonByBlue =
        match.getRedScore() == null
          || match.getBlueScore() == null
          || match.getBlueScore() > match.getRedScore();
    }

    public PlayerBO getWinnerDefense() {
      return wonByBlue ? blueDefense : redOffense;
    }

    public PlayerBO getWinnerOffense() {
      return wonByBlue ? blueOffense : redOffense;
    }

    public PlayerBO getLooserDefense() {
      return !wonByBlue ? blueDefense : redDefense;
    }

    public PlayerBO getLooserOffense() {
      return !wonByBlue ? blueOffense : redOffense;
    }

    public List<PlayerBO> getAll() {
      return Arrays.asList(blueDefense, blueOffense, redDefense, redOffense);
    }
  }
}

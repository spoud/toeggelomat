package io.spoud.services;

import io.spoud.entities.MatchEO;
import io.spoud.entities.PlayerEO;
import io.spoud.repositories.PlayerRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class MatchPointsService {

  public static final int ADDITIONAL_POINT_FOR_PLAYING = 1;

  @Inject
  private PlayerRepository playerRepository;

  public MatchEO computePointsAndUpdatePlayers(MatchEO matchEO) {
    PlayersHelper playersHelper = new PlayersHelper(playerRepository, matchEO);
    int points = computePotentialPoints(playersHelper);
    if (matchEO.getRedScore() == 0 || matchEO.getBlueScore() == 0) {
      // double the points if game is win/lost to zero
      points *= 2;
    }

    playersHelper.getWinnerDeffence().setDefensePoints(playersHelper.getWinnerDeffence().getDefensePoints() + points + ADDITIONAL_POINT_FOR_PLAYING);
    playersHelper.getWinnerOffense().setOffensePoints(playersHelper.getWinnerOffense().getOffensePoints() + points + ADDITIONAL_POINT_FOR_PLAYING);
    playersHelper.getLooserDeffence().setDefensePoints(playersHelper.getLooserDeffence().getDefensePoints() - points + ADDITIONAL_POINT_FOR_PLAYING);
    playersHelper.getLooserOffense().setOffensePoints(playersHelper.getLooserOffense().getOffensePoints() - points + ADDITIONAL_POINT_FOR_PLAYING);
    playersHelper.getAll().forEach(p -> playerRepository.updatePointsOfPlayer(p));

    matchEO.setPoints(points);
    return matchEO;
  }

  public MatchEO computePotentialPoints(MatchEO matchEO) {
    PlayersHelper playersHelper = new PlayersHelper(playerRepository, matchEO);
    int points = computePotentialPoints(playersHelper);
    matchEO.setPoints(points);
    return matchEO;
  }

  private int computePotentialPoints(PlayersHelper playersHelper) {
    double winnerPoints =
      playersHelper.getWinnerDeffence().getDefensePoints() + playersHelper.getWinnerOffense()
        .getOffensePoints();
    double looserPoints =
      playersHelper.getLooserDeffence().getDefensePoints() + playersHelper.getLooserOffense()
        .getOffensePoints();
    double total = winnerPoints + looserPoints;
    int point = (int) ((winnerPoints / total) * 40);
    return point;
  }

  @Getter
  public class PlayersHelper {
    private final PlayerEO blueDeffense;
    private final PlayerEO blueOffense;
    private final PlayerEO redDeffense;
    private final PlayerEO redOffense;
    private final MatchEO match;
    private final boolean wonByBlue;

    public PlayersHelper(PlayerRepository playerRepository, MatchEO match) {
      this.match = match;
      this.wonByBlue = match.getRedScore() != null && match.getBlueScore() != null ?
        match.getBlueScore() > match.getRedScore() : true;
      blueDeffense = playerRepository.findByUuid(match.getPlayerBlueDefenseUuid()).get();
      blueOffense = playerRepository.findByUuid(match.getPlayerBlueOffenseUuid()).get();
      redDeffense = playerRepository.findByUuid(match.getPlayerRedDefenseUuid()).get();
      redOffense = playerRepository.findByUuid(match.getPlayerRedOffenseUuid()).get();
    }

    public PlayerEO getWinnerDeffence() {
      return wonByBlue ? blueDeffense : redDeffense;
    }

    public PlayerEO getWinnerOffense() {
      return wonByBlue ? blueOffense : redOffense;
    }

    public PlayerEO getLooserDeffence() {
      return !wonByBlue ? blueDeffense : redDeffense;
    }

    public PlayerEO getLooserOffense() {
      return !wonByBlue ? blueOffense : redOffense;
    }

    public List<PlayerEO> getAll() {
      return Arrays.asList(blueDeffense, blueOffense, redDeffense, redOffense);
    }
  }
}

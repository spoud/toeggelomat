package io.spoud.services;

import io.spoud.entities.MatchEO;
import io.spoud.entities.PlayerEO;
import io.spoud.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PointsCalculationService {
  static private final double LI_POINTS_SLOPE = 4;
  static private final double LI_POINTS_OFFSET = -1.5;

  static private final int BASE_POINTS = 40;

  public int calc(MatchEO match,
                    PlayerEO winnerOffense,
                    PlayerEO winnerDefense,
                    PlayerEO looserOffense,
                    PlayerEO looserDefense) {
    System.out.println("START CALC");
    double factor = 1.0;
    factor *= zeroMultiplier(match);
    factor *= birthdayMultiplier(winnerOffense);
    factor *= birthdayMultiplier(winnerDefense);
    factor *= birthdayMultiplier(looserOffense);
    factor *= birthdayMultiplier(looserDefense);
    double winnerPoints = winnerDefense.getDefensePoints() + winnerOffense.getOffensePoints();
    double looserPoints = looserDefense.getDefensePoints() + looserOffense.getOffensePoints();
    double total = winnerPoints + looserPoints;
    double slope = slope(looserPoints, total);
    return (int) (slope * factor * BASE_POINTS);
  }

  private static double clamp(double d) {
    return Math.max(Math.min(d, 1.0), 0);
  }

  public static double slope(double looserPoints, double totalPoints){
    return clamp(
      LI_POINTS_OFFSET +
        LI_POINTS_SLOPE *
          (double) (looserPoints) /
          (double) (totalPoints));
  }

  private double birthdayMultiplier(PlayerEO player){
    return 1; // add birthdays
  }

  private double zeroMultiplier(MatchEO match){
    return match.getBlueScore()==0 || match.getRedScore()==0? 2:1;
  }
}

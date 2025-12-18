package io.spoud.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "t_match")
@RegisterForReflection
public class MatchEO extends PanacheEntityBase {

  @Id
//  @GeneratedValue(generator = "uuid")
  //  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "match_uuid", unique = true)
  public UUID uuid;

  @Column(name = "match_time", nullable = false)
  public ZonedDateTime matchTime;

  @Column(name = "red_score", nullable = false)
  public Integer redScore;

  @Column(name = "blue_score", nullable = false)
  public Integer blueScore;

  @Column(name = "points", nullable = false)
  public Integer points;

  @Column(name = "player_red_defense_uuid", nullable = false)
  public UUID playerRedDefenseUuid;

  @Column(name = "player_red_offense_uuid", nullable = false)
  public UUID playerRedOffenseUuid;

  @Column(name = "player_blue_defense_uuid", nullable = false)
  public UUID playerBlueDefenseUuid;

  @Column(name = "player_blue_offense_uuid", nullable = false)
  public UUID playerBlueOffenseUuid;

  @Transient public Integer potentialBluePoints;

  @Transient public Integer potentialRedPoints;

  public static MatchEO newMatch(
      UUID playerRedDefenseUuid,
      UUID playerRedOffenseUuid,
      UUID playerBlueDefenseUuid,
      UUID playerBlueOffenseUuid) {
    MatchEO match = new MatchEO();
    match.uuid = UUID.randomUUID();
    match.playerRedDefenseUuid = playerRedDefenseUuid;
    match.playerRedOffenseUuid = playerRedOffenseUuid;
    match.playerBlueDefenseUuid = playerBlueDefenseUuid;
    match.playerBlueOffenseUuid = playerBlueOffenseUuid;
    return match;
  }
}

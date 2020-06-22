package io.spoud.entities;

import java.time.ZonedDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_match")
@RegisterForReflection
public class MatchEO {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "match_uuid", unique = true)
  private UUID uuid;

  @Column(name = "match_time", nullable = false)
  private ZonedDateTime matchTime;

  @Column(name = "red_score", nullable = false)
  private Integer redScore;

  @Column(name = "blue_score", nullable = false)
  private Integer blueScore;

  @Column(name = "points", nullable = false)
  private Integer points;

  @Column(name = "player_red_defense_uuid", nullable = false)
  private UUID playerRedDefenseUuid;

  @Column(name = "player_red_offense_uuid", nullable = false)
  private UUID playerRedOffenseUuid;

  @Column(name = "player_blue_defense_uuid", nullable = false)
  private UUID playerBlueDefenseUuid;

  @Column(name = "player_blue_offense_uuid", nullable = false)
  private UUID playerBlueOffenseUuid;

  @Transient
  private Integer potentialBluePoints;

  @Transient
  private Integer potentialRedPoints;


}

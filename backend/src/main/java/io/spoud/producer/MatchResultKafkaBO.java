package io.spoud.producer;

import java.time.ZonedDateTime;
import java.util.UUID;

import javax.persistence.Column;

import io.spoud.entities.MatchEO;
import io.spoud.entities.PlayerEO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchResultKafkaBO {

  private UUID matchUuid;
  private ZonedDateTime matchTime;
  private Integer redScore;
  private Integer blueScore;
  private Integer points;

  private PlayerEO blueDeffenseBefore;
  private PlayerEO blueOffenseBefore;
  private PlayerEO redDeffenseBefore;
  private PlayerEO redOffenseBefore;

  private PlayerEO blueDeffenseAfter;
  private PlayerEO blueOffenseAfter;
  private PlayerEO redDeffenseAfter;
  private PlayerEO redOffenseAfter;
}

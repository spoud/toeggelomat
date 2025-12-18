package io.spoud.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "t_player")
@RegisterForReflection
public class PlayerEO extends PanacheEntityBase implements Cloneable {
  @Id
//  @GeneratedValue(generator = "uuid")
//  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "player_uuid", unique = true)
  public UUID uuid;

  @Column(name = "nick_name", length = 100, nullable = false)
  public String nickName;

  @Column(name = "email", length = 100, nullable = false)
  public String email;

  @Column(name = "defense_points", nullable = false)
  public Integer defensePoints;

  @Column(name = "offense_points", nullable = false)
  public Integer offensePoints;

  @Column(name = "last_match_time", nullable = true)
  public ZonedDateTime lastMatchTime;

  public PlayerEO clone() {
    PlayerEO player = new PlayerEO();
    player.uuid = uuid;
    player.nickName = nickName;
    player.email = email;
    player.defensePoints = defensePoints;
    player.offensePoints = offensePoints;
    player.lastMatchTime = lastMatchTime;
    return player;
  }
}

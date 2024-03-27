package io.spoud.entities;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.time.ZonedDateTime;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_player")
@RegisterForReflection
public class PlayerEO implements Cloneable {
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "player_uuid", unique = true)
  private UUID uuid;

  @Column(name = "nick_name", length = 100, nullable = false)
  private String nickName;

  @Column(name = "email", length = 100, nullable = false)
  private String email;

  @Column(name = "defense_points", nullable = false)
  private Integer defensePoints;

  @Column(name = "offense_points", nullable = false)
  private Integer offensePoints;

  @Column(name = "last_match_time", nullable = true)
  private ZonedDateTime lastMatchTime;

  public PlayerEO clone() {
    return PlayerEO.builder()
        .uuid(uuid)
        .nickName(nickName)
        .email(email)
        .defensePoints(defensePoints)
        .offensePoints(offensePoints)
        .lastMatchTime(lastMatchTime)
        .build();
  }
}

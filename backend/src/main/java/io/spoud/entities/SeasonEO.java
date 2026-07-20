package io.spoud.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "t_season")
@RegisterForReflection
public class SeasonEO extends PanacheEntityBase {
  @Id
  @Column(name = "season_uuid", unique = true)
  public UUID uuid;

  @Column(name = "label", length = 100, nullable = false)
  public String label;

  @Column(name = "start_time", nullable = false)
  public ZonedDateTime startTime;

  @Column(name = "end_time", nullable = true)
  public ZonedDateTime endTime;
}

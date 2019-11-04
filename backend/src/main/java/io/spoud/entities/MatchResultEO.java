package io.spoud.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_match_result")
public class MatchResultEO {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "match_result_uuid", unique = true)
    private UUID uuid;

    @Column(name = "resultTime", nullable = false)
    private ZonedDateTime resultTime;

    @Column(name = "redScore", nullable = false)
    private Integer redScore;

    @Column(name = "blueScore", nullable = false)
    private Integer blueScore;

    @Column(name = "point", nullable = false)
    private Integer point;

    @Column(name = "player_red_defense_uuid", nullable = false)
    private UUID playerRedDefenseUuid;

    @Column(name = "player_red_offense_uuid", nullable = false)
    private UUID playerRedOffenseUuid;

    @Column(name = "player_blue_defense_uuid", nullable = false)
    private UUID playerBlueDefenseUuid;

    @Column(name = "player_blue_offense_uuid", nullable = false)
    private UUID playerBlueOffenseUuid;
}

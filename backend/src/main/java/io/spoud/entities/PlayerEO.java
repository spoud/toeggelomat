package io.spoud.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_player")
public class PlayerEO {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "player_uuid", unique = true)
    private UUID uuid;

    @Column(name = "nick_name", length = 100, nullable = false)
    private String nickName;

    @Column(name = "points", nullable = false)
    private Integer points;
}

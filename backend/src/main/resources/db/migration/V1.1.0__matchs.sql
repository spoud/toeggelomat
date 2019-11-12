CREATE TABLE t_match
(
    match_uuid               UUID        NOT NULL,
    match_time               TIMESTAMPTZ NOT NULL,
    red_score                INTEGER     NOT NULL,
    blue_score               INTEGER     NOT NULL,
    points                   INTEGER     NOT NULL,
    player_red_defense_uuid  UUID        NOT NULL,
    player_red_offense_uuid  UUID        NOT NULL,
    player_blue_defense_uuid UUID        NOT NULL,
    player_blue_offense_uuid UUID        NOT NULL,
    PRIMARY KEY (match_uuid),
    FOREIGN KEY (player_red_defense_uuid) REFERENCES t_player (player_uuid),
    FOREIGN KEY (player_red_offense_uuid) REFERENCES t_player (player_uuid),
    FOREIGN KEY (player_blue_defense_uuid) REFERENCES t_player (player_uuid),
    FOREIGN KEY (player_blue_offense_uuid) REFERENCES t_player (player_uuid)
);

CREATE INDEX idx_match_match_time ON t_match (match_time);
CREATE INDEX idx_match_player_red_defense_uuid ON t_match (player_red_defense_uuid);
CREATE INDEX idx_match_player_red_offense_uuid ON t_match (player_red_offense_uuid);
CREATE INDEX idx_match_player_blue_defense_uuid ON t_match (player_blue_defense_uuid);
CREATE INDEX idx_match_player_blue_offense_uuid ON t_match (player_blue_offense_uuid);

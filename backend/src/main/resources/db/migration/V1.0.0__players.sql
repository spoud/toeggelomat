
CREATE TABLE t_player(
    player_uuid UUID NOT NULL,
    nick_name VARCHAR(100) NOT NULL,
    email VARCHAR(200) NOT NULL,
    defense_points INTEGER NOT NULL,
    offense_points INTEGER NOT NULL,
    last_match_time  TIMESTAMPTZ,
    PRIMARY KEY (player_uuid),
    CONSTRAINT undx_player_nickname UNIQUE (nick_name),
    CONSTRAINT undx_player_email UNIQUE (email)
);



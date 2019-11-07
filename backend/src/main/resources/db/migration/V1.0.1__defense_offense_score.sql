DELETE FROM t_player;

ALTER TABLE t_player ADD defense_points INTEGER NOT NULL;
ALTER TABLE t_player ADD offense_points INTEGER NOT NULL;
ALTER TABLE t_player DROP points;

INSERT INTO t_player (player_uuid, nick_name, email, defense_points, offense_points) VALUES('d2b6ad89-828c-4cd7-9ed9-6f49496fbd88', 'Anna', 'anna.messerli@spoud.io', 500, 500);
INSERT INTO t_player (player_uuid, nick_name, email, defense_points, offense_points) VALUES('e22b6b10-76aa-415a-b214-f5b83be6e49e', 'Gaétan', 'gaetan.collaud@spoud.io', 500, 500);
INSERT INTO t_player (player_uuid, nick_name, email, defense_points, offense_points) VALUES('a99dea23-5574-4739-86cb-87a0169fe45f', 'Julian', 'julian.stampfli@spoud.io', 500, 500);
INSERT INTO t_player (player_uuid, nick_name, email, defense_points, offense_points) VALUES('82f15dd8-76a4-484f-886d-3bee0dca80be', 'Marcel', 'marcel.baur@spoud.io', 500, 500);
INSERT INTO t_player (player_uuid, nick_name, email, defense_points, offense_points) VALUES('3343fcd1-7e29-4b53-8018-1e93038c141d', 'Patrick', 'patrick.boenzli@spoud.io', 500, 500);
INSERT INTO t_player (player_uuid, nick_name, email, defense_points, offense_points) VALUES('56a13282-02d4-498b-91cb-74f3c0c71f7f', 'Lukas', 'lukas.zaugg@spoud.io', 500, 500);

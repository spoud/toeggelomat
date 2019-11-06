
CREATE TABLE t_player(
    player_uuid UUID NOT NULL,
    nick_name VARCHAR(100) NOT NULL,
    email VARCHAR(200) NOT NULL,
    points INTEGER NOT NULL,
    PRIMARY KEY (player_uuid)
);


INSERT INTO t_player (player_uuid, nick_name, email, points) VALUES('b480a9a6-00c5-11ea-8d71-362b9e155667', 'Anna', 'anna.messerli@spoud.io', 1000);
INSERT INTO t_player (player_uuid, nick_name, email, points) VALUES('b480ac12-00c5-11ea-8d71-362b9e155667', 'Gaétan', 'gaetan.collaud@spoud.io', 1000);
INSERT INTO t_player (player_uuid, nick_name, email, points) VALUES('b480ad5c-00c5-11ea-8d71-362b9e155667', 'Julian', 'julian.kampfli@spoud.io', 1000);
INSERT INTO t_player (player_uuid, nick_name, email, points) VALUES('b480ae88-00c5-11ea-8d71-362b9e155667', 'Marcel', 'marcel.baur@spoud.io', 1000);
INSERT INTO t_player (player_uuid, nick_name, email, points) VALUES('b480afb4-00c5-11ea-8d71-362b9e155667', 'Patrick', 'patrick.boenzli@spoud.io', 1000);
INSERT INTO t_player (player_uuid, nick_name, email, points) VALUES('b480b2b6-00c5-11ea-8d71-362b9e155667', 'Lukas', 'lukas.zaugg@spoud.io', 1000);

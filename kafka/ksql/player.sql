CREATE STREAM employee (
      uuid VARCHAR KEY,
      nickName VARCHAR,
      email VARCHAR)
WITH (kafka_topic='employee', PARTITIONS=1, REPLICAS=1, value_format='JSON');

CREATE STREAM toeggelomat_player (
      uuid VARCHAR KEY,
      nickName VARCHAR,
      email VARCHAR, 
      defensePoints INT,
      offensePoints INT)
WITH (kafka_topic='toeggelomat-player', PARTITIONS=1,REPLICAS=1, value_format='JSON');

INSERT INTO toeggelomat_player
      SELECT 
            uuid,
            nickName, 
            email,
            500 as defensePoints,
            500 as offensePoints
       FROM employee;


CREATE STREAM toeggelomat_match_result (
      uuid VARCHAR, 
      matchTime VARCHAR, 
      redScore int, 
      blueScore int, 
      playerBlueDefenseUuid VARCHAR, 
      playerBlueOffenseUuid VARCHAR, 
      playerRedDefenseUuid VARCHAR, 
      playerRedOffenseUuid VARCHAR)
WITH (kafka_topic='toeggelomat-match-result', value_format='JSON',PARTITIONS=1,REPLICAS=1);

CREATE STREAM toeggelomat_scores (
      uuid VARCHAR,
      matchTime VARCHAR,
      redScore int, 
      blueScore int, 
      points int, 
      playerBlueDefenseUuid VARCHAR, 
      playerBlueOffenseUuid VARCHAR, 
      playerRedDefenseUuid VARCHAR, 
      playerRedOffenseUuid VARCHAR, 
      playerWinnerDefenseUuid VARCHAR,
      playerWinnerOffenseUuid VARCHAR,
      playerLoserDefenseUuid VARCHAR,
      playerLoserOffenseUuid VARCHAR)
WITH (kafka_topic='toeggelomat-scores', value_format='JSON',PARTITIONS=1,REPLICAS=1);

CREATE STREAM toeggelomat_point_change (
      uuid VARCHAR,
      pointsDefense int, 
      pointsOffense int, 
      matchTime VARCHAR,
      playerUuid VARCHAR)
      WITH (KAFKA_TOPIC='toeggelomat-point-change', VALUE_FORMAT='JSON',PARTITIONS=1,REPLICAS=1);

INSERT INTO toeggelomat_point_change
      SELECT 
            uuid, 
            points + 1 as pointsDefense,
            0 as pointsOffense,
            matchTime, 
            playerWinnerDefenseUuid as playerUuid
       FROM toeggelomat_scores;


INSERT INTO toeggelomat_point_change
      SELECT 
            uuid, 
            0 as pointsDefense, 
            points + 1 as pointsOffense,
            matchTime, 
            playerWinnerOffenseUuid as playerUuid
       FROM toeggelomat_scores;


INSERT INTO toeggelomat_point_change
      SELECT 
            uuid, 
            points * -1 + 1 as pointsDefense,
            0 as pointsOffense,
            matchTime, 
            playerLoserDefenseUuid as playerUuid
       FROM toeggelomat_scores;


INSERT INTO toeggelomat_point_change
      SELECT 
            uuid, 
            0 as pointsDefense, 
            points * -1 + 1 as pointsOffense,
            matchTime, 
            playerLoserOffenseUuid as playerUuid
       FROM toeggelomat_scores;

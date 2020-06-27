DROP TABLE IF EXISTS toeggelomat_player;
DROP STREAM IF EXISTS toeggelomat_scores;
DROP STREAM IF EXISTS toeggelomat_match_result;
DROP STREAM IF EXISTS toeggelomat_point_change;


CREATE STREAM toeggelomat_match_result (
      matchUuid VARCHAR KEY, 
      matchTime VARCHAR, 
      redScore int, 
      blueScore int, 
      blueDefense VARCHAR, 
      blueOffense VARCHAR, 
      redDefense VARCHAR, 
      redOffense VARCHAR)
WITH (kafka_topic='toeggelomat-match-result', value_format='JSON');

CREATE TABLE toeggelomat_player (
      uuid VARCHAR PRIMARY KEY,
      nickName VARCHAR,
      email VARCHAR, 
      slackId VARCHAR,
      defensePoints INT,
      offensePoints INT)
WITH (kafka_topic='toeggelomat-player', value_format='JSON');

CREATE STREAM toeggelomat_scores (
      matchUuid VARCHAR KEY,
      matchTime VARCHAR,
      redScore int, 
      blueScore int, 
      points int, 
      winnerDefense STRUCT<
        uuid VARCHAR, 
        nickName VARCHAR, 
        email VARCHAR, 
        slackId VARCHAR,
        defensePoints INT,
        offensePoints INT>,
        
      winnerOffense STRUCT<
        uuid VARCHAR, 
        nickName VARCHAR, 
        email VARCHAR, 
        slackId VARCHAR,
        defensePoints INT,
        offensePoints INT>,
        
      loserDefense STRUCT<
        uuid VARCHAR, 
        nickName VARCHAR, 
        email VARCHAR, 
        slackId VARCHAR,
        defensePoints INT,
        offensePoints INT>,
        
      loserOffense STRUCT<
        uuid VARCHAR, 
        nickName VARCHAR, 
        email VARCHAR, 
        slackId VARCHAR,
        defensePoints INT,
        offensePoints INT>)
WITH (kafka_topic='toeggelomat-scores', value_format='JSON');


CREATE STREAM toeggelomat_point_change 
      WITH (KAFKA_TOPIC='toeggelomat-point-change', VALUE_FORMAT='JSON') 
      AS SELECT 
            matchUuid, 
            points as points_defense, 
            0 as points_offense,
            matchTime, 
            winnerDefense->uuid as playerUuid
       FROM toeggelomat_scores;


INSERT INTO toeggelomat_point_change
      SELECT 
            matchUuid, 
            0 as points_defense, 
            points as points_offense,
            matchTime, 
            winnerOffense->uuid as playerUuid
       FROM toeggelomat_scores;


INSERT INTO toeggelomat_point_change
      SELECT 
            matchUuid, 
            points * -1 as points_defense, 
            0 as points_offense,
            matchTime, 
            loserDefense->uuid as playerUuid
       FROM toeggelomat_scores;


INSERT INTO toeggelomat_point_change
      SELECT 
            matchUuid, 
            0 as points_defense, 
            points * -1 as points_offense,
            matchTime, 
            loserOffense->uuid as playerUuid
       FROM toeggelomat_scores;

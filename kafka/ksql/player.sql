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
      uuid VARCHAR KEY, 
      matchTime VARCHAR, 
      redScore int, 
      blueScore int, 
      blueDefense VARCHAR, 
      blueOffense VARCHAR, 
      redDefense VARCHAR, 
      redOffense VARCHAR)
WITH (kafka_topic='toeggelomat-match-result', value_format='JSON',PARTITIONS=1,REPLICAS=1);

CREATE STREAM toeggelomat_scores (
      uuid VARCHAR KEY,
      matchTime VARCHAR,
      redScore int, 
      blueScore int, 
      points int, 
      winnerDefense STRUCT<
        uuid VARCHAR, 
        nickName VARCHAR, 
        email VARCHAR, 
        defensePoints INT,
        offensePoints INT>,
        
      winnerOffense STRUCT<
        uuid VARCHAR, 
        nickName VARCHAR, 
        email VARCHAR, 
        defensePoints INT,
        offensePoints INT>,
        
      loserDefense STRUCT<
        uuid VARCHAR, 
        nickName VARCHAR, 
        email VARCHAR, 
        defensePoints INT,
        offensePoints INT>,
        
      loserOffense STRUCT<
        uuid VARCHAR, 
        nickName VARCHAR, 
        email VARCHAR, 
        defensePoints INT,
        offensePoints INT>)
WITH (kafka_topic='toeggelomat-scores', value_format='JSON',PARTITIONS=1,REPLICAS=1);


CREATE STREAM toeggelomat_point_change 
      WITH (KAFKA_TOPIC='toeggelomat-point-change', VALUE_FORMAT='JSON',PARTITIONS=1,REPLICAS=1)
      AS SELECT 
            uuid, 
            points + 1 as points_defense,
            0 as points_offense,
            matchTime, 
            winnerDefense->uuid as playerUuid
       FROM toeggelomat_scores;


INSERT INTO toeggelomat_point_change
      SELECT 
            uuid, 
            0 as points_defense, 
            points + 1 as points_offense,
            matchTime, 
            winnerOffense->uuid as playerUuid
       FROM toeggelomat_scores;


INSERT INTO toeggelomat_point_change
      SELECT 
            uuid, 
            points * -1 + 1 as points_defense,
            0 as points_offense,
            matchTime, 
            loserDefense->uuid as playerUuid
       FROM toeggelomat_scores;


INSERT INTO toeggelomat_point_change
      SELECT 
            uuid, 
            0 as points_defense, 
            points * -1 + 1 as points_offense,
            matchTime, 
            loserOffense->uuid as playerUuid
       FROM toeggelomat_scores;

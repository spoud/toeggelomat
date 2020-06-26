DROP STREAM IF EXISTS toeggelomat_match_result_blueDefense DELETE TOPIC;
DROP STREAM IF EXISTS toeggelomat_match_result;


CREATE STREAM toeggelomat_match_result (matchUuid VARCHAR KEY, matchTime VARCHAR, redScore int, blueScore int, points int, blueDefense VARCHAR, blueOffense VARCHAR, redDefense VARCHAR, redOffense VARCHAR)
WITH (kafka_topic='toeggelomat-match-result', value_format='JSON');

CREATE TABLE toeggelomat_player (uuid VARCHAR PRIMARY KEY, nickName VARCHAR, email VARCHAR, slackId VARCHAR)
WITH (kafka_topic='toeggelomat-player', value_format='JSON', partition_by='UUID');

CREATE STREAM toeggelomat_match_result_blueDefense AS
  SELECT matchUuid, matchTime, redScore, blueScore, points, STRUCT(uuid := p.uuid, nickName := p.nickName, email := p.email, slackId := p.slackId), blueOffense, redDefense, redOffense
  FROM toeggelomat_match_result r
  LEFT JOIN toeggelomat_player p ON p.uuid = r.blueDefense;



CREATE STREAM toeggelomat_match_result_flat_bd2 AS
  SELECT matchUuid, matchTime, redScore, blueScore, points, blueDefense, blueOffense, redDefense, redOffense, bd.*
  FROM toeggelomat_match_result
  LEFT JOIN toeggelomat_player bd ON bd.uuid = blueDefense;

CREATE STREAM toeggelomat_match_result_flat_bo AS
  SELECT matchUuid, matchTime, redScore, blueScore, points, blueDefense, blueOffense, redDefense, redOffense, bo.*
  FROM toeggelomat_match_result_flat_bd
  LEFT JOIN toeggelomat_player bo ON bo.uuid = blueOffense;

CREATE STREAM toeggelomat_match_result_flat_rd AS
  SELECT r.*,  rd.*
  FROM toeggelomat_match_result_flat_bo
  LEFT JOIN toeggelomat_player rd ON rd.uuid = redDefense;

CREATE STREAM toeggelomat_match_result_flat_ro AS
  SELECT r.*, ro.*
  FROM toeggelomat_match_result_flat_rd
  LEFT JOIN toeggelomat_player ro ON ro.uuid = redOffense;

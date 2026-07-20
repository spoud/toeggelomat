CREATE TABLE t_season
(
    season_uuid UUID        NOT NULL,
    label       VARCHAR(100) NOT NULL,
    start_time  TIMESTAMPTZ NOT NULL,
    end_time    TIMESTAMPTZ,
    PRIMARY KEY (season_uuid)
);

ALTER TABLE t_match ADD COLUMN season_uuid UUID;
ALTER TABLE t_match ADD CONSTRAINT fk_match_season FOREIGN KEY (season_uuid) REFERENCES t_season (season_uuid);

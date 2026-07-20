ALTER TABLE t_player ADD COLUMN active BOOLEAN NOT NULL DEFAULT TRUE;

-- email is not used anywhere in the app (not exposed via GraphQL, never
-- read/written outside the initial seed data); relax it so creating a
-- player through the new createPlayer mutation only requires a nickname.
ALTER TABLE t_player ALTER COLUMN email DROP NOT NULL;

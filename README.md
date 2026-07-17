# Toeggelomat

## Purpose

The Toeggelomat has been created to improve breaks and increase the fun of playing foosball / töggele / kicker. It stores some players and can automatically match them together based on a selection. The matches are randomized and the winners will receive points.

## Screenshot

![alt text](https://spoud.io/wp-content/uploads/2020/02/toeggelomat.png "Screenshot of the Toeggelomat")

## Tech stack

- `backend/` — Quarkus (Java 21), Hibernate ORM with Panache, Flyway migrations, PostgreSQL
- `frontend/` — Angular, ng-bootstrap, Apollo GraphQL client

## Getting started

```bash
cd backend
./mvnw quarkus:dev    # dev mode with hot reload; Quarkus Dev Services will spin up PostgreSQL automatically if Docker is running
```

```bash
cd frontend
npm install
npm start              # dev server, proxies API calls to the backend (see proxy.conf.json)
```

`docker-compose.yml` at the repo root brings up a full stack (Traefik + built frontend/backend images) for a production-like local run.

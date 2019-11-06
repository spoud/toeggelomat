# Backend

## Configuration

``` text
{! src/main/resources/application.properties !}
```

## API

There is a REST API for the backend.

## Quick Start

docker-compose.yml:

``` text
{! docker-compose.yaml !}
```

## Development

### Run

Run a Kafka client and then the backend with quarkus

``` bash
docker-compose up -d
mvn clean quarkus:dev
```

### Styleguide

We use the [Google Style Guide](https://google.github.io/styleguide/javaguide.html).

To setup Intellij accordingly, we follow the recommendation of the gerrit review settings
<https://gerrit-review.googlesource.com/Documentation/dev-intellij.html#_recommended_settings>

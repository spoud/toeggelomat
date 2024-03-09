package io.spoud.rest;

import io.spoud.entities.PlayerEO;
import io.spoud.repositories.PlayerRepository;
import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/api/v1/players")
public class PlayerResource {

  @Inject private PlayerRepository playerRepository;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<PlayerEO> findAll() {
    return playerRepository.getAllPlayers();
  }
}

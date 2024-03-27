package io.spoud.rest;

import io.spoud.entities.PlayerEO;
import io.spoud.repositories.PlayerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@Path("/api/v1/players")
@RequiredArgsConstructor
public class PlayerResource {

  private final PlayerRepository playerRepository;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<PlayerEO> findAll() {
    return playerRepository.getAllPlayers();
  }
}

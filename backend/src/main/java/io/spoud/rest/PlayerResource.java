package io.spoud.rest;

import io.spoud.data.PlayerBO;
import io.spoud.repositories.PlayerRepository;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/api/v1/players")
public class PlayerResource {

  @Inject private PlayerRepository playerService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<PlayerBO> findAll() {
    return playerService.getAllPlayers();
  }
}

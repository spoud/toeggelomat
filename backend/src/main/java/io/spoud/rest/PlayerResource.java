package io.spoud.rest;

import io.spoud.entities.PlayerEO;
import io.spoud.repositories.PlayerRepository;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RestController
@RequestMapping("/api/v1/players")
public class PlayerResource {

    @Autowired
    private PlayerRepository playerRepository;


    @RequestMapping()
    public List<PlayerEO> findAll() {
        return playerRepository.getAllPlayers();
    }
}

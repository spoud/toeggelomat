package io.spoud.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.spoud.entities.PlayerEO;
import io.spoud.repositories.PlayerRepository;

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

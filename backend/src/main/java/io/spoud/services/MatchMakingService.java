package io.spoud.services;

import io.spoud.entities.CurrentMatchEO;
import io.spoud.entities.PlayerEO;
import io.spoud.repositories.PlayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.*;

@Slf4j
@Service
public class MatchMakingService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private Random random = new Random();

    private List<PlayerEO> queue = new ArrayList<>();

    public void addPlayerToQueue(UUID playerUuid) {
        PlayerEO player = playerRepository.findByUuid(playerUuid).orElseThrow(() -> new NotFoundException("User with uuid '" + playerUuid + "' not found"));
        queue.add(player);
        //always sort the strength of the player
        Collections.sort(queue, Comparator.comparing(PlayerEO::getPoints));
        if (queue.size() == 4) {
            createNewMatch(1);
        }
    }

    private CurrentMatchEO createNewMatch(int retry) {
        ArrayList<PlayerEO> listCopy = new ArrayList<>(queue);
        CurrentMatchEO match = CurrentMatchEO.builder()
                .playerBlueDefense(listCopy.remove(random.nextInt(4)))
                .playerBlueOffense(listCopy.remove(random.nextInt(3)))
                .playerRedDefense(listCopy.get(0))
                .playerRedOffense(listCopy.get(1))
                .build();

        if (retry > 0) {
            // check fairness

            UUID lowPlayer1 = queue.get(0).getUuid();
            UUID lowPlayer2 = queue.get(1).getUuid();
            boolean lowPlayer1IsBlue = lowPlayer1.equals(match.getPlayerBlueDefense().getUuid()) || lowPlayer1.equals(match.getPlayerBlueOffense());
            boolean lowPlayer2IsBlue = lowPlayer2.equals(match.getPlayerBlueDefense().getUuid()) || lowPlayer2.equals(match.getPlayerBlueOffense());

            if (lowPlayer1IsBlue == lowPlayer2IsBlue) {
                log.info("Match is not fair, retry again: {}", match);
                return createNewMatch(retry - 1);
            }
        }
        return match;
    }
}

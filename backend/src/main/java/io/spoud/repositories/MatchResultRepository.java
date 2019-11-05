package io.spoud.repositories;

import io.spoud.entities.MatchEO;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Repository
public class MatchResultRepository {

    private static List<MatchEO> db = Arrays.asList();

    public MatchEO addMatch(MatchEO matchResult){
        matchResult.setUuid(UUID.randomUUID());
        db.add(matchResult);
        return matchResult;
    }

    public List<MatchEO> getLastMatches(int count) {
        return Collections.emptyList();
    }
}

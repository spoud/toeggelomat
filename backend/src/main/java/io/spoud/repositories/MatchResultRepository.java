package io.spoud.repositories;

import io.spoud.entities.MatchResultEO;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Repository
public class MatchResultRepository {

    private static List<MatchResultEO> db = Arrays.asList();

    public MatchResultEO addMatch(MatchResultEO matchResult){
        matchResult.setUuid(UUID.randomUUID());
        db.add(matchResult);
        return matchResult;
    }

    public List<MatchResultEO> getLastMatches(int count) {
        return Collections.emptyList();
    }
}

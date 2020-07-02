package io.spoud.streams.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import io.spoud.data.MatchResultBO;
import io.spoud.data.MatchResultWithPointsBO;
import io.spoud.services.MatchPointsService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@Slf4j
public class PointsProcessor {
  @Inject private ObjectMapper mapper;

  @Inject MatchPointsService matchPointsService;

  @Incoming("match-result-in")
  @Outgoing("scores-out")
  @Broadcast
  public MatchResultWithPointsBO process(MatchResultBO result) {
    return matchPointsService.computePoints(result);
  }
}

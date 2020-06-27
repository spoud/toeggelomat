package io.spoud.processor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.reactive.messaging.annotations.Blocking;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import io.spoud.data.kafka.MatchResultBO;
import io.spoud.services.MatchPointsService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.InvalidRecordException;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

@ApplicationScoped
@Slf4j
public class PointsProcessor {
  @Inject private ObjectMapper mapper;

  @Inject MatchPointsService matchPointsService;

  @Incoming("match-result-in")
  @Outgoing("scores-out")
  @Blocking
  @Transactional
  @Broadcast
  public String process(String result) {
    try {
      return mapper.writeValueAsString(
          matchPointsService.computePoints(mapper.readValue(result, MatchResultBO.class)));
    } catch (JsonProcessingException e) {
      throw new InvalidRecordException(result, e);
    }
  }
}

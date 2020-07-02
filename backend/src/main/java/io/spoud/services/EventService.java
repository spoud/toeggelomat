package io.spoud.services;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import io.spoud.data.MatchPropositionBO;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;

@Slf4j
@ApplicationScoped
public class EventService {

  private BroadcastProcessor<String> scoreChangedProcessor;
  private BroadcastProcessor<MatchPropositionBO> matchProcessor;

  public EventService() {
    scoreChangedProcessor = BroadcastProcessor.create();
    matchProcessor = BroadcastProcessor.create();
  }

  public synchronized void newMatchEvent(MatchPropositionBO match) {
    matchProcessor.onNext(match);
  }

  public Multi<MatchPropositionBO> newMatchStream() {
    return matchProcessor;
  }

  public synchronized void scoreChangedEvent() {
    scoreChangedProcessor.onNext("newScore");
  }

  public Multi<String> scoreChangedStream() {
    return scoreChangedProcessor;
  }
}

package io.spoud.services;

import io.spoud.entities.MatchEO;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EventService {
  private static final Logger LOG = LoggerFactory.getLogger(EventService.class);


  private Publisher<MatchEO> matchPublisher;
  private Subscriber<? super MatchEO> matchSubscriber;

  private Publisher<String> scoreChangePublisher;
  private Subscriber<? super String> scoreChangeSubscriber;

  public EventService() {
    this.matchPublisher = subscriber -> {
      this.matchSubscriber = subscriber;
    };
    this.scoreChangePublisher = subscriber -> {
      this.scoreChangeSubscriber = subscriber;
    };
  }

  public void newMatchEvent(MatchEO match) {
    try {
      matchSubscriber.onNext(match);
    } catch (Exception ex) {
      LOG.error("Cannot push match", ex);
    }
  }

  public Publisher<MatchEO> newMatchStream() {
    return matchPublisher;
  }

  public void scoreChangedEvent() {
    scoreChangeSubscriber.onNext("newScore");
  }

  public Publisher<String> scoreChangedStream() {
    return scoreChangePublisher;
  }
}

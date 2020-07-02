package io.spoud.services;

import io.spoud.data.entities.MatchProposition;
import javax.enterprise.context.ApplicationScoped;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class EventService {
  private static final Logger LOG = LoggerFactory.getLogger(EventService.class);

  private Publisher<MatchProposition> matchPublisher;
  private Subscriber<? super MatchProposition> matchSubscriber;

  private Publisher<String> scoreChangePublisher;
  private Subscriber<? super String> scoreChangeSubscriber;

  public EventService() {
    this.matchPublisher =
        subscriber -> this.matchSubscriber = subscriber;
    this.scoreChangePublisher =
        subscriber -> this.scoreChangeSubscriber = subscriber;
  }

  public void newMatchEvent(MatchProposition match) {
    try {
      matchSubscriber.onNext(match);
    } catch (NullPointerException ex) {
      LOG.warn("matchSubscriber is null again");
    } catch (Exception ex) {
      LOG.error("Cannot push match", ex);
    }
  }

  public Publisher<MatchProposition> newMatchStream() {
    return matchPublisher;
  }

  public void scoreChangedEvent() {
    try {
      scoreChangeSubscriber.onNext("newScore");
    } catch (NullPointerException ex) {
      LOG.warn("scoreChangeSubscriber is null again");
    } catch (Exception ex) {
      LOG.error("Cannot push score", ex);
    }
  }

  public Publisher<String> scoreChangedStream() {
    return scoreChangePublisher;
  }
}

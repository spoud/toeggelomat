package io.spoud.services;

import io.spoud.entities.MatchEO;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.springframework.stereotype.Service;

@Service
public class EventService {

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
        matchSubscriber.onNext(match);
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

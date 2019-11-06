package io.spoud.rest;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.spoud.services.EventService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ServerEndpoint("/api/v1/ws")
@ApplicationScoped
public class WebsocketResource implements PropertyChangeListener {

    @Autowired
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Session> sessions = Collections.synchronizedList(new ArrayList<>());

    @PostConstruct
    public void postConstruct() {
        eventService.addPropertyChangeListener(this);
    }

    @OnOpen
    public void onOpen(Session session) {
        log.info("on open");
        sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        log.info("on close");
        sessions.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.info("on error");
        sessions.remove(session);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        log.info("on message");
    }

    private void broadcast(Object obj) {
        try {
            String msg = objectMapper.writeValueAsString(obj);
            sessions.forEach(s -> {
                s.getAsyncRemote().sendObject(msg, result -> {
                    if (result.getException() != null) {
                        log.error("Unable to send message: ", result.getException());
                    }
                });
            });
        } catch (JsonProcessingException e) {
            log.error("Unable to write object ", e);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "match":
                broadcast(evt.getNewValue());
                break;
            default:
                log.error("Unknown property name {}", evt.getPropertyName());
        }
    }
}

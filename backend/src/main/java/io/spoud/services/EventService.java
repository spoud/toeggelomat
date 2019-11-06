package io.spoud.services;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.springframework.stereotype.Service;

import io.spoud.entities.MatchEO;

@Service
public class EventService {
    private PropertyChangeSupport changeListener;

    public EventService() {
        this.changeListener = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        changeListener.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        changeListener.removePropertyChangeListener(pcl);
    }

    public void newMatch(MatchEO match){
        changeListener.firePropertyChange("match", null, match);
    }

}

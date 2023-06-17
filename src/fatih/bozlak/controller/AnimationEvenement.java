package fatih.bozlak.controller;

import javafx.event.Event;
import javafx.event.EventType;

public class AnimationEvenement extends Event {
    public AnimationEvenement(EventType<? extends Event> eventType) {
        super(eventType);
    }
}

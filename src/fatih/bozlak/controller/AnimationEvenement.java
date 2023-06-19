package fatih.bozlak.controller;

import javafx.event.Event;
import javafx.event.EventType;

public class AnimationEvenement extends Event {
    private String msg;
    public AnimationEvenement(EventType<? extends Event> eventType) {
        super(eventType);
    }
    
    public AnimationEvenement(EventType<? extends Event> eventType, String msg) {
        super(eventType);
        this.msg = msg;
    }
    
    public String getMsg() {
        return this.msg;
    }
}

package fatih.bozlak.controller;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * La classe AnimationEvenement est utilisée pour gérer les différents types d'événements qui peuvent se produire
 * pendant une animation. Elle étend la classe Event de JavaFX.
 */
public class AnimationEvenement extends Event {
    // Message qui peut être associé à l'événement.
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

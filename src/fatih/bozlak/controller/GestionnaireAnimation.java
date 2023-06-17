package fatih.bozlak.controller;

import javafx.event.Event;
import javafx.event.EventType;

public class GestionnaireAnimation {
    private final EventType<AnimationEvenement> DISTRIBUTION_DES_CARTES_TERMINEE = new EventType<AnimationEvenement>(Event.ANY, "DISTRIBUTION_DES_CARTES_TERMINEE");
    private final EventType<AnimationEvenement> JOUEUR_A_JOUEE_TERMINEE = new EventType<AnimationEvenement>(Event.ANY, "JOUEUR_A_JOUEE_TERMINEE");
    private final EventType<AnimationEvenement> PANIER_ARRANGEE_TERMINEE = new EventType<AnimationEvenement>(Event.ANY, "PANIER_ARRANGER_TERMINEE");
}

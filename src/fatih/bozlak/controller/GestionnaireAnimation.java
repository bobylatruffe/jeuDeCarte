package fatih.bozlak.controller;

import fatih.bozlak.modele.Carte;
import javafx.animation.TranslateTransition;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.util.Duration;

public class GestionnaireAnimation {
    public final EventType<AnimationEvenement> CARTE_DISTRIBUEE = new EventType<AnimationEvenement>(Event.ANY, "CARTE_DISTRIBUEE");
    public final EventType<AnimationEvenement> CARTE_DISTRIBUEES = new EventType<AnimationEvenement>(Event.ANY, "CARTE_DISTRIBUEES");
    public final EventType<AnimationEvenement> JOUEUR_A_JOUEE_TERMINEE = new EventType<AnimationEvenement>(Event.ANY, "JOUEUR_A_JOUEE_TERMINEE");
    public final EventType<AnimationEvenement> PANIER_ARRANGEE_TERMINEE = new EventType<AnimationEvenement>(Event.ANY, "PANIER_ARRANGER_TERMINEE");
    
    private final double dureeAnimationDistribution = 100;
    private final double distanceDuPanierAuxJoueurs;
    
    private final FenetrePrincipaleControlleur fenetrePrincipaleControlleur;
    
    public GestionnaireAnimation(FenetrePrincipaleControlleur fenetrePrincipaleControlleur) {
        this.fenetrePrincipaleControlleur = fenetrePrincipaleControlleur;
        distanceDuPanierAuxJoueurs = this.fenetrePrincipaleControlleur.getHeight() / 2;
    }
    
    private double lastOffsetForPerpectiveForIa = 0.0;
    private double lastOffsetForPerpectiveForNonIa = 0.0;
    public void donnerUneCarteAUnJoueur(Carte carteADonner) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(dureeAnimationDistribution), carteADonner);
        
        if (carteADonner.isForIa()) {
            tt.setByY(-distanceDuPanierAuxJoueurs + lastOffsetForPerpectiveForIa);
            lastOffsetForPerpectiveForIa += 0.5;
        } else {
            tt.setToY(distanceDuPanierAuxJoueurs - lastOffsetForPerpectiveForNonIa);
            lastOffsetForPerpectiveForNonIa += 0.5;
        }
        
        carteADonner.toFront();
        fenetrePrincipaleControlleur.getScoreBar().toFront();
        
        tt.play();
        tt.setOnFinished(e -> {
            carteADonner.toBack();
            fenetrePrincipaleControlleur.pseudoIa.fireEvent(new AnimationEvenement(CARTE_DISTRIBUEE));
        });
    }
}

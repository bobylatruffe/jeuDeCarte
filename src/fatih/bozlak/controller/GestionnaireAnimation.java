package fatih.bozlak.controller;

import fatih.bozlak.modele.Carte;
import fatih.bozlak.modele.Joueur;
import fatih.bozlak.modele.MaitreDuJeu;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.ArrayList;

public class GestionnaireAnimation {
    public final EventType<AnimationEvenement> BATAILLE = new EventType<AnimationEvenement>(Event.ANY, "BATAILLE");
    public final EventType<AnimationEvenement> PANIER_DISTRIBUEE = new EventType<AnimationEvenement>(Event.ANY, "PANIER_DISTRIBUEE");
    public final EventType<AnimationEvenement> VALEURS_AFFICHEES = new EventType<AnimationEvenement>(Event.ANY, "VALEURS_AFFICHEES");
    public final EventType<AnimationEvenement> MANCHE_JOUEE = new EventType<AnimationEvenement>(Event.ANY, "MANCHE_JOUEE");
    public final EventType<AnimationEvenement> CARTE_DISTRIBUEE = new EventType<AnimationEvenement>(Event.ANY, "CARTE_DISTRIBUEE");
    public final EventType<AnimationEvenement> CARTE_DISTRIBUEES = new EventType<AnimationEvenement>(Event.ANY, "CARTE_DISTRIBUEES");
    public final EventType<AnimationEvenement> JOUEUR_A_JOUEE = new EventType<AnimationEvenement>(Event.ANY, "JOUEUR_A_JOUEE");
    public final EventType<AnimationEvenement> PANIER_ARRANGEE_TERMINEE = new EventType<AnimationEvenement>(Event.ANY, "PANIER_ARRANGER_TERMINEE");
    
    private final double dureeAnimation = 100;
    private final double distanceDuPanierAuxJoueurs;
    private final double offsetCarteDansPanier = 30.0;
    
    private final FenetrePrincipaleControlleur mainWindow;
    
    public GestionnaireAnimation(FenetrePrincipaleControlleur fenetrePrincipaleControlleur) {
        this.mainWindow = fenetrePrincipaleControlleur;
        distanceDuPanierAuxJoueurs = this.mainWindow.getHeight() / 2;
    }
    
    private double lastOffsetForPerpectiveForIa = 0.0;
    private double lastOffsetForPerpectiveForNonIa = 0.0;
    
    private void incPrograssbarNonIa() {
        mainWindow.getProgressBarNonIa().setProgress(mainWindow.getProgressBarNonIa().getProgress() + mainWindow.getStepProgressBar());
    }
    
    private void decProgressBarNonIa() {
        mainWindow.getProgressBarNonIa().setProgress(mainWindow.getProgressBarNonIa().getProgress() - mainWindow.getStepProgressBar());
    }
    
    private void incProgressBarIa() {
        mainWindow.getProgressBarIa().setProgress(mainWindow.getProgressBarIa().getProgress() + mainWindow.getStepProgressBar());
    }
    
    private void decProgressBarIa() {
        mainWindow.getProgressBarIa().setProgress(mainWindow.getProgressBarIa().getProgress() - mainWindow.getStepProgressBar());
    }
    
    public void donnerUneCarteAUnJoueur(Carte carteADonner) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(dureeAnimation), carteADonner);
        
        if (carteADonner.isForIa()) {
            tt.setByY(-distanceDuPanierAuxJoueurs);
//            lastOffsetForPerpectiveForIa += 0.5;
            incProgressBarIa();
        } else {
            tt.setToY(distanceDuPanierAuxJoueurs);
//            lastOffsetForPerpectiveForNonIa += 0.5;
            incPrograssbarNonIa();
        }
        
        carteADonner.toFront();
        mainWindow.getScoreBar().toFront();
        
        tt.play();
        tt.setOnFinished(e -> {
            carteADonner.toBack();
            mainWindow.pseudoIa.fireEvent(new AnimationEvenement(CARTE_DISTRIBUEE));
        });
    }
    
    public void joueurJoueUneCarte(Carte carteJouee) {
        
        TranslateTransition tt = new TranslateTransition(Duration.millis(dureeAnimation), carteJouee);
        
        carteJouee.toFront();
        
        if (carteJouee.isForIa()) {
            tt.setByY(distanceDuPanierAuxJoueurs);
            tt.setByX(carteJouee.getFitWidth() / 2 + offsetCarteDansPanier);
        } else {
            tt.setByY(-distanceDuPanierAuxJoueurs);
            tt.setByX(-carteJouee.getFitWidth() / 2 - offsetCarteDansPanier);
        }
        
        tt.play();
        tt.setOnFinished(e -> {
            if (carteJouee.isForIa()) {
                mainWindow.pseudoIa.fireEvent(new AnimationEvenement(MANCHE_JOUEE));
                decProgressBarIa();
            } else {
                mainWindow.pseudoIa.fireEvent(new AnimationEvenement(JOUEUR_A_JOUEE));
                decProgressBarNonIa();
            }
        });
    }
    
    public void showValueCards(Carte[] cartesAComparer) {
        for (Carte carte : cartesAComparer) {
            RotateTransition rt = new RotateTransition(Duration.millis(dureeAnimation), carte);
            rt.setAxis(Rotate.Y_AXIS);
            rt.setByAngle(90);
            rt.setDelay(Duration.millis(dureeAnimation));
            
            rt.play();
            rt.setOnFinished(e -> {
                carte.setFaceDecouverte(true);
                
                RotateTransition rt2 = new RotateTransition(Duration.millis(dureeAnimation), carte);
                rt2.setAxis(Rotate.Y_AXIS);
                rt2.setByAngle(-90);
                
                rt2.play();
                rt2.setOnFinished(ee -> {
                    if (carte.isForIa()) {
                        mainWindow.pseudoIa.fireEvent(new AnimationEvenement(VALEURS_AFFICHEES));
                    }
                });
            });
        }
    }
    
    public void retournerLesCartesFacesCacheeDuPanier(ArrayList<Carte> panier) {
        for (Carte carte : panier) {
            if (!carte.isFaceDecouverte()) {
                carte.setFaceDecouverte(true);
//                carte.setLayoutY(carte.getLayoutY() - offsetCarteDansPanier);
            }
        }
    }
    
    public void donnerLesCartesDuPanierAuVainqeur(Joueur gagnant, ArrayList<Carte> panier) {
        SequentialTransition st = new SequentialTransition();
        
        for (Carte carte : panier) {
//            carte.toBack();
            
            TranslateTransition tt = new TranslateTransition(Duration.millis(dureeAnimation), carte);
            
            if (gagnant.getPseudo().equals(MaitreDuJeu.nomIa)) {
                tt.setByY(-distanceDuPanierAuxJoueurs);
            } else {
                tt.setByY(distanceDuPanierAuxJoueurs);
            }
            
            tt.setToX(0);
            
            tt.setOnFinished(e -> {
                carte.toBack();
                carte.setFaceDecouverte(false);
            });
            
            st.getChildren().add(tt);
        }
        st.setDelay(Duration.millis(2000));
        st.play();
        st.setOnFinished(e -> {
            panier.forEach(carte -> {
                if (gagnant.getPseudo().equals(MaitreDuJeu.nomIa)) incProgressBarIa();
                else incPrograssbarNonIa();
            });
            mainWindow.pseudoIa.fireEvent(new AnimationEvenement(PANIER_DISTRIBUEE));
        });
    }
    
    public void arrangerPanierPourBataille(ArrayList<Carte> panier) {
        for (Carte carte : panier) {
            TranslateTransition tt = new TranslateTransition(Duration.millis(dureeAnimation), carte);
            if (carte.isForIa()) {
                tt.setByX(offsetCarteDansPanier);
            } else {
                tt.setByX(-offsetCarteDansPanier);
            }
            
            tt.play();
        }
    }
}

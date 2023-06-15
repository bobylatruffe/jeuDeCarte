package fatih.bozlak.controller;

import fatih.bozlak.modele.Carte;
import fatih.bozlak.modele.ErreurMaitreDuJeu;
import fatih.bozlak.modele.Joueur;
import fatih.bozlak.modele.MaitreDuJeu;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.control.ProgressBar;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class JeuControlleur {
    private MaitreDuJeu maitre;
    private ProgressBar progressBarIa, progressBarNonIa;
    private Joueur joueurNonIa;
    private Joueur joueurIa;
    
    private double offSetCarte = 30.0;
    
    public void animationJoueurJoue(Joueur joueur, Carte carte) {
        double positionY = FenetrePrincipaleController.height / 2 - carte.getFitHeight() / 2;
        
        if (joueur.getPseudo().equals(MaitreDuJeu.nomIa)) {
            carte.setLayoutY(positionY);
            carte.setLayoutX(carte.getLayoutX() + carte.getFitWidth() / 2 + offSetCarte);
            progressBarIa.setProgress(progressBarIa.getProgress() - FenetrePrincipaleController.stepProgressBar);
        } else {
            carte.setLayoutY(positionY);
            carte.setLayoutX(carte.getLayoutX() - carte.getFitWidth() / 2 - offSetCarte);
            progressBarNonIa.setProgress(progressBarIa.getProgress() - FenetrePrincipaleController.stepProgressBar);
        }
    }
    
    public void animationFight() {
        Carte carteJoueurIa = maitre.getPanier().get(maitre.getPanier().size() - 1);
        Carte carteJoueurNonIa = maitre.getPanier().get(maitre.getPanier().size() - 2);
        
        SequentialTransition pt = new SequentialTransition();
        for (Carte carte : new Carte[]{carteJoueurNonIa, carteJoueurIa}) {
            RotateTransition rt = new RotateTransition(Duration.millis(250), carte);
            rt.setAxis(Rotate.Y_AXIS);
            rt.setByAngle(90);
            rt.setOnFinished(e -> {
                carte.setFaceDecouverte(true);
            });
            
            pt.getChildren().add(rt);
            
            rt = new RotateTransition(Duration.millis(250), carte);
            rt.setAxis(Rotate.Y_AXIS);
            rt.setByAngle(-90);
            
            pt.getChildren().add(rt);
        }
        
        pt.play();
        pt.setOnFinished(e -> {
            try {
                Joueur joueurGagnant = maitre.quiGagne();
                if (joueurGagnant != null) {
//                    animationDistribuerPanierAuGagnant(joueurGagnant);
                }
            } catch (ErreurMaitreDuJeu ex) {
                // Todo : proposer de recommencer une partie
            }
        });
    }
    
    public void joueurNonIaJoue(boolean isFaceDecouverte) {
        Carte carteJouee = maitre.demanderAUnJoueurDeJouer(joueurNonIa, true);
        if (carteJouee != null) {
            carteJouee.setOnMouseClicked(null);
            animationJoueurJoue(joueurNonIa, carteJouee);
        }
        
        carteJouee = maitre.demanderAUnJoueurDeJouer(joueurIa, true);
        if (carteJouee != null) animationJoueurJoue(joueurIa, carteJouee);
        
        animationFight();
    }
    
    public JeuControlleur(MaitreDuJeu maitre, ProgressBar progressBarIa, ProgressBar progressBarNonIa) {
        this.maitre = maitre;
        this.progressBarIa = progressBarIa;
        this.progressBarNonIa = progressBarNonIa;
        
        this.joueurIa = maitre.getJoueurs().get(0);
        this.joueurNonIa = maitre.getJoueurs().get(1);
        
        joueurNonIa.getCartes().get(0).setOnMouseClicked(e -> joueurNonIaJoue(true));
        System.out.println(joueurNonIa);
        System.out.println(joueurIa);
    }
}

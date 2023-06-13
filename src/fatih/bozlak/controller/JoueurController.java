package fatih.bozlak.controller;

import fatih.bozlak.modele.Carte;
import fatih.bozlak.modele.ErreurJoueur;
import fatih.bozlak.modele.Joueur;
import fatih.bozlak.modele.MaitreDuJeu;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point3D;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class JoueurController {
    private FenetrePrincipaleController mainFenetre;
    private Joueur joueurIa;
    private Joueur joueurNonIa;
    private MaitreDuJeu maitre;
    
    public JoueurController(FenetrePrincipaleController fenetrePrincipaleController, Joueur joueurIa, Joueur joueurNonIa) {
        this.mainFenetre = fenetrePrincipaleController;
        this.maitre = fenetrePrincipaleController.getMaitre();
        this.joueurNonIa = joueurNonIa;
        this.joueurIa = joueurIa;
        
        joueurNonIa.getCartes().get(0).setOnMouseClicked(e -> {
            try {
                Carte carteJouee = joueurNonIa.jouerUneCarte();
                maitre.ajouterCarteAuPanier(joueurNonIa, carteJouee, true);
                mainFenetre.animationCarteJouee(false, carteJouee, true);
                carteJouee.setOnMouseClicked(null);
            } catch (ErreurJoueur err) {
                System.out.println(err.getMessage());
            }
            
            try {
                Carte carteJouee = joueurIa.jouerUneCarte();
                maitre.ajouterCarteAuPanier(joueurIa, carteJouee, true);
                mainFenetre.animationCarteJouee(true, carteJouee, true);
                
                maitre.determinerVainqueur();
            } catch (ErreurJoueur err) {
                System.out.println(err.getMessage());
            }
        });
        System.out.println(joueurNonIa);
    }
}

package fatih.bozlak.controller;

import fatih.bozlak.modele.Carte;
import fatih.bozlak.modele.ErreurMaitreDuJeu;
import fatih.bozlak.modele.Joueur;
import fatih.bozlak.modele.MaitreDuJeu;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class FenetrePrincipaleControlleur implements Initializable {
    @FXML
    private ProgressBar progressBarIa;
    
    @FXML
    private ProgressBar progressBarNonIa;
    
    @FXML
    private Label pseudoNonIa;
    
    @FXML
    private Pane fenetrePrincipale;
    
    @FXML
    private HBox scoreBar;
    
    @FXML
    private Group cartesPanier;
    
    @FXML
    private Group cartesJoueurIa;
    @FXML
    private Group cartesJoueurNonIa;
    @FXML
    private Label pseudoIa;
    
    private MaitreDuJeu maitre;
    
    private GestionnaireDesCartesEtJoueurs gestionnaire;
    
    private double stepProgressBar;
    
    private Joueur joueurNonIa;
    private Joueur joueurIa;
    
    public FenetrePrincipaleControlleur() {
        maitre = new MaitreDuJeu(2);
        joueurNonIa = new Joueur("Bobylatruffe");
        maitre.addJoueur(joueurNonIa);
        joueurIa = maitre.getJoueurs().get(0);
        
        stepProgressBar = 1.0 / maitre.getPaquet().getCartes().size();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gestionnaire = new GestionnaireDesCartesEtJoueurs(cartesJoueurNonIa, cartesJoueurIa, cartesPanier, fenetrePrincipale, this);
        fenetrePrincipale.setOnMouseClicked((e) -> {
            gestionnaire.placerPaquetCarteAuCentre(maitre.getPaquet());
            gestionnaire.distribuerCartesAuxJoueurs(maitre);
        });
        
        progressBarNonIa.setOnMouseClicked((e) -> {
            Carte carteJouee = maitre.demanderAUnJoueurDeJouer(joueurNonIa, true);
            if (carteJouee != null) {
                gestionnaire.joueurAJoueeUneCarte(carteJouee);
            }
            
            carteJouee = maitre.demanderAUnJoueurDeJouer(joueurIa, true);
            if (carteJouee != null) {
                gestionnaire.joueurAJoueeUneCarte(carteJouee);
            }
            
            gestionnaire.fight(maitre.getAComparer());
            
            try {
                Joueur gagant = maitre.quiGagne();
                if (gagant != null) {
                    gestionnaire.vainqueurAnimation(gagant);
                }
            } catch (ErreurMaitreDuJeu ex) {
                // todo: demander à relancer une partie.
            }
            
        });
    }
    
    public ProgressBar getProgressBarIa() {
        return progressBarIa;
    }
    
    public ProgressBar getProgressBarNonIa() {
        return progressBarNonIa;
    }
    
    public double getStepProgressBar() {
        return stepProgressBar;
    }
}

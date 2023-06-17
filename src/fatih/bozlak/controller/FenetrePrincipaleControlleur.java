package fatih.bozlak.controller;

import fatih.bozlak.modele.Carte;
import fatih.bozlak.modele.Joueur;
import fatih.bozlak.modele.MaitreDuJeu;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private Label pseudoIa;
    
    private double width = 1000.0;
    private double height = 800.0;
    
    private double stepProgressBar;
    
    private MaitreDuJeu maitre;
    
    private GestionnaireAnimation gestionnaireAnimation;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gestionnaireAnimation = new GestionnaireAnimation();
        
        maitre = new MaitreDuJeu(2);
        maitre.addJoueur(new Joueur("BobyLatruffe"));
        
        maitre.getPaquet().melanger();
        maitre.log("Paquet de carte mélangé.");
        
        stepProgressBar = 1.0 / maitre.getPaquet().getCartes().size();
        
        placerLePaquetAuCentre();
    }
    
    private void placerLePaquetAuCentre() {
        for (Carte carte : maitre.getPaquet().getCartes()) {
            fenetrePrincipale.getChildren().add(carte);
            carte.setLayoutX(width / 2 - carte.getFitWidth() / 2);
            carte.setLayoutY(height / 2 - carte.getFitHeight() / 2);

            carte.toBack();
        }
    }
}

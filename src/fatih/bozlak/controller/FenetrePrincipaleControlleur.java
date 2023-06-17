package fatih.bozlak.controller;

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
    
    public FenetrePrincipaleControlleur() {
    
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    
    }
}

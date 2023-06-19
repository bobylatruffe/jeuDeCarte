package fatih.bozlak.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AccueilControlleur implements Initializable {
    @FXML
    private Button btnJouer;
    
    @FXML
    private TextField tfPseudo;
    
    @FXML
    private TextField tfNbCartes;
    
    @FXML
    private Label lblErreur;
    
    private final String pseudo;
    private int nbDeCartes;
    
    public AccueilControlleur() {
        super();
        
        this.pseudo = "";
    }
    
    public AccueilControlleur(String pseudo, int nbDeCartesParCouleur) {
        super();
        
        this.pseudo = pseudo;
        this.nbDeCartes = nbDeCartesParCouleur * 4;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tfPseudo.setText(pseudo);
        
        if (nbDeCartes == 0) {
            tfNbCartes.setText(String.valueOf(8));
        } else {
            tfNbCartes.setText(String.valueOf(nbDeCartes));
        }
    }
    
    @FXML
    void ActionJouer(ActionEvent event) throws IOException {
        int nbDeCartesParCouleur = 0;
        
        if (tfPseudo.getText().isBlank()) {
            lblErreur.setText("Si pas d'idée pour le pseudo, essaye \"Tricheur\"...");
            System.out.println("Erreur pseudo");
            return;
        }
        
        if (tfNbCartes.getText().isBlank()) {
            lblErreur.setText("Et le nombre de carte, je dois le deviner ?!");
            System.out.println("Erreur nbCartes");
            return;
        }
        
        try {
            nbDeCartesParCouleur = Integer.parseInt(tfNbCartes.getText()) / 4;
            if (nbDeCartesParCouleur > 13) {
                lblErreur.setText("Oups, c'est beaucoup trop de carte pour moi (max=52) !");
                return;
            } else if (nbDeCartesParCouleur < 1) {
                lblErreur.setText("Oups, c'est vraiment pas assez de carte pour jouer (min=4) !");
                return;
            }
        } catch (NumberFormatException err) {
            lblErreur.setText("Tu sais ce qu'un nombre au moins ?!");
            return;
        }
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fatih/bozlak/view/FenetrePrincipale.fxml"));
        loader.setController(new FenetrePrincipaleControlleur(tfPseudo.getText(), nbDeCartesParCouleur));
        
        Parent root = loader.load();
        
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        
        stage.show();
        
        // C'est une astuce connu de JavaFX pour récupérer la fenêtre courante
        ((Stage) btnJouer.getScene().getWindow()).close();
        
    }
}

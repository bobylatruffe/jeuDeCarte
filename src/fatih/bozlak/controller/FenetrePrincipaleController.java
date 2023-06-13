package fatih.bozlak.controller;

import fatih.bozlak.modele.Carte;
import fatih.bozlak.modele.Joueur;
import fatih.bozlak.modele.MaitreDuJeu;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class FenetrePrincipaleController implements Initializable {
    private MaitreDuJeu maitre;
    
    private final double width = 1000;
    private final double height = 700;
    
    @FXML
    private Pane fenetrePrincipale;
    
    @FXML
    private HBox scoreHBox;
    
    @FXML
    private TextField tfPseudo;
    
    @FXML
    private Button startGame;
    
    @FXML
    private ProgressBar progressBarNonIa;
    
    @FXML
    private ProgressBar progressBarIa;
    
    @FXML
    private Label lblPseudo;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fenetrePrincipale.setPrefHeight(height);
        fenetrePrincipale.setPrefWidth(width);
    }
    
    /**
     * Commence une nouvelle partie. Le Maître du Jeu est initialisé, un joueur est ajouté, et le paquet de cartes est
     * mélangé. La fenêtre principale est mise à jour pour afficher les informations de la partie.
     */
    public void demarrerUnePartie() {
        // Crée un nouveau Maître du Jeu avec un paquet de 13 cartes par couleur.
        maitre = new MaitreDuJeu(13);
        
        // Ajoute un joueur avec le pseudo entré dans le champ de texte.
        maitre.addJoueur(new Joueur(tfPseudo.getText()));
        if (!tfPseudo.getText().equals("")) lblPseudo.setText(tfPseudo.getText());
        
        // Rend visible la HBox de score et cache le champ de texte pour le pseudo.
        scoreHBox.setVisible(true);
        tfPseudo.setVisible(false);
        
        // Mélange le paquet de cartes
        maitre.getPaquet().melanger();
        System.out.println("Maitre du jeu : Je mélange le paquet avant de commencer.");
        
        // Place chaque carte au centre de la fenêtre
        for (Carte carte : maitre.getPaquet().getCartes()) {
            carte.setLayoutX(width / 2 - carte.getFitWidth() / 2);
            carte.setLayoutY(height / 2 - carte.getFitHeight() / 2);
        }
        
        // Ajoute toutes les cartes à la fenêtre principale
        fenetrePrincipale.getChildren().addAll(faireCorrespondreLordreDesCartesDansSddAvecIHM(maitre.getPaquet().getCartes()));
        
        // Place le bouton "startGame" au centre de la fenêtre et le met au premier plan
        startGame.setLayoutX(width / 2 - startGame.getWidth() / 2);
        startGame.setLayoutY(height / 2 - startGame.getHeight() / 2);
        startGame.toFront();
        startGame.setText("Let's go !");
        
        // Définit l'action du bouton pour commencer la distribution des cartes lorsque cliqué
        startGame.setOnAction(e -> distribuerCarte());
        
        // Met la HBox de score au premier plan
        scoreHBox.toFront();
    }
    
    /**
     * Distribue les cartes aux joueurs. Les cartes sont retirées du paquet de cartes, puis une animation de
     * distribution de cartes est effectuée. L'ordre de distribution par qui commencer à distribuer est déterminé de
     * manière aléatoire. Les barres de progression sont mises à jour pour refléter la progression de la distribution,
     * la barre de progression rempli à moitié correspond au nombre de carte total divisé par 2.
     * <p>
     * Attention, il faut corriger le fait que les cartes sont distribuées par le bas pour les joueurs...
     */
    public void distribuerCarte() {
        System.out.println("Maitre du jeu : Je distribue les cartes...");
        // Cache le bouton "startGame"
        startGame.setVisible(false);
        
        // Inverse la direction de la barre de progression de l'IA
        progressBarIa.setRotate(180);
        
        // Mélange l'ordre des joueurs pour déterminer l'ordre de distribution
        Joueur ia = maitre.getJoueurs().get(0);
        Joueur nonIa = maitre.getJoueurs().get(1);
        Collections.shuffle(maitre.getJoueurs());
        
        // Obtient la liste des cartes à distribuer
        ArrayList<Carte> cartesADistribuer = maitre.getPaquet().getCartes();
        
        // Crée une nouvelle transition séquentielle pour l'animation de distribution
        SequentialTransition st = new SequentialTransition();
        double offsetYForPerseptive = 0;
        
        // Calcule le pas de progression pour chaque carte distribuée
        double stepProgress = 1.0 / cartesADistribuer.size();
        double progress = stepProgress;
        
        // Continue à distribuer les cartes jusqu'à ce qu'il n'y en ait plus
        while (!cartesADistribuer.isEmpty()) {
            for (Joueur joueur : maitre.getJoueurs()) {
                // Retire la première carte de la liste
                Carte carteADistribuer = cartesADistribuer.remove(0);
                
                // Crée une nouvelle transition de translation pour l'animation de distribution de la carte
                TranslateTransition tt = new TranslateTransition(Duration.millis(100), carteADistribuer);
                
                // Configure l'animation et la barre de progression en fonction du joueur
                if (joueur.getPseudo().equals("Brutuse")) {
                    tt.setByY(-315 + offsetYForPerseptive);
                    double newProgress = progress;
                    tt.setOnFinished(e -> progressBarNonIa.setProgress(newProgress));
                } else {
                    tt.setByY(350 - offsetYForPerseptive);
                    double newProgress = progress;
                    tt.setOnFinished(e -> progressBarIa.setProgress(newProgress));
                }
                
                offsetYForPerseptive += 0.25;
                
                // Ajoute la transition de translation à la transition séquentielle
                st.getChildren().add(tt);
            }
            progress += stepProgress;
        }
        
        // Joue l'animation globale séquentielle de distribution des cartes
        st.play();
        st.setOnFinished(e -> {
            System.out.println("Maitre du jeu : Distribution des cartes terminées, bonne chance !");
        });
        
        // Cache le bouton "Start Game"
        startGame.setVisible(false);
    }
    
    /**
     * Prend en paramètre une liste de cartes et retourne une nouvelle liste avec l'ordre des cartes inversé. Cette
     * méthode est utile pour s'assurer que l'ordre des cartes affichées correspond à l'ordre des cartes dans la sdd.
     *
     * @param cartes une ArrayList de cartes dont l'ordre doit être inversé.
     *
     * @return une nouvelle ArrayList de cartes avec l'ordre inversé.
     */
    public ArrayList<Carte> faireCorrespondreLordreDesCartesDansSddAvecIHM(ArrayList<Carte> cartes) {
        // Crée une nouvelle liste de cartes qui est une copie de la liste de cartes en entrée
        ArrayList<Carte> cartesReversed = new ArrayList<>(cartes);
        
        // Inverse l'ordre des cartes dans la nouvelle liste
        Collections.reverse(cartesReversed);
        
        // Retourne la nouvelle liste de cartes
        return cartesReversed;
    }
    
}

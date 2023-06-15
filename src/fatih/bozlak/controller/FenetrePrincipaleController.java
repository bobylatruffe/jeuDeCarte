package fatih.bozlak.controller;

import fatih.bozlak.modele.Carte;
import fatih.bozlak.modele.Joueur;
import fatih.bozlak.modele.MaitreDuJeu;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class FenetrePrincipaleController implements Initializable {
    public static final double width = 1000;
    public static final double height = 700;
    private MaitreDuJeu maitre;
    private double stepProgressBar;
    
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
    
    /**
     * Méthode pour distribuer les cartes aux joueurs. Cette méthode va progressivement distribuer les cartes de la pile
     * à chaque joueur, en ajustant les progress bars respectives des joueurs, jusqu'à ce que la pile soit vide.
     */
    // TODO: Rajouter animation pour distribution des cartes
    public void distribuerLesCartesAuxJoueurs() {
        // Inversion de l'orientation de la barre de progression de l'IA
        progressBarIa.setScaleX(-1);
        
        // Définir le pas de progression de la barre de progression.
        // Il est défini en fonction du nombre total de cartes dans le paquet du maitre du jeu
        stepProgressBar = 1.0 / maitre.getPaquet().getCartes().size();
        
        // Variable utilisée pour modifier la perspective lors de la distribution des cartes
        double forPerspective = 0.0;
        
        System.out.println("Maitre du jeu : Je distribue les cartes...");
        
        // Boucle tant qu'il y a des cartes dans le paquet du maitre du jeu
        while (!maitre.getPaquet().getCartes().isEmpty()) {
            
            // Parcourir chaque joueur
            for (Joueur joueur : maitre.getJoueurs()) {
                // Retirer la première carte du paquet
                Carte carte = maitre.getPaquet().getCartes().remove(0);
                
                // Donner cette carte au joueur courant
                maitre.donnerUneCarteAUnJoueur(joueur, carte);
                
                // Si le joueur courant est l'IA, ajuster la position et la progression de la barre de l'IA
                if (joueur.getPseudo().equals(MaitreDuJeu.nomIa)) {
                    carte.setLayoutY(0 - carte.getFitHeight() / 2 + forPerspective);
                    progressBarIa.setProgress(progressBarIa.getProgress() + stepProgressBar);
                }
                // Si le joueur courant n'est pas l'IA, ajuster la position et la progression de la barre du joueur non-IA
                else {
                    carte.setLayoutY(height - carte.getFitHeight() / 2 - forPerspective);
                    progressBarNonIa.setProgress(progressBarNonIa.getProgress() + stepProgressBar);
                }
            }
            
            // Augmentation de la perspective
            forPerspective += 0.5;
        }
        
        System.out.println("Maitre du jeu : Distribution des cartes terminées.");
        
        // Donner la main à un autre contrôlleur qui va contrôler le jeu.
        new JeuControlleur(maitre, progressBarIa, progressBarNonIa);
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
        startGame.setVisible(false);
        
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
        
        distribuerLesCartesAuxJoueurs();
        
        // Met la HBox de score au premier plan
        scoreHBox.toFront();
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
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fenetrePrincipale.setPrefHeight(height);
        fenetrePrincipale.setPrefWidth(width);
    }
}
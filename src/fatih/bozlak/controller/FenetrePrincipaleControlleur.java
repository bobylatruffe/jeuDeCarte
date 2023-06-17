package fatih.bozlak.controller;

import fatih.bozlak.modele.Carte;
import fatih.bozlak.modele.ErreurPaquetDeCarte;
import fatih.bozlak.modele.Joueur;
import fatih.bozlak.modele.MaitreDuJeu;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
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
    
    // TODO: CREE UN COMPOSANT VIDE POUR RECEVOIR LES EVENEMTNS à la place de ça ...
    
    @FXML
    public Label pseudoIa;
    private final double width = 1000.0;
    
    private final double height = 800.0;
    
    private double stepProgressBar;
    private MaitreDuJeu maitre;
    
    private GestionnaireAnimation gestionnaireAnimation;
    
    private ArrayList<Joueur> joueurs;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gestionnaireAnimation = new GestionnaireAnimation(this);
        
        maitre = new MaitreDuJeu(13);
        maitre.addJoueur(new Joueur("BobyLatruffe"));

//        maitre.getPaquet().melanger();
//        maitre.log("Paquet de carte mélangé.");
        
        stepProgressBar = 1.0 / maitre.getPaquet().getCartes().size();
        
        placerLePaquetAuCentre();
        
        progressBarNonIa.setOnMouseClicked(e -> {
            distribuerLesCartesAuxJoueurs();
        });
    }
    
    private void placerLePaquetAuCentre() {
        for (Carte carte : maitre.getPaquet().getCartes()) {
            fenetrePrincipale.getChildren().add(carte);
            carte.setLayoutX(width / 2 - carte.getFitWidth() / 2);
            carte.setLayoutY(height / 2 - carte.getFitHeight() / 2);
            
            carte.toBack();
        }
    }
    
    /**
     * Cette méthode est responsable de la distribution des cartes aux joueurs. Elle gère également deux événements
     * :
     * <p>
     * - 'CARTE_DISTRIBUEE' lorsqu'une carte est distribuée à un joueur.
     * <p>
     * - 'CARTE_DISTRIBUEES' lorsque toutes les cartes ont été distribuées.
     */
    public void distribuerLesCartesAuxJoueurs() {
        maitre.log("Je distribue les cartes aux joueurs...");
        
        // Distribution initiale d'une carte à un joueur.
        distribuerUneCarteAUnJoueur();
        
        // Événement déclenché chaque fois qu'une carte est distribuée.
        pseudoIa.addEventHandler(gestionnaireAnimation.CARTE_DISTRIBUEE, new EventHandler<AnimationEvenement>() {
            @Override
            public void handle(AnimationEvenement animationEvenement) {
                distribuerUneCarteAUnJoueur();
            }
        });
        
        // Événement déclenché lorsque toutes les cartes ont été distribuées.
        pseudoIa.addEventHandler(gestionnaireAnimation.CARTE_DISTRIBUEES, new EventHandler<AnimationEvenement>() {
            @Override
            public void handle(AnimationEvenement animationEvenement) {
                maitre.log("Distribution des cartes terminées.");
            }
        });
    }
    
    /**
     * Cette méthode gère la distribution d'une carte à un joueur spécifique. Elle vérifie si des cartes sont
     * disponibles dans le paquet. Si le paquet est vide, un événement 'CARTE_DISTRIBUEES' est déclenché. Elle extrait
     * une carte du paquet et la donne au joueur. Si le joueur est l'IA, elle définit un attribut spécifique sur la
     * carte. Enfin, elle déclenche l'animation de la distribution de la carte.
     */
    public void distribuerUneCarteAUnJoueur() {
        // Initialiser la liste des joueurs si elle est vide.
        if (this.joueurs == null || this.joueurs.isEmpty()) {
            this.joueurs = new ArrayList<>(maitre.getJoueurs());
        }
        
        // Si le paquet de cartes est vide, déclencher l'événement 'CARTE_DISTRIBUEES'.
        if (maitre.getPaquet().getCartes().isEmpty()) {
            pseudoIa.fireEvent(new AnimationEvenement(gestionnaireAnimation.CARTE_DISTRIBUEES));
        }
        
        try {
            // Récupérer la prochaine carte du paquet.
            Carte carteADistribuer = maitre.getPaquet().pop();
            
            // Retirer le premier joueur de la liste pour lui donner la carte.
            Joueur joueurAQuiDonner = joueurs.remove(0);
            
            // Si le joueur est l'IA, définir la carte comme étant pour l'IA.
            if (joueurAQuiDonner.getPseudo().equals(MaitreDuJeu.nomIa)) {
                carteADistribuer.setForIa(true);
            }
            
            // Distribuer la carte au joueur et déclencher l'animation correspondante.
            maitre.donnerUneCarteAUnJoueur(joueurAQuiDonner, carteADistribuer);
            gestionnaireAnimation.donnerUneCarteAUnJoueur(carteADistribuer);
        } catch (ErreurPaquetDeCarte e) {
            // Si une erreur se produit lors de la manipulation du paquet de cartes, l'erreur est simplement ignorée.
            // Cela pourrait être amélioré pour gérer les erreurs de manière plus robuste.
        }
    }
    
    public double getWidth() {
        return width;
    }
    
    public double getHeight() {
        return height;
    }
    
    public double getStepProgressBar() {
        return stepProgressBar;
    }
    
    public HBox getScoreBar() {
        return scoreBar;
    }
}

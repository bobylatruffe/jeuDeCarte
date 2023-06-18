package fatih.bozlak.controller;

import fatih.bozlak.modele.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
    
    private Joueur lastGagant;
    
    private boolean isFaceDecouverte = true;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gestionnaireAnimation = new GestionnaireAnimation(this);
        
        maitre = new MaitreDuJeu(3);
        maitre.addJoueur(new Joueur("BobyLatruffe"));
        
        maitre.getPaquet().melanger();
        maitre.log("Paquet de carte mélangé.");
        
        stepProgressBar = 1.0 / maitre.getPaquet().getCartes().size();
        
        placerLePaquetAuCentre();
        
        // TODO à supprimer !
        progressBarNonIa.setOnMouseClicked(e -> {
            distribuerLesCartesAuxJoueurs();
        });
        
        pseudoIa.addEventHandler(gestionnaireAnimation.JOUEUR_A_JOUEE, new EventHandler<AnimationEvenement>() {
            @Override
            public void handle(AnimationEvenement animationEvenement) {
                joueurJoueUneCarte(maitre.getJoueurs().get(0));
            }
        });
        
        pseudoIa.addEventHandler(gestionnaireAnimation.MANCHE_JOUEE, new EventHandler<AnimationEvenement>() {
            @Override
            public void handle(AnimationEvenement animationEvenement) {
                if (isFaceDecouverte) {
                    gestionnaireAnimation.showValueCards(maitre.getAComparer());
                } else {
                    isFaceDecouverte = true;
                    gestionnaireAnimation.arrangerPanierPourBataille(maitre.getPanier());
                }
            }
        });
        
        pseudoIa.addEventHandler(gestionnaireAnimation.VALEURS_AFFICHEES, new EventHandler<AnimationEvenement>() {
            @Override
            public void handle(AnimationEvenement animationEvenement) {
                fight();
            }
        });
        
        pseudoIa.addEventHandler(gestionnaireAnimation.PANIER_DISTRIBUEE, new EventHandler<AnimationEvenement>() {
            @Override
            public void handle(AnimationEvenement animationEvenement) {
                maitre.distribuerPanierAuGagnant(lastGagant);
            }
        });
        
        pseudoIa.addEventHandler(gestionnaireAnimation.BATAILLE, new EventHandler<AnimationEvenement>() {
            @Override
            public void handle(AnimationEvenement animationEvenement) {
                isFaceDecouverte = false;
            }
        });
        
        progressBarIa.setOnMouseClicked(e -> {
            joueurJoueUneCarte(maitre.getJoueurs().get(1));
        });
    }
    
    public void fight() {
        try {
            lastGagant = maitre.quiGagne();
            if (lastGagant != null) {
                gestionnaireAnimation.retournerLesCartesFacesCacheeDuPanier(maitre.getPanier());
                gestionnaireAnimation.donnerLesCartesDuPanierAuVainqeur(lastGagant, maitre.getPanier());
            } else {
                gestionnaireAnimation.arrangerPanierPourBataille(maitre.getPanier());
                pseudoIa.fireEvent(new AnimationEvenement((gestionnaireAnimation.BATAILLE)));
            }
        } catch (ErreurMaitreDuJeu e) {
            ((Stage) pseudoIa.getScene().getWindow()).close();
        }
    }
    
    private void joueurJoueUneCarte(Joueur joueurQuiJoue) {
        Carte carteJouee = maitre.demanderAUnJoueurDeJouer(joueurQuiJoue, isFaceDecouverte);
        
        if (carteJouee != null) {
            gestionnaireAnimation.joueurJoueUneCarte(carteJouee);
        } else if (joueurQuiJoue.getPseudo().equals(MaitreDuJeu.nomIa)) {
            pseudoIa.fireEvent(new AnimationEvenement(gestionnaireAnimation.VALEURS_AFFICHEES));
        } else {
            pseudoIa.fireEvent(new AnimationEvenement(gestionnaireAnimation.JOUEUR_A_JOUEE));
        }
    }
    
    /**
     * Cette méthode est responsable de la distribution des cartes aux joueurs. Elle gère également deux événements :
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
    
    /**
     * Cette méthode est responsable de placer le paquet de cartes au centre de la fenêtre principale. Elle parcourt
     * toutes les cartes du paquet et les ajoute à la fenêtre principale. Chaque carte est positionnée au centre de la
     * fenêtre et envoyée à l'arrière des autres éléments.
     */
    private void placerLePaquetAuCentre() {
        // Parcourir toutes les cartes du paquet.
        for (Carte carte : maitre.getPaquet().getCartes()) {
            // Ajouter la carte à la fenêtre principale.
            fenetrePrincipale.getChildren().add(carte);
            
            // Calculer la position X de la carte de manière à la centrer horizontalement.
            carte.setLayoutX(width / 2 - carte.getFitWidth() / 2);
            
            // Calculer la position Y de la carte de manière à la centrer verticalement.
            carte.setLayoutY(height / 2 - carte.getFitHeight() / 2);
            
            // Envoyer la carte à l'arrière de la fenêtre principale pour qu'elle n'obstrue pas les autres éléments.
            carte.toBack();
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
    
    public ProgressBar getProgressBarIa() {
        return progressBarIa;
    }
    
    public ProgressBar getProgressBarNonIa() {
        return progressBarNonIa;
    }
}

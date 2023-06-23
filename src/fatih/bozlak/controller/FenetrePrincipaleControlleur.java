package fatih.bozlak.controller;

import fatih.bozlak.modele.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Cette classe permet de gérer tous ce qui se passe dans l'interface graphique en utilisant le maitre du jeu pour
 * contrôler le jeu de bataille.
 */
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
    public Label pseudoIa;

    private final String pseudoNonIaNoFxml;

    private final double width = 1000.0;
    private final double height = 800.0;

    private double stepProgressBar;

    private final MaitreDuJeu maitre;
    private GestionnaireAnimation gestionnaireAnimation;

    private ArrayList<Joueur> joueurs;

    private Joueur lastGagant;

    private boolean isFaceDecouverte = true;

    private final int nbDeCartesParCouleur;

    /**
     * Constructeur de la classe FenetrePrincipaleControlleur.
     *
     * @param pseudo               Le pseudo du joueur.
     * @param nbDeCartesParCouleur Le nombre de cartes par couleur dans le jeu.
     */
    public FenetrePrincipaleControlleur(String pseudo, int nbDeCartesParCouleur) {
        // Mémoriser le nombre de cartes par couleur
        this.nbDeCartesParCouleur = nbDeCartesParCouleur;

        // Créer une nouvelle instance du Maître du Jeu avec le nombre de cartes par couleur spécifié
        maitre = new MaitreDuJeu(nbDeCartesParCouleur);

        // Ajouter un nouveau joueur au Maître du Jeu avec le pseudo spécifié
        maitre.addJoueur(new Joueur(pseudo));
        pseudoNonIaNoFxml = pseudo;

        // Mélanger le paquet de cartes du Maître du Jeu
        maitre.getPaquet().melanger();
        maitre.log("Paquet de carte mélangé.");
    }

    /**
     * Cette méthode est automatiquement appelée après que les éléments FXML ont été chargés. Elle est utilisée pour
     * initialiser la logique du contrôleur.
     *
     * @param url            l'emplacement utilisé pour résoudre les chemins relatifs pour l'objet root.
     * @param resourceBundle les ressources utilisées pour localiser l'objet root.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialiser le texte du pseudo
        pseudoNonIa.setText(pseudoNonIaNoFxml);

        // Initialiser le gestionnaire d'animation
        gestionnaireAnimation = new GestionnaireAnimation(this);

        // Initialiser la barre de progression
        stepProgressBar = 1.0 / maitre.getPaquet().getCartes().size();

        // Placer le paquet de cartes au centre de la scène
        placerLePaquetAuCentre();

        // Distribuer les cartes aux joueurs
        distribuerLesCartesAuxJoueurs();

        // Ajouter un écouteur à la propriété scene de la fenêtre principale pour détecter les appuis sur la touche espace.
        // Pour ça c'est ChatGPT qui m'a aider !
        fenetrePrincipale.sceneProperty().addListener(new ChangeListener<Scene>() {
            @Override
            public void changed(ObservableValue<? extends Scene> observableValue, Scene oldScene, Scene newScene) {
                newScene.setOnKeyPressed(keyEvent -> {
                    if (keyEvent.getCode() == KeyCode.SPACE)
                        joueurJoueUneCarte(maitre.getJoueurs().get(1));
                });
            }
        });

        // Ajouter plusieurs gestionnaires d'événements pour gérer les différentes actions du jeu
        // Ces gestionnaires d'événements réagissent à des événements personnalisés définis dans le GestionnaireAnimation
        // JOUEUR_A_JOUEE: quand un joueur a joué une carte
        // MANCHE_JOUEE: quand une manche a été jouée
        // VALEURS_AFFICHEES: quand les valeurs des cartes sont affichées
        // PANIER_DISTRIBUEE: quand le panier de cartes a été distribué
        // BATAILLE: quand il y a une bataille
        // END_GAME: quand le jeu se termine
        // Chaque gestionnaire d'événements a une méthode handle() qui définit ce qui se passe lorsqu'un événement se produit.
        pseudoIa.addEventHandler(GestionnaireAnimation.JOUEUR_A_JOUEE, new EventHandler<AnimationEvenement>() {
            @Override
            public void handle(AnimationEvenement animationEvenement) {
                joueurJoueUneCarte(maitre.getJoueurs().get(0));
            }
        });

        pseudoIa.addEventHandler(GestionnaireAnimation.MANCHE_JOUEE, new EventHandler<AnimationEvenement>() {
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

        pseudoIa.addEventHandler(GestionnaireAnimation.VALEURS_AFFICHEES, new EventHandler<AnimationEvenement>() {
            @Override
            public void handle(AnimationEvenement animationEvenement) {
                fight();
            }
        });

        pseudoIa.addEventHandler(GestionnaireAnimation.PANIER_DISTRIBUEE, new EventHandler<AnimationEvenement>() {
            @Override
            public void handle(AnimationEvenement animationEvenement) {
                maitre.distribuerPanierAuGagnant(lastGagant);
            }
        });

        pseudoIa.addEventHandler(GestionnaireAnimation.BATAILLE, new EventHandler<AnimationEvenement>() {
            @Override
            public void handle(AnimationEvenement animationEvenement) {
                isFaceDecouverte = false;
            }
        });

        pseudoIa.addEventHandler(GestionnaireAnimation.END_GAME, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Une autre partie ?", ButtonType.YES, ButtonType.NO);
                alert.setHeaderText(null);
                alert.setGraphic(null);
                alert.setTitle(((AnimationEvenement) event).getMsg());
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fatih/bozlak/view/Accueil.fxml"));
                    loader.setController(new AccueilControlleur(pseudoNonIaNoFxml, nbDeCartesParCouleur));

                    try {
                        Parent root = loader.load();

                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.setResizable(false);
                        stage.show();

                        // C'est une astuce connu de JavaFX pour récupérer la fenêtre courante
                        ((Stage) pseudoIa.getScene().getWindow()).close();

                    } catch (IOException err) {
                        System.out.println("Impossible de charger le fichier FXML!");
                    }

                } else {
                    // C'est une astuce connu de JavaFX pour récupérer la fenêtre courante
                    ((Stage) pseudoIa.getScene().getWindow()).close();
                }
            }
        });
    }

    /**
     * Cette méthode gère le déroulement d'une bataille dans le jeu. Elle détermine qui est le gagnant de la bataille et
     * effectue les actions appropriées en conséquence.
     */
    public void fight() {
        try {
            // Détermine le gagnant de la bataille
            lastGagant = maitre.quiGagne();

            if (lastGagant != null) {
                // S'il y a un gagnant, retourne les cartes faces cachées du panier
                // et donne les cartes du panier au vainqueur
                gestionnaireAnimation.retournerLesCartesFacesCacheeDuPanier(maitre.getPanier());
                gestionnaireAnimation.donnerLesCartesDuPanierAuVainqeur(lastGagant, maitre.getPanier());
            } else {
                // S'il n'y a pas de gagnant, arrange le panier pour une autre bataille
                // et déclenche un événement BATAILLE
                gestionnaireAnimation.arrangerPanierPourBataille(maitre.getPanier());
                pseudoIa.fireEvent(new AnimationEvenement((GestionnaireAnimation.BATAILLE)));
            }
        } catch (ErreurMaitreDuJeu e) {
            // Si une erreur se produit pendant la détermination du gagnant (qu'un des joueurs (ou les deux) n'a plus de carte donc), imprime le message d'erreur
            System.out.println(e.getMessage());

            // Déclenche un événement END_GAME avec le message d'erreur comme titre de l'alerte
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    pseudoIa.fireEvent(new AnimationEvenement(GestionnaireAnimation.END_GAME, e.getMessage()));
                }
            });
        }
    }

    /**
     * Cette méthode permet à un joueur de jouer une carte.
     *
     * @param joueurQuiJoue Le joueur qui est actuellement en train de jouer.
     */
    private void joueurJoueUneCarte(Joueur joueurQuiJoue) {
        // Demande au joueur de jouer une carte. Le joueur peut soit jouer une carte face découverte,
        // soit une carte face cachée, en fonction de la valeur de isFaceDecouverte.
        Carte carteJouee = maitre.demanderAUnJoueurDeJouer(joueurQuiJoue, isFaceDecouverte);

        if (carteJouee != null) {
            // Si une carte a été jouée, lance une animation pour montrer que le joueur a joué une carte
            gestionnaireAnimation.joueurJoueUneCarte(carteJouee);
        } else if (joueurQuiJoue.getPseudo().equals(MaitreDuJeu.nomIa)) {
            // Si le joueur est l'IA, lance un événement VALEURS_AFFICHEES pour afficher les valeurs des cartes jouées par les deux joueurs.
            pseudoIa.fireEvent(new AnimationEvenement(GestionnaireAnimation.VALEURS_AFFICHEES));
        } else {
            pseudoIa.fireEvent(new AnimationEvenement(GestionnaireAnimation.JOUEUR_A_JOUEE));
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
        pseudoIa.addEventHandler(GestionnaireAnimation.CARTE_DISTRIBUEE, new EventHandler<AnimationEvenement>() {
            @Override
            public void handle(AnimationEvenement animationEvenement) {
                distribuerUneCarteAUnJoueur();
            }
        });

        // Événement déclenché lorsque toutes les cartes ont été distribuées.
        pseudoIa.addEventHandler(GestionnaireAnimation.CARTE_DISTRIBUEES, new EventHandler<AnimationEvenement>() {
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
            pseudoIa.fireEvent(new AnimationEvenement(GestionnaireAnimation.CARTE_DISTRIBUEES));
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

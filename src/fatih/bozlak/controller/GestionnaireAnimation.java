package fatih.bozlak.controller;

import fatih.bozlak.modele.Carte;
import fatih.bozlak.modele.Joueur;
import fatih.bozlak.modele.MaitreDuJeu;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * Classe GestionnaireAnimation pour gérer les différents animations dans le jeu.
 */
public class GestionnaireAnimation {
    public static final EventType<AnimationEvenement> END_GAME = new EventType<>(Event.ANY, "END_GAME");
    public static final EventType<AnimationEvenement> BATAILLE = new EventType<>(Event.ANY, "BATAILLE");
    public static final EventType<AnimationEvenement> PANIER_DISTRIBUEE = new EventType<>(Event.ANY, "PANIER_DISTRIBUEE");
    public static final EventType<AnimationEvenement> VALEURS_AFFICHEES = new EventType<>(Event.ANY, "VALEURS_AFFICHEES");
    public static final EventType<AnimationEvenement> MANCHE_JOUEE = new EventType<>(Event.ANY, "MANCHE_JOUEE");
    public static final EventType<AnimationEvenement> CARTE_DISTRIBUEE = new EventType<>(Event.ANY, "CARTE_DISTRIBUEE");
    public static final EventType<AnimationEvenement> CARTE_DISTRIBUEES = new EventType<>(Event.ANY, "CARTE_DISTRIBUEES");
    public static final EventType<AnimationEvenement> JOUEUR_A_JOUEE = new EventType<>(Event.ANY, "JOUEUR_A_JOUEE");
    
    private final double dureeAnimation = 100;
    private final double distanceDuPanierAuxJoueurs;
    private final double offsetCarteDansPanier = 30.0;
    
    private final FenetrePrincipaleControlleur mainWindow;
    
    public GestionnaireAnimation(FenetrePrincipaleControlleur fenetrePrincipaleControlleur) {
        this.mainWindow = fenetrePrincipaleControlleur;
        distanceDuPanierAuxJoueurs = this.mainWindow.getHeight() / 2;
    }
    
    /**
     * Incrémente la barre de progression du joueur non IA. Le montant de l'incrémentation est défini par l'attribut
     * stepProgressBar de la fenêtre principale.
     */
    private void incPrograssbarNonIa() {
        // On récupère la progression actuelle, on y ajoute la valeur de stepProgressBar
        // et on met à jour la barre de progression
        mainWindow.getProgressBarNonIa().setProgress(mainWindow.getProgressBarNonIa().getProgress() + mainWindow.getStepProgressBar());
    }
    
    /**
     * Décrémente la barre de progression du joueur non IA. Le montant de la décrémentation est défini par l'attribut
     * stepProgressBar de la fenêtre principale.
     */
    private void decProgressBarNonIa() {
        // On récupère la progression actuelle, on en soustrait la valeur de stepProgressBar
        // et on met à jour la barre de progression
        mainWindow.getProgressBarNonIa().setProgress(mainWindow.getProgressBarNonIa().getProgress() - mainWindow.getStepProgressBar());
    }
    
    /**
     * Incrémente la barre de progression de l'IA. Le montant de l'incrémentation est défini par l'attribut
     * stepProgressBar de la fenêtre principale.
     */
    private void incProgressBarIa() {
        // On récupère la progression actuelle, on y ajoute la valeur de stepProgressBar
        // et on met à jour la barre de progression
        mainWindow.getProgressBarIa().setProgress(mainWindow.getProgressBarIa().getProgress() + mainWindow.getStepProgressBar());
    }
    
    /**
     * Décrémente la barre de progression de l'IA. Le montant de la décrémentation est défini par l'attribut
     * stepProgressBar de la fenêtre principale.
     */
    private void decProgressBarIa() {
        // On récupère la progression actuelle, on en soustrait la valeur de stepProgressBar
        // et on met à jour la barre de progression
        mainWindow.getProgressBarIa().setProgress(mainWindow.getProgressBarIa().getProgress() - mainWindow.getStepProgressBar());
    }
    
    /**
     * Cette méthode anime le processus de donner une carte à un joueur.
     *
     * @param carteADonner La carte à donner au joueur.
     */
    public void donnerUneCarteAUnJoueur(Carte carteADonner) {
        // Création d'une nouvelle animation de translation
        TranslateTransition tt = new TranslateTransition(Duration.millis(dureeAnimation), carteADonner);
        
        // Si la carte est pour l'IA, elle est déplacée vers le haut
        // Sinon, elle est déplacée vers le bas
        // De plus, la barre de progression du joueur qui reçoit la carte est mise à jour
        if (carteADonner.isForIa()) {
            tt.setByY(-distanceDuPanierAuxJoueurs);
            incProgressBarIa();
        } else {
            tt.setToY(distanceDuPanierAuxJoueurs);
            incPrograssbarNonIa();
        }
        
        // La carte à donner est déplacée devant les autres cartes
        carteADonner.toFront();
        // La barre de score est également déplacée devant les autres éléments
        mainWindow.getScoreBar().toFront();
        
        // Démarrage de l'animation
        tt.play();
        tt.setOnFinished(e -> {
            // A la fin de l'animation, la carte est déplacée derrière les autres cartes
            // Et un événement est envoyé pour indiquer qu'une carte a été distribuée
            carteADonner.toBack();
            mainWindow.pseudoIa.fireEvent(new AnimationEvenement(CARTE_DISTRIBUEE));
        });
    }
    
    /**
     * Cette méthode effectue une animation lorsqu'un joueur joue une carte.
     *
     * @param carteJouee La carte jouée par le joueur.
     */
    public void joueurJoueUneCarte(Carte carteJouee) {
        // Création d'une nouvelle animation de translation
        TranslateTransition tt = new TranslateTransition(Duration.millis(dureeAnimation), carteJouee);
        
        // La carte jouée est déplacée devant les autres cartes
        carteJouee.toFront();
        
        // Si la carte appartient à l'IA, elle est déplacée vers le haut et vers la droite
        // Sinon, elle est déplacée vers le bas et vers la gauche
        if (carteJouee.isForIa()) {
            tt.setByY(distanceDuPanierAuxJoueurs);
            tt.setByX(carteJouee.getFitWidth() / 2 + offsetCarteDansPanier);
        } else {
            tt.setByY(-distanceDuPanierAuxJoueurs);
            tt.setByX(-carteJouee.getFitWidth() / 2 - offsetCarteDansPanier);
        }
        
        // Démarrage de l'animation
        tt.play();
        tt.setOnFinished(e -> {
            // A la fin de l'animation, si la carte appartient à l'IA, un événement est envoyé pour indiquer que la manche est jouée
            // Sinon, un événement est envoyé pour indiquer que le joueur a joué
            // De plus, la barre de progression du joueur qui a joué est mise à jour
            if (carteJouee.isForIa()) {
                mainWindow.pseudoIa.fireEvent(new AnimationEvenement(MANCHE_JOUEE));
                decProgressBarIa();
            } else {
                mainWindow.pseudoIa.fireEvent(new AnimationEvenement(JOUEUR_A_JOUEE));
                decProgressBarNonIa();
            }
        });
    }
    
    /**
     * Cette méthode effectue une animation pour révéler la valeur des cartes à comparer.
     *
     * @param cartesAComparer Le tableau de cartes dont les valeurs doivent être révélées.
     */
    public void showValueCards(Carte[] cartesAComparer) {
        // Parcours de toutes les cartes à comparer (toujours 2 cartes)
        for (Carte carte : cartesAComparer) {
            // Création d'une nouvelle animation de rotation
            RotateTransition rt = new RotateTransition(Duration.millis(dureeAnimation), carte);
            rt.setAxis(Rotate.Y_AXIS); // L'animation de rotation se fait autour de l'axe Y
            rt.setByAngle(90); // La carte est tournée de 90 degrés
            rt.setDelay(Duration.millis(dureeAnimation)); // Délai avant le début de l'animation
            
            // Démarrage de l'animation
            rt.play();
            rt.setOnFinished(e -> {
                // A la fin de la première rotation, la carte est retournée pour révéler sa valeur
                carte.setFaceDecouverte(true);
                
                // Création d'une deuxième animation de rotation
                RotateTransition rt2 = new RotateTransition(Duration.millis(dureeAnimation), carte);
                rt2.setAxis(Rotate.Y_AXIS); // L'animation de rotation se fait autour de l'axe Y
                rt2.setByAngle(-90); // La carte est tournée de -90 degrés pour revenir à sa position initiale
                
                // Démarrage de la deuxième animation
                rt2.play();
                rt2.setOnFinished(ee -> {
                    // Si la carte appartient à l'IA, un événement est envoyé pour indiquer que les valeurs ont été révélées
                    if (carte.isForIa()) {
                        mainWindow.pseudoIa.fireEvent(new AnimationEvenement(VALEURS_AFFICHEES));
                    }
                });
            });
        }
    }
    
    /**
     * Cette méthode retourne toutes les cartes face cachée du panier.
     *
     * @param panier Le panier contenant les cartes à retourner.
     */
    public void retournerLesCartesFacesCacheeDuPanier(ArrayList<Carte> panier) {
        // Parcoure toutes les cartes dans le panier
        for (Carte carte : panier) {
            // Si la carte est face cachée
            if (!carte.isFaceDecouverte()) {
                // Retourne la carte pour la rendre visible
                carte.setFaceDecouverte(true);
            }
        }
    }
    
    /**
     * Cette méthode donne toutes les cartes du panier au joueur gagnant.
     *
     * @param gagnant Le joueur qui a gagné la bataille.
     * @param panier  Le panier contenant les cartes à donner.
     */
    public void donnerLesCartesDuPanierAuVainqeur(Joueur gagnant, ArrayList<Carte> panier) {
        // Crée une nouvelle transition séquentielle pour l'animation
        SequentialTransition st = new SequentialTransition();
        
        // Parcoure toutes les cartes dans le panier
        for (Carte carte : panier) {
            // Crée une nouvelle transition de translation pour l'animation de la carte
            TranslateTransition tt = new TranslateTransition(Duration.millis(dureeAnimation), carte);
            
            // Si le gagnant est l'IA, déplace la carte vers le haut
            // Sinon, déplace la carte vers le bas
            if (gagnant.getPseudo().equals(MaitreDuJeu.nomIa)) {
                tt.setByY(-distanceDuPanierAuxJoueurs);
            } else {
                tt.setByY(distanceDuPanierAuxJoueurs);
            }
            
            // Réinitialise la position horizontale de la carte
            tt.setToX(0);
            
            // A la fin de l'animation, met la carte en arrière et la tourne face cachée
            tt.setOnFinished(e -> {
                carte.toBack();
                carte.setFaceDecouverte(false);
            });
            
            // Ajoute la transition de translation à la transition séquentielle
            st.getChildren().add(tt);
        }
        
        // Définit un délai avant le début de l'animation
        st.setDelay(Duration.millis(1000));
        // Démarre l'animation
        st.play();
        
        // A la fin de l'animation, incrémente la barre de progression du gagnant pour chaque carte dans le panier
        // et lance un événement PANIER_DISTRIBUEE
        st.setOnFinished(e -> {
            panier.forEach(carte -> {
                if (gagnant.getPseudo().equals(MaitreDuJeu.nomIa)) incProgressBarIa();
                else incPrograssbarNonIa();
            });
            
            mainWindow.pseudoIa.fireEvent(new AnimationEvenement(PANIER_DISTRIBUEE));
        });
    }
    
    /**
     * Cette méthode arrange les cartes dans le panier pour préparer une nouvelle bataille. Arranger ? Déplacer les
     * cartes déjà dans le panier pour les mettres de côtés.
     *
     * @param panier Le panier contenant les cartes à arranger.
     */
    public void arrangerPanierPourBataille(ArrayList<Carte> panier) {
        // Parcoure toutes les cartes dans le panier
        for (Carte carte : panier) {
            // Crée une nouvelle transition de translation pour l'animation de la carte
            TranslateTransition tt = new TranslateTransition(Duration.millis(dureeAnimation), carte);
            
            // Si la carte appartient à l'IA, déplace la carte vers la droite
            // Sinon, déplace la carte vers la gauche
            if (carte.isForIa()) {
                tt.setByX(offsetCarteDansPanier);
            } else {
                tt.setByX(-offsetCarteDansPanier);
            }
            
            // Démarre l'animation
            tt.play();
        }
    }
    
}

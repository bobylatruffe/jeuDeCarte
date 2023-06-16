package fatih.bozlak.controller;

import fatih.bozlak.modele.*;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;

public class GestionnaireDesCartesEtJoueurs {
    private final Group cartesJoueurNonIa;
    private final Group cartesJoueurIa;
    private final Group cartesPanier;
    
    private final double offsetCartePanier = 20.0;
    private final double forPerspectiveCartesJoueur = 0.5;
    
    private final Pane fenetrePrincipale;
    
    private final GenerateurAnimations generateurAnimations;
    
    private final FenetrePrincipaleControlleur fenetrePrincipaleControlleur;
    
    private double distanceCartesJoueurIaAuPanier;
    private double distanceCartesJoueurNonIaAuPanier;
    
    private ProgressBar pbIa, pbNonIa;
    
    /**
     * Gestionnaire des cartes et joueurs pour un jeu de cartes.
     *
     * @param cartesJoueurNonIa le conteneur pour les cartes du joueur non-IA.
     * @param cartesJoueurIa    le conteneur pour les cartes du joueur IA.
     * @param cartesPanier      le conteneur pour le panier de cartes.
     * @param fenetrePrincipale la fenêtre principale du jeu.
     */
    public GestionnaireDesCartesEtJoueurs(Group cartesJoueurNonIa, Group cartesJoueurIa, Group cartesPanier, Pane fenetrePrincipale, FenetrePrincipaleControlleur fenetrePrincipaleControlleur) {
        this.cartesJoueurNonIa = cartesJoueurNonIa;
        this.cartesJoueurIa = cartesJoueurIa;
        this.cartesPanier = cartesPanier;
        
        this.fenetrePrincipaleControlleur = fenetrePrincipaleControlleur;
        this.fenetrePrincipale = fenetrePrincipale;
        
        this.pbIa = fenetrePrincipaleControlleur.getProgressBarIa();
        this.pbNonIa = fenetrePrincipaleControlleur.getProgressBarNonIa();
        
        this.generateurAnimations = new GenerateurAnimations();
        
        this.cartesJoueurIa.setLayoutX(fenetrePrincipale.getPrefWidth() / 2 - Carte.carteWidth / 2);
        this.cartesJoueurIa.setLayoutY(0 - Carte.carteHeight / 2 + 40);
        
        this.cartesPanier.setLayoutX(fenetrePrincipale.getPrefWidth() / 2 - Carte.carteWidth / 2);
        this.cartesPanier.setLayoutY(fenetrePrincipale.getPrefHeight() / 2 - Carte.carteHeight / 2);
        
        this.cartesJoueurNonIa.setLayoutX(fenetrePrincipale.getPrefWidth() / 2 - Carte.carteWidth / 2);
        this.cartesJoueurNonIa.setLayoutY(fenetrePrincipale.getPrefHeight() - Carte.carteHeight / 2 - 40);
    }
    
    /**
     * Ajoute une carte spécifique au panier.
     *
     * @param carte la carte à ajouter au panier.
     */
    public void placerUneCarteDansLePanier(Carte carte) {
        carte.setTranslateX(0);
        carte.setTranslateY(0);
        carte.setLayoutY(0);
        carte.setLayoutX(0);
        
        // Ajouter la carte au panier de cartes
        cartesPanier.getChildren().add(carte);
        
        // Si la carte appartient au joueur IA, ajuster la position x de la carte vers la droite
        if (carte.isForIa()) {
            carte.setTranslateX(Carte.carteWidth / 2 + offsetCartePanier);
            
            // Si la carte appartient au joueur non-IA, ajuster la position x de la carte vers la gauche
        } else {
            carte.setTranslateX(-Carte.carteWidth / 2 - offsetCartePanier);
        }
    }
    
    /**
     * Arrange les cartes dans le panier en appliquant une animation.
     * <p>
     * <p>
     * Cette méthode permet lors d'une bataille de décaler les autres cartes déjà présente dans la panier.
     */
    public void arrangerLePanier() {
        // Parcourir tous les noeuds (cartes) dans le panier
        for (Node carteDejaDansPanier : this.cartesPanier.getChildren()) {
            
            // Si la carte appartient au joueur IA, appliquer une animation de translation vers la droite
            if (((Carte) carteDejaDansPanier).isForIa()) {
                generateurAnimations.translation(carteDejaDansPanier, 250, +offsetCartePanier, null).play();
                
                // Sinon, si la carte appartient au joueur non-IA, appliquer une animation de translation vers la gauche
            } else {
                generateurAnimations.translation(carteDejaDansPanier, 250, -offsetCartePanier, null).play();
            }
        }
    }
    
    /**
     * Place le paquet de cartes au centre de la table.
     *
     * @param paquetDeCarte le paquet de cartes à placer au centre.
     */
    public void placerPaquetCarteAuCentre(PaquetDeCarte paquetDeCarte) {
        
        // Parcourir toutes les cartes dans le paquet de cartes
        for (Carte carte : paquetDeCarte.getCartes()) {
            
            // Ajouter chaque carte au panier de cartes au centre de la table
            cartesPanier.getChildren().add(carte);
            
            // Placer la carte en bas de la pile (c'est-à-dire à l'arrière de la vue)
            carte.toBack();
        }
    }
    
    /**
     * Distribue les cartes aux joueurs (doit être utlisé en début de partie).
     *
     * @param maitre l'instance de MaitreDuJeu qui contrôle la partie en cours.
     */
    public void distribuerCartesAuxJoueurs(MaitreDuJeu maitre) {
        
        Carte carteADonner = null;
        
        // Calculer la distance entre les cartes des joueurs IA et non-IA et le panier
        distanceCartesJoueurIaAuPanier = cartesJoueurIa.localToParent(0.0, 0.0).distance(cartesPanier.localToParent(0.0, 0));
        distanceCartesJoueurNonIaAuPanier = cartesJoueurNonIa.localToParent(0.0, 0.0).distance(cartesPanier.localToParent(0.0, 0));
        
        // Tant qu'il reste des cartes dans le paquet, distribuer les cartes aux joueurs
        while (!maitre.getPaquet().getCartes().isEmpty()) {
            for (Joueur joueurAQuiDonner : maitre.getJoueurs()) {
                
                try {
                    // Récupérer une carte du paquet
                    carteADonner = maitre.getPaquet().pop();
                    carteADonner.setForIa(joueurAQuiDonner.getPseudo().equals(MaitreDuJeu.nomIa));
                    
                    // Donner la carte au joueur
                    maitre.donnerUneCarteAUnJoueur(joueurAQuiDonner, carteADonner);
                    
                    // Générer une animation de translation pour la carte (distribution)
                    if (carteADonner.isForIa()) {
                        TranslateTransition tt = generateurAnimations.translation(carteADonner, 100, 0.0, -distanceCartesJoueurIaAuPanier);
                        generateurAnimations.addForSequentiel(tt);
                        tt.setOnFinished(e -> {
                            ProgressBar pBiA = fenetrePrincipaleControlleur.getProgressBarIa();
                            pBiA.setProgress(pBiA.getProgress() + fenetrePrincipaleControlleur.getStepProgressBar());
                        });
                        
                    } else {
                        TranslateTransition tt = generateurAnimations.translation(carteADonner, 100, 0.0, distanceCartesJoueurNonIaAuPanier);
                        generateurAnimations.addForSequentiel(tt);
                        tt.setOnFinished(e -> {
                            ProgressBar pBNonIa = fenetrePrincipaleControlleur.getProgressBarNonIa();
                            pBNonIa.setProgress(pBNonIa.getProgress() + fenetrePrincipaleControlleur.getStepProgressBar());
                        });
                    }
                    
                } catch (ErreurPaquetDeCarte err) {
                    System.out.println(err.getMessage());
                }
            }
            // Mettre à jour la distance pour créer une perspective pour les cartes empilées
            distanceCartesJoueurIaAuPanier -= forPerspectiveCartesJoueur;
            distanceCartesJoueurNonIaAuPanier -= forPerspectiveCartesJoueur;
        }
        
        System.out.println("Maitre du jeu : Je distribue les cartes...");
        
        // Jouer les animations de distribution de cartes
        generateurAnimations.getSequentialTransition().play();
        generateurAnimations.getSequentialTransition().setOnFinished((e) -> {
            System.out.println("Maitre de jeu : Distribution des cartes terminées.");
            
            double perspective = 0;
            
            // Gérer la mise en place des cartes une fois que toutes les cartes ont été distribuées
            while (!cartesPanier.getChildren().isEmpty()) {
                Carte c = (Carte) cartesPanier.getChildren().remove(0);
                if (c.isForIa()) {
                    c.setTranslateY(0);
                    // Ajouter la carte aux cartes du joueur IA
                    cartesJoueurIa.getChildren().add(c);
                    c.setLayoutY(c.getLayoutY() - perspective);
                } else {
                    c.setTranslateY(0);
                    
                    // Ajouter la carte aux cartes du joueur non-IA
                    cartesJoueurNonIa.getChildren().add(c);
                    c.setLayoutY(c.getLayoutY() + perspective);
                }
                
                // Mettre à jour la perspective
                perspective += forPerspectiveCartesJoueur;
            }
            
            // Vider les animations précédentes
            generateurAnimations.clearSt();
        });
    }
    
    public void joueurAJoueeUneCarte(Carte carteJouee) {
        TranslateTransition tt = null;
        
        if (carteJouee.isForIa()) {
            tt = generateurAnimations.translation(carteJouee, 100, +Carte.carteWidth / 2, +distanceCartesJoueurNonIaAuPanier);
            tt.setOnFinished(e -> {
                pbIa.setProgress(pbIa.getProgress() - fenetrePrincipaleControlleur.getStepProgressBar());
                placerUneCarteDansLePanier(carteJouee);
                cartesJoueurIa.getChildren().remove(carteJouee);
            });
        } else {
            tt = generateurAnimations.translation(carteJouee, 100, -Carte.carteWidth / 2, -distanceCartesJoueurNonIaAuPanier);
            tt.setOnFinished(e -> {
                pbNonIa.setProgress(pbNonIa.getProgress() - fenetrePrincipaleControlleur.getStepProgressBar());
                placerUneCarteDansLePanier(carteJouee);
                cartesJoueurNonIa.getChildren().remove(carteJouee);
            });
        }
        
        tt.play();
    }
    
    /**
     * @param cartes carte[0] contient la carte du joueurIa et [1] celle du joueurNonIa
     *
     * @return
     */
    public void fight(Carte[] cartes) {
        for (Carte carte : cartes) {
            if (carte != null)
                generateurAnimations.addForParalleleTransition(generateurAnimations.retournerCarte(carte, 500));
        }
        
        generateurAnimations.getPt().setOnFinished(e -> {
            System.out.println(cartesPanier.getChildren().size());
            generateurAnimations.cleatPt();
        });
        generateurAnimations.getPt().play();
    }
    
    public void vainqueurAnimation(Joueur gagant) {
        System.out.println(cartesPanier.getChildren().size());
    }
}

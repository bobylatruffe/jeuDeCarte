package fatih.bozlak.modele;

import java.util.ArrayList;
import java.util.Arrays;

public class MaitreDuJeu {
    public static String nomIa = "Tribulus";
    
    private final PaquetDeCarte paquet;
    
    /**
     * L'utilisation d'une liste est privilégiée ici à celle d'un tableau, pour des raisons de commodité. En effet, une
     * liste offre une facilité d'utilisation supérieure lors du parcours, de l'ajout et de la suppression d'éléments.
     * En outre, elle offre la possibilité de gérer un nombre de joueurs supérieur à deux, offrant ainsi une certaine
     * flexibilité pour une mise à jour.
     * <p>
     * Note importante : le premier joueur de la liste (à l'index 0), représente l'IA du jeu, initialisé dans le
     * constructeur.
     */
    private final ArrayList<Joueur> joueurs = new ArrayList<>();
    
    /**
     * Chaque fois qu'un joueur n'a plus de carte, alors on l'ajoute dans cette liste.
     */
    private final ArrayList<Joueur> perdants = new ArrayList<>();
    
    public final ArrayList<Carte> panier = new ArrayList<>();
    
    /**
     * Va contenir à l'indice 0 la carte jouée par "l'IA" et à l'indice 1 le joueur "humain".
     */
    private final Carte[] aComparer = new Carte[2];
    
    public ArrayList<Carte> getPanier() {
        return panier;
    }
    
    public Carte[] getAComparer() {
        return aComparer;
    }
    
    /**
     * Cette méthode distribue toutes les cartes du panier au joueur spécifié.
     *
     * @param joueur - le joueur à qui seront distribuées les cartes du panier
     */
    public void distribuerPanierAuGagnant(Joueur joueur) {
//        Collections.shuffle(panier);
        // Tant que le panier contient des cartes
        while (!panier.isEmpty()) {
            try {
                // Essayer d'attribuer la première carte du panier au joueur
                joueur.recevoirUneCarte(panier.remove(0));
            } catch (ErreurJoueur e) {
                // Si une erreur se produit lors de l'attribution de la carte, afficher le message d'erreur
                System.out.println(e.getMessage());
            }
        }
        // Toutes les cartes ont été distribuées, le panier est maintenant vide
    }
    
    /**
     * Cette méthode détermine quel joueur a gagné la manche (ou partie) en cours.
     *
     * @return Joueur - le joueur gagnant s'il existe un gagnant, null s'il faut une bataille.
     *
     * @throws ErreurMaitreDuJeu - Si aucun joueur n'a plus de carte pour jouer.
     */
    public Joueur quiGagne() throws ErreurMaitreDuJeu {
        // Initialisation d'un joueur gagnant potentiel à null
        Joueur gagnant = null;
        
        // Si tous les joueurs sont perdants, c'est qu'ils n'ont plus de carte, donc il n'y a pas de gagnant
        if (perdants.size() == 2) {
            System.out.println("Maitre du jeu : Aucun joueur n'a gagné !");
            throw new ErreurMaitreDuJeu("Aucun gagnant");
        } else if (perdants.size() == 1) { // Si nous avons un seul perdant, l'autre joueur est le gagnant
            System.out.printf("Maitre du jeu : Le joueur %s n'a plus de carte, donc perdu pour lui:(\n", perdants.get(0).getPseudo());
            
            // On parcourt la liste des joueurs pour trouver le gagnant (celui qui n'est pas le perdant)
            for (Joueur joueur : joueurs) {
                if (!joueur.getPseudo().equals(perdants.get(0).getPseudo())) gagnant = joueur;
            }
            
            /*
             Alternativement, nous pourrions faire une copie de la liste des joueurs, retirer le perdant et prendre le premier élément restant comme gagnant
             ArrayList<Joueur> joueursCopy = new ArrayList<>(joueurs);
             joueursCopy.remove(perdants.get(0));
             gagnant = joueursCopy.remove(0);
            */
            
        } else {
            // Si aucun des joueurs n'a perdu, donc ont encore des cartes, nous comparons la valeur de la carte qu'ils ont jouer pour déterminer le gagnant
            switch (aComparer[0].compareTo(aComparer[1])) {
                case 0: // Si la métrique est égale pour les deux joueurs, c'est qu'il faut une bataille
                    gagnant = null;
                    System.out.println("Maitre du jeu : Pour vous départager, une bataille s'impose !");
                    break;
                case 1: // Si la métrique du premier joueur est plus grande, le premier joueur est le gagnant
                    gagnant = joueurs.get(0);
                    break;
                case -1: // Si la métrique du second joueur est plus grande, le second joueur est le gagnant
                    gagnant = joueurs.get(1);
                    break;
            }
            
            if(gagnant != null)
                System.out.printf("Maitre du jeu : Le joueur %s à gagné !\n", gagnant.getPseudo());
        }
        
        aComparer[0] = null;
        aComparer[1] = null;
        perdants.clear();
        
        // Retourner le gagnant potentiel
        return gagnant;
    }
    
    /**
     * Demande à un joueur spécifique de jouer une carte. Cette carte est ensuite ajoutée au panier. Si carte est jouée
     * face découverte, la carte est également ajoutée au tableau de comparaison.
     *
     * @param joueur           - le joueur qui doit jouer une carte
     * @param isFaceDecouverte - un booléen qui indique si la carte est joué face découverte ou non
     */
    public Carte demanderAUnJoueurDeJouer(Joueur joueur, boolean isFaceDecouverte) {
        Carte carteJouee = null;
        try {
            // Le joueur joue une carte
            carteJouee = joueur.jouerUneCarte();
            System.out.printf("Maitre du jeu : %s a jouée une carte.\n", joueur.getPseudo());
            
            // La carte jouée est ajoutée au panier
            panier.add(carteJouee);
            
            // Si la carte est jouée face découverte
            if (isFaceDecouverte) {
                // Si le joueur est l'IA, la carte est ajoutée à la première position du tableau de comparaison
                if (joueur.getPseudo().equals(MaitreDuJeu.nomIa)) aComparer[0] = carteJouee;
                    // Sinon, la carte est ajoutée à la seconde position du tableau de comparaison
                else aComparer[1] = carteJouee;
            }
        } catch (ErreurJoueur e) {
            // Si une erreur se produit (par exemple, si le joueur n'a plus de cartes à jouer), un message d'erreur est affiché et le joueur est ajouté à la liste des perdants
            System.out.println(e.getMessage());
            perdants.add(joueur);
        }
        
        return carteJouee;
    }
    
    /**
     * Cette méthode donne une carte spécifique à un joueur. Si le joueur ne peut pas recevoir de carte (s'il la possède
     * déjà), une exception ErreurJoueur est générée et le message d'erreur correspondant est affiché.
     *
     * @param joueur - le joueur qui doit recevoir la carte
     * @param carte  - la carte à donner au joueur
     */
    public void donnerUneCarteAUnJoueur(Joueur joueur, Carte carte) {
        try {
            // Essayer de donner la carte au joueur
            joueur.recevoirUneCarte(carte);
        } catch (ErreurJoueur e) {
            // Si une erreur se produit lors de l'attribution de la carte, afficher le message d'erreur
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Ajoute un nouveau joueur à la liste des joueurs. Affiche ensuite un message pour annoncer l'ajout du joueur dans
     * la partie.
     *
     * @param joueur - le nouveau joueur à ajouter à la partie (la liste des joueurs).
     */
    public void addJoueur(Joueur joueur) {
        // Ajout du joueur à la liste des joueurs
        joueurs.add(joueur);
        
        // Affichage d'un message indiquant que le joueur a été ajouté
        System.out.printf("Maitre du jeu : Création du joueur %s.\n", joueur.getPseudo());
    }
    
    /**
     * Cette méthode renvoie le paquet de cartes actuellement en possession du maître du jeu.
     *
     * @return PaquetDeCarte - le paquet de cartes actuel
     */
    public PaquetDeCarte getPaquet() {
        // Renvoi du paquet de cartes actuel
        return paquet;
    }
    
    /**
     * Cette méthode renvoie la liste des joueurs actuellement dans le jeu.
     *
     * @return ArrayList<Joueur> - la liste des joueurs actuels
     */
    public ArrayList<Joueur> getJoueurs() {
        // Renvoi de la liste des joueurs actuels
        return joueurs;
    }
    
    /**
     * Ce constructeur crée une instance de MaitreDuJeu avec un paquet de cartes de taille spécifique. Il initialise
     * également le premier joueur de la liste en tant qu'IA.
     *
     * @param nbDeCarteParCouleur - le nombre de cartes par couleur dans le paquet à créer
     */
    public MaitreDuJeu(int nbDeCarteParCouleur) {
        // Création du paquet de cartes avec le nombre spécifié de cartes par couleur
        paquet = new PaquetDeCarte(nbDeCarteParCouleur);
        
        // Affichage d'un message indiquant la création du paquet
        System.out.printf("Maitre du jeu : Création d'un paquet de %d cartes.\n", paquet.getCartes().size());
        
        // Création de l'IA et ajout à la liste des joueurs
        joueurs.add(new Joueur(MaitreDuJeu.nomIa));
        
        // Affichage d'un message indiquant la création de l'IA
        System.out.printf("Maitre du jeu : Création du joueur %s.\n", joueurs.get(0).getPseudo());
    }
    
    @Override
    public String toString() {
        return "MaitreDuJeu{" + "paquet=" + paquet + ", joueurs=" + joueurs + ", perdants=" + perdants + ", panier=" + panier + ", aComparer=" + Arrays.toString(aComparer) + '}';
    }
}

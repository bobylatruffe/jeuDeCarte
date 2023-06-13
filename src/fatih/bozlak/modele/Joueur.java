package fatih.bozlak.modele;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Classe représentant un joueur dans un jeu de cartes. Chaque joueur a un pseudo, un identifiant unique et un ensemble
 * de cartes.
 */
public class Joueur {
    /**
     * Identifiant qui sera attribué au prochain joueur créé.
     */
    private static int nextId = 1;
    
    /**
     * Liste des cartes que le joueur a en main.
     */
    private final ArrayList<Carte> cartes = new ArrayList<>();
    
    /**
     * Pseudo du joueur.
     */
    private final String pseudo;
    
    /**
     * Identifiant unique du joueur.
     */
    private final int id;
    
    /**
     * Crée un nouveau joueur avec le pseudo spécifié.
     *
     * @param pseudo le pseudo du nouveau joueur
     */
    public Joueur(String pseudo) {
        this.pseudo = pseudo;
        this.id = nextId++;
    }
    
    /**
     * Le joueur joue une carte de sa main. Si le joueur n'a pas de cartes, une ErreurJoueur est lancée.
     *
     * @return la carte jouée
     *
     * @throws ErreurJoueur si le joueur n'a pas de cartes
     */
    public Carte jouerUneCarte() throws ErreurJoueur {
        // Il faudrait prévoir du code (un Scanner ou un listener) quand je vais faire l'interface graphique.
        // Pour le moment je laisse comme ça.
        
        if (cartes.isEmpty()) throw new ErreurJoueur(this.getPseudo() + " : Je n'ai plus de carte.");
        
        if (!getPseudo().equals("SUPER_IA")) {
            boolean aJouee = false;
            Scanner scanner = new Scanner(System.in);
            while (!aJouee) {
                System.out.print(this.getPseudo() + " (j pour jouer la 1er carte) : ");
                String joueurProposition = scanner.nextLine();
                switch (joueurProposition) {
                    case "j":
                        aJouee = true;
                        break;
                    
                }
            }
        }
        
        return cartes.remove(0);
    }
    
    /**
     * Le joueur reçoit une carte. Si le joueur a déjà cette carte, une ErreurJoueur est lancée.
     *
     * @param carte la carte à recevoir
     *
     * @throws ErreurJoueur si le joueur a déjà cette carte
     */
    public void recevoirUneCarte(Carte carte) throws ErreurJoueur {
        if (cartes.contains(carte))
            throw new ErreurJoueur("Ne peut pas recevoir la carte + " + carte + ", le joueur le possède déjà.");
        
        cartes.add(carte);
    }
    
    /**
     * Retourne la liste des cartes que le joueur a en main.
     *
     * @return la liste des cartes que le joueur a en main
     */
    public ArrayList<Carte> getCartes() {
        return cartes;
    }
    
    /**
     * Retourne le pseudo du joueur.
     *
     * @return le pseudo du joueur
     */
    public String getPseudo() {
        return pseudo;
    }
    
    /**
     * Retourne une représentation textuelle du joueur.
     *
     * @return une chaîne de caractères représentant le joueur
     */
    @Override
    public String toString() {
        return "Joueur{" + "cartes=" + cartes + ", pseudo='" + pseudo + '\'' + ", id=" + id + '}';
    }
}

/**
 * Exception lancée lorsqu'une opération invalide est effectuée sur un joueur. Par exemple, essayer de jouer une carte
 * alors qu'il n'en a pas, ou essayer de recevoir une carte qu'il possède déjà.
 */
class ErreurJoueur extends Exception {
    /**
     * Construit une nouvelle ErreurJoueur avec le message d'erreur spécifié.
     *
     * @param msg le message d'erreur
     */
    public ErreurJoueur(String msg) {
        super(msg);
    }
}

package fatih.bozlak.modele;

import java.util.ArrayList;

public class MaitreDuJeu {
    private final PaquetDeCarte paquet;
    
    private final ArrayList<Joueur> joueurs = new ArrayList<>();
    
    private final ArrayList<Carte> panier = new ArrayList<>();
    /**
     * Indice i=0 correspond aux cartes jouées par le joueur nonIA, * et i=1 correspond aux cartes jouées par le joueur
     * IA.
     * <p>
     * Dans la version sans interface graphique, j'avais une meilleure approche, mais ne voulant plus compliquer les
     * choses, je verrai si j'implémente la possibilité de jouer à plusieurs, et reprendre mon approche non interface
     * graphique.
     */
    private final Carte[] aComparer = new Carte[2];
    
    private Joueur lastVainqeur;
    
    public MaitreDuJeu(int nbDeCarteParCouleur) {
        paquet = new PaquetDeCarte(nbDeCarteParCouleur);
        System.out.printf("Maitre du jeu : Création d'un paquet de %d cartes.\n", paquet.getCartes().size());
        
        joueurs.add(new Joueur("Tribuluss"));
        System.out.printf("Maitre du jeu : Création du joueur %s (ton adversaire).\n", joueurs.get(0).getPseudo());
    }
    
    public void donnerUneCarteAUnJoueur(Joueur joueur, Carte carte) throws ErreurJoueur {
        joueur.recevoirUneCarte(carte);
    }
    
    public void addJoueur(Joueur joueur) {
        joueurs.add(joueur);
        System.out.printf("Maitre du jeu : Création du joueur %s.\n", joueur.getPseudo());
    }
    
    public PaquetDeCarte getPaquet() {
        return paquet;
    }
    
    public ArrayList<Joueur> getJoueurs() {
        return joueurs;
    }
    
    public void ajouterCarteAuPanier(Joueur joueur, Carte carte, boolean isFaceDecouverte) {
        int quelJoueur = 0; // par defaut le joueur nonIa
        if (joueur.getPseudo().equals("Tribuluss")) {
            quelJoueur = 1;
        }
        
        if (isFaceDecouverte) {
            aComparer[quelJoueur] = carte;
        }
        
        panier.add(carte);
    }
    
    public Joueur determinerVainqueur() {
        switch (aComparer[0].compareTo(aComparer[1])) {
            case 0:
                this.lastVainqeur = null;
                break;
            case 1:
                this.lastVainqeur = this.getJoueurs().get(1);
                break;
            case -1:
                this.lastVainqeur = this.getJoueurs().get(0);
                break;
        }
        return this.lastVainqeur;
    }
    
    public ArrayList<Carte> getPanier() {
        return this.panier;
    }
}

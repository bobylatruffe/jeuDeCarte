package fatih.bozlak.modele;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MaitreDuJeu {
    private final PaquetDeCarte paquet;
    
    private final ArrayList<Joueur> joueurs = new ArrayList<>();
    
    public MaitreDuJeu(int nbDeCarteParCouleur) {
        paquet = new PaquetDeCarte(nbDeCarteParCouleur);
        System.out.printf("Maitre du jeu : Création d'un paquet de %d cartes.\n", paquet.getCartes().size());
        
        joueurs.add(new Joueur("Brutuse"));
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
}

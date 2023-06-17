package fatih.bozlak.modele;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Classe représentant un paquet de cartes dans un jeu de cartes. Le paquet de cartes contient un certain nombre de
 * cartes de chaque couleur.
 */
public class PaquetDeCarte {
    /**
     * Liste de toutes les cartes dans le paquet.
     */
    private final ArrayList<Carte> cartes;
    
    /**
     * Construit un nouveau paquet de cartes. Chaque couleur contiendra le nombre spécifié de cartes.
     *
     * @param nbDeCarteParCouleur le nombre de cartes de chaque couleur à ajouter au paquet
     */
    public PaquetDeCarte(int nbDeCarteParCouleur) {
        cartes = new ArrayList<>();
        
        for (Couleur couleur : Couleur.values())
            for (int i = 1; i <= nbDeCarteParCouleur; i++)
                cartes.add(new Carte(i, couleur));
    }
    
    /**
     * Retire la première carte du paquet. Si le paquet est vide, une ErreurPaquetDeCarte est lancée.
     *
     * @return la première carte du paquet
     *
     * @throws ErreurPaquetDeCarte si le paquet est vide
     */
    public Carte pop() throws ErreurPaquetDeCarte {
        if (cartes.isEmpty()) throw new ErreurPaquetDeCarte("Le paquet est vide.");
        
        return cartes.remove(0);
    }
    
    /**
     * Mélange les cartes dans le paquet.
     */
    public void melanger() {
        Collections.shuffle(cartes);
    }
    
    /**
     * Retourne la liste des cartes dans le paquet.
     *
     * @return la liste des cartes dans le paquet
     */
    public ArrayList<Carte> getCartes() {
        return cartes;
    }
    
    /**
     * Retourne une représentation textuelle du paquet de cartes.
     *
     * @return une chaîne de caractères représentant le paquet de cartes
     */
    @Override
    public String toString() {
        return "PaquetDeCarte{" + "cartes=" + cartes + '}';
    }
}


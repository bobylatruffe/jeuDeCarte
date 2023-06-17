package fatih.bozlak.modele;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Classe représentant une carte dans un jeu de cartes. Chaque carte a une valeur et une couleur.
 */
public class Carte extends ImageView {
    public boolean isForIa() {
        return isForIa;
    }
    
    public void setForIa(boolean forIa) {
        isForIa = forIa;
    }
    
    private boolean isForIa = false;
    
    /**
     * La valeur de la carte. Dans la plupart des jeux de cartes, la valeur peut varier de 1 (as) à 13 (roi).
     */
    private final int valeur;
    
    /**
     * La couleur de la carte. Par exemple, dans un jeu de cartes standard, il peut s'agir de coeurs, de carreaux, de
     * trèfles ou de piques.
     */
    private final Couleur couleur;
    
    /**
     * Indique si la face de la carte est découverte.
     */
    private boolean isFaceDecouverte = false;
    
    /**
     * Image représentant la face cachée de la carte.
     */
    private final Image imgFaceDecouverte;
    
    /**
     * Image représentant la face cachée de la carte.
     */
    private final Image imgFaceCachee = new Image(getClass().getResource("/resources/images/backCarte.png").toExternalForm());
    
    /**
     * Constructeur pour la classe Carte. Initialise une nouvelle carte avec une valeur et une couleur spécifiées, et
     * charge les images de la face cachée et découverte de la carte.
     *
     * @param valeur  la valeur de la nouvelle carte
     * @param couleur la couleur de la nouvelle carte
     */
    public Carte(int valeur, Couleur couleur) {
        this.valeur = valeur;
        this.couleur = couleur;
        
        imgFaceDecouverte = new Image(getClass().getResource("/resources/images/cartes/" + valeur + " " + couleur + ".png").toExternalForm());
        
        this.setImage(imgFaceCachee);
        this.setFitWidth(190);
        this.setFitHeight(290);
//        this.setPreserveRatio(true);
    }
    
    /**
     * Défini si c'est la face cachée ou découverte qui sera affichée.
     *
     * @param faceDecouverte true si c'est la face découverte qui sera affichée ou false si c'est la face cachée.
     */
    public void setFaceDecouverte(boolean faceDecouverte) {
        isFaceDecouverte = faceDecouverte;
        
        if (isFaceDecouverte) this.setImage(this.imgFaceDecouverte);
        else this.setImage(this.imgFaceCachee);
    }
    
    /**
     * Indique si la face de la carte est découverte.
     *
     * @return vrai si la face de la carte est découverte, sinon faux
     */
    public boolean isFaceDecouverte() {
        return isFaceDecouverte;
    }
    
    /**
     * Représente la carte sous forme de chaîne de caractères.
     *
     * @return une représentation textuelle de la carte
     */
    @Override
    public String toString() {
        return "Carte{" + "valeur=" + valeur + ", couleur=" + couleur + '}';
    }
    
    /**
     * Compare cette carte à une autre pour déterminer laquelle est plus élevée. L'as est considéré comme la carte la
     * plus élevée.
     *
     * @param autreCarte la carte à comparer
     *
     * @return -1 si cette carte est inférieure, 0 si elles sont égales, 1 si cette carte est supérieure
     */
    public int compareTo(Carte autreCarte) {
        if (this.getValeur() == autreCarte.getValeur()) return 0;
        
        // Puisque l'as (1) est plus forte que tout les autres cartes.
        if (this.getValeur() == 1) return 1;
        if (autreCarte.getValeur() == 1) return -1;
        
        if (this.getValeur() > autreCarte.getValeur()) return 1;
        
        return -1;
    }
    
    /**
     * Vérifie si cette carte est égale à un autre objet. Deux cartes sont considérées comme égales si elles ont la même
     * valeur et la même couleur.
     *
     * @param autre l'objet à comparer à cette carte
     *
     * @return true si l'objet est une carte avec la même valeur et la même couleur, false sinon
     */
    @Override
    public boolean equals(Object autre) {
        if (autre == this) return true;
        
        if (!(autre instanceof Carte)) return false;
        
        Carte autreCarte = (Carte) autre;
        
        return valeur == autreCarte.getValeur() && couleur == autreCarte.getCouleur();
    }
    
    /**
     * Retourne la valeur de la carte.
     *
     * @return la valeur de la carte
     */
    public int getValeur() {
        return valeur;
    }
    
    /**
     * Retourne la couleur de la carte.
     *
     * @return la couleur de la carte
     */
    public Couleur getCouleur() {
        return couleur;
    }
}

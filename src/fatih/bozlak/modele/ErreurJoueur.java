package fatih.bozlak.modele;

/**
 * Exception lancée lorsqu'une opération invalide est effectuée sur un joueur. Par exemple, essayer de jouer une carte
 * alors qu'il n'en a pas, ou essayer de recevoir une carte qu'il possède déjà.
 */
public class ErreurJoueur extends Exception {
    /**
     * Construit une nouvelle ErreurJoueur avec le message d'erreur spécifié.
     *
     * @param msg le message d'erreur
     */
    public ErreurJoueur(String msg) {
        super(msg);
    }
}
package fatih.bozlak.modele;

/**
 * Exception lancée lorsqu'une opération invalide est effectuée sur un paquet de cartes. Par exemple, essayer de retirer
 * une carte d'un paquet vide.
 */
public class ErreurPaquetDeCarte extends Exception {
    /**
     * Construit une nouvelle ErreurPaquetDeCarte avec le message d'erreur spécifié.
     *
     * @param msg le message d'erreur
     */
    public ErreurPaquetDeCarte(String msg) {
        super(msg);
    }
}

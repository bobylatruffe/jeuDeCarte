package fatih.bozlak.modele;

public class ErreurMaitreDuJeu extends Exception {
    private Joueur gagnant;
    public ErreurMaitreDuJeu(String msg, Joueur gagnant) {
        super(msg);
        this.gagnant = gagnant;
    }
    
    public Joueur getGagnant() {
        return gagnant;
    }
}

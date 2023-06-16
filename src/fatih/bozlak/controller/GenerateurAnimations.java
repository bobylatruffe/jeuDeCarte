package fatih.bozlak.controller;

import fatih.bozlak.modele.Carte;
import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class GenerateurAnimations {
    private final SequentialTransition st = new SequentialTransition();
    private final ParallelTransition pt = new ParallelTransition();
    
    public TranslateTransition translation(Node carteAAnimer, double msecondes, Double byX, Double byY) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(msecondes), carteAAnimer);
        
        if (byX != null) {
            tt.setByX(byX);
        }
        
        if (byY != null) {
            tt.setByY(byY);
        }
        
        return tt;
    }
    
    public void addForSequentiel(Transition t) {
        this.st.getChildren().add(t);
    }
    
    public void clearSt() {
        this.st.getChildren().clear();
    }
    
    public SequentialTransition getSequentialTransition() {
        return this.st;
    }
    
    public ParallelTransition getPt() {
        return pt;
    }
    
    public void cleatPt() {
        this.pt.getChildren().clear();
    }
    
    public void addForParalleleTransition(Transition t) {
        this.pt.getChildren().add(t);
    }
    
    public RotateTransition retournerCarte(Carte carte, double delay) {
        RotateTransition rt = new RotateTransition(Duration.millis(100), carte);
        rt.setAxis(Rotate.Y_AXIS);
        rt.setByAngle(90);
        rt.setDelay(Duration.millis(delay));
        rt.setOnFinished(e -> {
            carte.setFaceDecouverte(true);
            RotateTransition rt2 = new RotateTransition(Duration.millis(100), carte);
            rt2.setAxis(Rotate.Y_AXIS);
            rt2.setByAngle(-90);
            
            rt2.play();
        });
        
        return rt;
    }
}

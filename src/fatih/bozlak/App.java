package fatih.bozlak;

import fatih.bozlak.controller.AccueilControlleur;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fatih/bozlak/view/Accueil.fxml"));
        loader.setController(new AccueilControlleur());
        
        Parent root = loader.load();
        
        stage.setTitle("Super Bataille");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }
}

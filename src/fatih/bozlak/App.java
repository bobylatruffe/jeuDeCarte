package fatih.bozlak;

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
        Parent root = FXMLLoader.load(getClass().getResource("/fatih/bozlak/view/FenetrePrincipale.fxml"));
        
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
    }
}

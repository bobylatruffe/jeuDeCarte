module fatih.bozlak {
    requires javafx.fxml;
    requires javafx.controls;
    
    opens fatih.bozlak to javafx.fxml;
    
    exports fatih.bozlak;
}
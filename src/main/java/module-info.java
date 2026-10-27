module com.dodibo.tp2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;


    opens com.ensicaen.tp2 to javafx.fxml;
    exports com.ensicaen.tp2;
}
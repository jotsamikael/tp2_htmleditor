package com.ensicaen.tp2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class HtmlEditorApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HtmlEditorApplication.class.getResource("home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("EDITEUR HTML");
        stage.setScene(scene);
        stage.getIcons().add(new Image(
                HtmlEditorApplication.class.getResourceAsStream("/images/edit.png")
        ));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
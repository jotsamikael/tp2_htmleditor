package com.ensicaen.tp2;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class AboutController {

    @FXML
    Text aboutText;

   @FXML
   public void initialize() {
        aboutText.setText("TP2: ENSICAEN - Editeur HTML\n" +
                "Version 1.0\n" +
                "Développé par JOTSA NGUETSOP Mikael\n" +
                "2026");
    }
}

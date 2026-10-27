package com.ensicaen.tp2;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class EditionZoneController {
    @FXML
    private TextArea htmlEditor;

    @FXML
    private WebView htmlPreview;

    private WebEngine webEngine;

    private File currentFile;

    private final ReadOnlyIntegerWrapper characterCount =
            new ReadOnlyIntegerWrapper(0);

    private final ReadOnlyIntegerWrapper lineCount =
            new ReadOnlyIntegerWrapper(1);

    // This method is called by the FXMLLoader when initialization is complete
    @FXML
    public void initialize() {

        webEngine = htmlPreview.getEngine();

        // Refresh the preview whenever HTML changes
        htmlEditor.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    webEngine.loadContent(newValue);

                    characterCount.set(newValue.length());

                    lineCount.set(
                            newValue.split("\\R", -1).length
                    );
                }
        );
    }

    public void setContent(String html) {
        htmlEditor.setText(html);
    }

    public String getContent() {
        return htmlEditor.getText();
    }

    public void openFile(File file) throws IOException {

        String content = Files.readString(
                file.toPath(),
                StandardCharsets.UTF_8
        );

        currentFile = file;
        setContent(content);
    }

    public void saveFile(File file) throws IOException {

        Files.writeString(
                file.toPath(),
                getContent(),
                StandardCharsets.UTF_8
        );

        currentFile = file;
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public ReadOnlyIntegerProperty characterCountProperty() {
        return characterCount.getReadOnlyProperty();
    }

    public ReadOnlyIntegerProperty lineCountProperty() {
        return lineCount.getReadOnlyProperty();
    }

    public void copy() {
        htmlEditor.copy();
    }

    public void cut() {
        htmlEditor.cut();
    }

    public void paste() {
        htmlEditor.paste();
    }
}

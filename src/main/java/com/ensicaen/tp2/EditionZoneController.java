package com.ensicaen.tp2;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
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

    private boolean loading;


    private final ReadOnlyBooleanWrapper modified =
            new ReadOnlyBooleanWrapper(false);

    private final ReadOnlyIntegerWrapper characterCount =
            new ReadOnlyIntegerWrapper(0);

    private final ReadOnlyIntegerWrapper lineCount =
            new ReadOnlyIntegerWrapper(1);

    // This method is called by the FXMLLoader when initialization is complete
    @FXML
    public void initialize() {
        webEngine = htmlPreview.getEngine();

        // Refresh preview when HTML changes
        htmlEditor.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    webEngine.loadContent(newValue);

                    characterCount.set(newValue.length());

                    lineCount.set(
                            newValue.split("\\R", -1).length
                    );

                    // Set modified to true if the new value is different from the old value
                    if (!loading) {
                        modified.set(true);
                    }
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
        loading = true;
        try {
            setContent(content);
        } finally {
            loading = false;
        }
        modified.set(false);
    }

    public void saveFile(File file) throws IOException {

        Files.writeString(
                file.toPath(),
                getContent(),
                StandardCharsets.UTF_8
        );

        currentFile = file;
        modified.set(false);
    }

    public boolean isModified() {
        return modified.get();
    }

    public ReadOnlyBooleanProperty modifiedProperty() {
        return modified.getReadOnlyProperty();
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

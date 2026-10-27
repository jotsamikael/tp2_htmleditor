package com.ensicaen.tp2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HomeController {

    @FXML
    private TabPane tabPane;

    @FXML
    private Label character_label;

    @FXML
    private Label line_label;

    @FXML
    private ImageView copyImg;

    @FXML
    private ImageView saveImg;

    @FXML
    private ImageView saveAsImg;

    private final Map<Tab, EditionZoneController> editors =
            new HashMap<>();

    private int untitledCount = 1;




    @FXML
    public void initialize() {
        //Tooltips

        Tooltip.install(copyImg, new Tooltip("Copy"));
        Tooltip.install(saveImg, new Tooltip("Save"));
        Tooltip.install(saveAsImg, new Tooltip("Save As"));

        // Listen for tab selection changes to update statistics
        tabPane.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldTab, newTab) -> {
                    updateStatistics();
                });


    }



    // =========================================
    // Create a new editor tab
    // =========================================

    private EditionZoneController createEditorTab(String title) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("edition-view.fxml")
            );

            Parent editorView = loader.load();

            EditionZoneController controller =
                    loader.getController();

            Tab tab = new Tab(title);
            tab.setContent(editorView);

            editors.put(tab, controller);

            tab.setOnClosed(event -> {
                editors.remove(tab);
                updateStatistics();
            });

            controller.characterCountProperty().addListener(
                    (obs, oldValue, newValue) -> {
                        if (tabPane.getSelectionModel()
                                .getSelectedItem() == tab) {
                            updateStatistics();
                        }
                    }
            );

            controller.lineCountProperty().addListener(
                    (obs, oldValue, newValue) -> {
                        if (tabPane.getSelectionModel()
                                .getSelectedItem() == tab) {
                            updateStatistics();
                        }
                    }
            );

            // Add the new tab to the TabPane
            tabPane.getTabs().add(tab);

            // Select the new tab
            tabPane.getSelectionModel().select(tab);

            updateStatistics();

            return controller;

        } catch (IOException e) {

            showError("Impossible de créer l'éditeur", e);
            return null;
        }
    }


    // =========================================
    // Get active editor
    // =========================================

    private EditionZoneController getActiveEditor() {

        Tab selectedTab = tabPane.getSelectionModel()
                .getSelectedItem();

        if (selectedTab == null) {
            return null;
        }

        return editors.get(selectedTab);
    }



    @FXML
    protected void onNewMenuItemClick() {
        EditionZoneController editor = createEditorTab(
                "Sans nom " + untitledCount
        );

        if (editor != null) {

            untitledCount++;

            editor.setContent(
                    "<!DOCTYPE html>\n" +
                            "<html>\n" +
                            "<head>\n" +
                            "    <title>New Document</title>\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "\n" +
                            "</body>\n" +
                            "</html>"
            );
        }
    }

    @FXML
    protected void onOpenMenuItemClick() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir un fichier HTML");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(
                        "HTML Files", "*.html", "*.htm"
                ),
                new FileChooser.ExtensionFilter(
                        "All Files", "*.*"
                )
        );

        File selectedFile = fileChooser.showOpenDialog(
                tabPane.getScene().getWindow()
        );

        if (selectedFile != null) {

            EditionZoneController editor = createEditorTab(
                    selectedFile.getName()
            );

            if (editor != null) {
                try {
                    editor.openFile(selectedFile);
                } catch (IOException e) {
                    showError("Impossible d'ouvrir le fichier", e);
                }
            }
        }
    }

    @FXML
    protected void onSaveMenuItemClick() throws IOException {
        System.out.println(" save clicked");
        // get the active editor
        EditionZoneController editor = getActiveEditor();
        // check if current file is null
        if (editor != null) {
            // get the current file
            File currentFile = editor.getCurrentFile();
            if (currentFile != null) {
                // save the file
                editor.saveFile(currentFile);
            } else {
                // show save as dialog
                onSaveAsMenuItemClick();
            }
        }


    }

    @FXML
    protected void onSaveAsMenuItemClick() {
        System.out.println("On save as clicked");
        EditionZoneController editor = getActiveEditor();
        if (editor != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer sous");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("HTML Files", "*.html"),
                    new FileChooser.ExtensionFilter("All Files", "*.*")
            );
            // Show the save file dialog
            File file = fileChooser.showSaveDialog(tabPane.getScene().getWindow());
            if (file != null) {
                try {
                    editor.saveFile(file);
                    // Update the tab title to the new file name
                    Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
                    if (selectedTab != null) {
                        selectedTab.setText(file.getName());
                    }
                } catch (IOException e) {
                    showError("Impossible de sauvegarder le fichier", e);
                }
            }
        }
    }

    @FXML
    protected void oncloseMenuItemClick() {

        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab != null) {
            selectedTab.getTabPane().getTabs().remove(selectedTab);
        }
    }

    @FXML
    protected void onExitMenuItemClick() {
        // Close the application
        Stage stage = (Stage) tabPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onCopyMenuItemClick() {
        System.out.println("on copy item clicked");
    }

    @FXML
    protected void onPasteMenuItemClick() {
        System.out.println("on paste item clicked");
    }

    @FXML
    protected void onCutMenuItemClick() {
        System.out.println("cut item clicked");
    }

    @FXML
    protected void onAboutMenuItemClick() {
        System.out.println("about item clicked");
        //load the about view
        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("about-view.fxml")
            );

            Parent root = loader.load();

            Stage aboutStage = new Stage();

            aboutStage.setTitle("À propos de HTMLEdit");

            aboutStage.setScene(new Scene(root));

            // Make this a modal popup
            //aboutStage.initModality(Modality.WINDOW_MODAL);
            var resource = HtmlEditorApplication.class.getResource("edit.png");
            if (resource != null) {
            aboutStage.getIcons().add(new Image(resource.toExternalForm()));
            }
            aboutStage.show();
            // Set the main window as owner
            aboutStage.initOwner(
                    tabPane.getScene().getWindow()
            );

            aboutStage.setResizable(false);

            aboutStage.showAndWait();

        } catch (IOException e) {
            showError("Impossible de charger la vue 'À propos'", e);
        }
    }


    //HELPER

    // =========================================
    // Statistics
    // =========================================

    private void updateStatistics() {

        EditionZoneController editor = getActiveEditor();

        if (editor == null) {
            character_label.setText("0");
            line_label.setText("0");
            return;
        }

        character_label.setText(
                String.valueOf(editor.characterCountProperty().get())
        );

        line_label.setText(
                String.valueOf(editor.lineCountProperty().get())
        );
    }

    private void showError(String message, Exception e) {
        System.err.println(message);
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(message);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }
}
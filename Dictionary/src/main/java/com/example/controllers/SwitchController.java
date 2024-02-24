package com.example.controllers;

import base.advanced.Dictionary;
import com.example.settings.cssSetting;
import com.example.settings.dataSetting;
import com.example.settings.readwriteLocal;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class SwitchController extends MainController{
    private static Stage stage;
    private static Scene scene;
    private static FXMLLoader root;
    private static Parent parent;

    public static Stage getStage() {
        return stage;
    }

    public static Scene getScene() {
        return scene;
    }

    public static FXMLLoader getRoot() {
        return root;
    }

    public static String DARK_CSS = Objects.requireNonNull(SwitchController.class.getResource("/com/css/application-dark.css")).toExternalForm();
    public static String LIGHT_CSS = Objects.requireNonNull(SwitchController.class.getResource("/com/css/application-light.css")).toExternalForm();

    public static void initializeApplication(Stage _stage, FXMLLoader _root) throws IOException {
        readwriteLocal.initialize();
        Dictionary.initialize();
        stage = _stage;
        root = _root;
        parent = root.load();
        scene = new Scene(parent, 1280, 720, false, SceneAntialiasing.BALANCED);
        scene.getStylesheets().add(cssSetting.getConfig() ? DARK_CSS : LIGHT_CSS);
        renderScene();
    }

    public static void switchToWord() throws IOException {
        root = new FXMLLoader(SwitchController.class.getResource("/com/example/word.fxml"));
        parent = root.load();
        renderScene();
    }

    public static void switchToParagraph() throws IOException {
        root = new FXMLLoader(SwitchController.class.getResource("/com/example/paragraph.fxml"));
        parent = root.load();
        renderScene();
    }

    public static void switchToPop() throws IOException {
        root = new FXMLLoader(SwitchController.class.getResource("/com/example/pop.fxml"));
        parent = root.load();
        renderScene();
    }

    public static void switchToSetting() throws IOException {
        root = new FXMLLoader(SwitchController.class.getResource("/com/example/setting.fxml"));
        parent = root.load();
        renderScene();
    }

    public static void switchToSynonym() throws IOException {
        root = new FXMLLoader(SwitchController.class.getResource("/com/example/synonyms.fxml"));
        parent = root.load();
        renderScene();
    }

//    public static void switchToGame() throws IOException {
//        root = new FXMLLoader(SwitchController.class.getResource("/com/example/game.fxml"));
//        parent = root.load();
//        renderScene();
//    }

    public static void renderScene() {
        scene.setRoot(parent);
        stage.setTitle("ThanhNhuTrong");
        stage.setScene(scene);
        stage.show();
    }
}
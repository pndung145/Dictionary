package com.example.mainApp;

import base.advanced.Dictionary;
import com.example.controllers.SwitchController;
import com.example.settings.dataSetting;
import com.example.settings.readwriteLocal;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader root = new FXMLLoader(getClass().getResource("/com/example/word.fxml"));
        SwitchController.initializeApplication(stage, root);
    }

    @Override
    public void stop() throws IOException {
        readwriteLocal.save();
        Dictionary.save();
    }

    public static void main(String[] args) {
        launch();
    }
}
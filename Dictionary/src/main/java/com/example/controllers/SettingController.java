package com.example.controllers;

import base.advanced.Dictionary;
import com.example.settings.cssSetting;
import com.example.settings.dataSetting;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;

public class SettingController extends MainController {
    private Alert alert;
    @FXML
    private ChoiceBox<String> switchThemeChoiceBox, dataChoiceBox;

    @FXML
    public void initialize() {
        switchThemeChoiceBox.setItems(FXCollections.observableArrayList("Light", "Dark"));
        switchThemeChoiceBox.setValue(cssSetting.getConfig() ? "Dark" : "Light");
        dataChoiceBox.setItems(FXCollections.observableArrayList("Local", "SQLLite"));
        dataChoiceBox.setValue(dataSetting.getConfig() ? "SQLLite" : "Local");
        alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Import data");
        alert.setContentText("Đang import dữ liệu...");
        alert.setResizable(false);
        alert.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Button cancel = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancel.setVisible(false);
    }

    @FXML
    public void onSwitchThemeChoiceBox() {
        if (switchThemeChoiceBox.getValue().equals("Dark")) {
            SwitchController.getScene().getStylesheets().remove(SwitchController.DARK_CSS);
            if (!SwitchController.getScene().getStylesheets().contains(SwitchController.DARK_CSS))
                SwitchController.getScene().getStylesheets().add(SwitchController.DARK_CSS);
            cssSetting.setConfig(true);
        } else {
            SwitchController.getScene().getStylesheets().remove(SwitchController.DARK_CSS);
            if (!SwitchController.getScene().getStylesheets().contains(SwitchController.LIGHT_CSS))
                SwitchController.getScene().getStylesheets().add(SwitchController.LIGHT_CSS);
            cssSetting.setConfig(false);
        }
    }

    @FXML
    public void onSwitchDataChoiceBox() {
        if(dataChoiceBox.getValue().equals("Local")){
            if(dataSetting.getConfig()){
                Thread thread = new Thread(() -> {
                    Dictionary.initialize();
                    Platform.runLater(() -> alert.close());
                });
                alert.show();
                thread.setDaemon(true);
                thread.start();
                dataSetting.setConfig(false);
            }
        }
        else {
            if(!dataSetting.getConfig()){
                Thread thread = new Thread(() -> {
                    Dictionary.save();
                    Platform.runLater(() -> alert.close());
                });
                alert.show();
                thread.setDaemon(true);
                thread.start();
                dataSetting.setConfig(true);
            }
        }
    }
}

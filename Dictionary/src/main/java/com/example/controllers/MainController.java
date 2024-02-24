package com.example.controllers;

import javafx.fxml.FXML;

import java.io.IOException;

public class MainController {
    @FXML
    public void onOfflineButtonClick() throws IOException {
        SwitchController.switchToWord();
    }

    @FXML
    public void onOnlineButtonClick() throws IOException {
        SwitchController.switchToParagraph();
    }

    @FXML
    public void onSynonymButtonClick() throws IOException {
        SwitchController.switchToSynonym();
    }

    @FXML
    public void onSettingsButtonClick() throws IOException {
        SwitchController.switchToSetting();
    }

//    @FXML
//    public void onGameButtonClick() throws IOException {
//        SwitchController.switchToGame();
//    }
}

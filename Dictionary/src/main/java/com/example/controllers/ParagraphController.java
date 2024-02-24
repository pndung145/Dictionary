package com.example.controllers;

import api.*;
import com.example.mainApp.Notification;
import com.example.settings.AudioSetting;
import com.example.settings.InternetConnect;
import com.example.settings.cssSetting;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ParagraphController extends MainController {

    private Alert alert;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ChoiceBox<String> inputTypeLanguage;

    @FXML
    private ChoiceBox<String> outputTypeLanguage;

    @FXML
    private TextArea inputTextArea;

    @FXML
    private TextArea outputTextArea;

    private Thread voiceRegThread;

    @FXML
    public void initialize() {
        inputTypeLanguage.setItems(FXCollections.observableArrayList("English", "Vietnamese"));
        outputTypeLanguage.setItems(FXCollections.observableArrayList("Vietnamese", "English"));
        inputTypeLanguage.setValue("English");
        outputTypeLanguage.setValue("Vietnamese");
        alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Voice Recognition");
        alert.setContentText("Đang xử lý âm thanh...");
        alert.setResizable(false);
        alert.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Button cancel = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
        EventHandler<ActionEvent> event = e -> voiceRegThread.stop();
        cancel.setOnAction(event);
    }


    @FXML
    public void onTranslateButtonClick() throws IOException {
        String input = inputTextArea.getText();
        InternetConnect internetConnect = new InternetConnect();
        if (InternetConnect.isInternetAvailable()) {
            Thread thread = new Thread(() -> {
                String output = null;
                try {
                    if (inputTypeLanguage.getValue().equals("English")) {
                        output = GoogleAPI.translate("en", "vi", input);
                    } else {
                        output = GoogleAPI.translate("vi", "en", input);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String finaloutput = output;
                Platform.runLater(() -> outputTextArea.setText(finaloutput));
            });
            thread.setDaemon(true);
            thread.start();
            outputTextArea.setPromptText("Đang dịch...");
            outputTextArea.setText(null);
        } else {
            Notification.show("Internet hiện tại không khả dụng!", rootPane, true);
        }
    }

    public void onPlayAudioInputBtn() {
        AudioSetting.setConfig();
        if (AudioSetting.getConfig()) {
            Thread thread = new Thread(() -> TextToSpeechAPIOnline.getTextToSpeech(inputTextArea.getText(), inputTypeLanguage.getValue()));
            thread.setDaemon(true);
            thread.start();
        } else {
            Thread thread = new Thread(() -> TextToSpeechAPIOffline.getTextToSpeech(inputTextArea.getText()));
            thread.setDaemon(true);
            thread.start();
        }
    }

    public void onSwitchInputLanguage() {
        if (inputTypeLanguage.getValue().equals("English")) outputTypeLanguage.setValue("Vietnamese");
        else outputTypeLanguage.setValue("English");
    }

    public void onSwitchOutputLanguage() {
        if (outputTypeLanguage.getValue().equals("English")) inputTypeLanguage.setValue("Vietnamese");
        else inputTypeLanguage.setValue("English");
    }

    public void onPlayAudioOutputBtn() {
        AudioSetting.setConfig();
        if (AudioSetting.getConfig()) {
            Thread thread = new Thread(() -> TextToSpeechAPIOnline.getTextToSpeech(outputTextArea.getText(),outputTypeLanguage.getValue()));
            thread.setDaemon(true);
            thread.start();
        } else {
            Thread thread = new Thread(() -> TextToSpeechAPIOffline.getTextToSpeech(outputTextArea.getText()));
            thread.setDaemon(true);
            thread.start();
        }
    }

    @FXML
    public void onInputCopyToClipboard() {
        copyToClipBoard(inputTextArea);
    }

    @FXML
    public void onOutputCopyToClipboard() {
        copyToClipBoard(outputTextArea);
    }


    public void copyToClipBoard(TextArea text) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(text.getText());
        clipboard.setContent(content);
        Notification.show("Đã sao chép vào bộ nhớ tạm!", rootPane, cssSetting.getConfig());
    }

    @FXML
    public void onMicrophoneButtonClick() {
        InternetConnect internetConnect = new InternetConnect();
        if (internetConnect.isInternetAvailable()) {
            if (!AudioManager.isRecording()) {
                Notification.show("Bắt đầu ghi âm. Bấm nút microphone để dừng", rootPane, cssSetting.getConfig());
                voiceRegThread = new Thread(() -> {
                    AudioManager.startRecording();
                    String searchResult = SpeechToTextAPI.getSpeechToText();
                    Platform.runLater(() -> {
                        alert.close();
                        inputTextArea.setText(searchResult);
                    });
                });
                voiceRegThread.setDaemon(true);
                voiceRegThread.start();
            } else {
                alert.show();
                AudioManager.stopRecording();
            }
        } else {
            Notification.show("Internet hiện tại không khả dụng!", rootPane, true);
        }
    }
}

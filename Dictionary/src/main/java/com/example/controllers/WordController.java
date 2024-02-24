package com.example.controllers;

import api.AudioManager;
import api.SpeechToTextAPI;
import api.TextToSpeechAPIOffline;
import api.TextToSpeechAPIOnline;
import base.advanced.Dictionary;
import com.example.mainApp.Notification;
import com.example.mainApp.popWindow;
import com.example.settings.AudioSetting;
import com.example.settings.InternetConnect;
import com.example.settings.cssSetting;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Optional;

public class WordController  extends MainController {


    private Alert alert;
    private ArrayList<String> outputDictionary;
    private int countLazy = 0;
    private String currentWord ;
    private String currentTypeWord = "";
    private Thread voiceRegThread;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button editWordButton, deleteWordButton;
    @FXML
    private HBox definitionHBox;
    @FXML
    private Label wordLabel, pronounLabel;
    @FXML
    private ScrollPane listWordScroll;
    @FXML
    private TextField searchInput;
    @FXML
    private VBox outputVbox, explainVbox;


    public AnchorPane getRootPane() {
        return rootPane;
    }

    @FXML
    public void initialize() {
        definitionHBox.setVisible(false);
        Thread thread = new Thread(() -> Platform.runLater(() -> {
            outputDictionary = Dictionary.dictionarySearcher(currentTypeWord);
            Platform.runLater(this::addListWordButton);
        }));
        thread.setDaemon(true);
        thread.start();
        //load on scroll

        listWordScroll.vvalueProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (listWordScroll.getVvalue() >= 0.75) {
                countLazy += 50;
                addListWordButton();
            }
        });

        // record
        alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Voice Recognition");
        alert.setContentText("Đang xử lý âm thanh...");
        alert.setResizable(false);
        alert.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Button cancel = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
        EventHandler<ActionEvent> event = e -> voiceRegThread.stop();
        cancel.setOnAction(event);

//        listWordScroll.setOnKeyPressed(e -> {
//            if (e.getCode() == KeyCode.UP) {
//                navigateWords(-1);
//                addListWordButton();// Navigate to previous word
//            } else if (e.getCode() == KeyCode.DOWN) {
//                navigateWords(1);
//                addListWordButton();// Navigate to next word
//            }
//        });
    }

//    private void navigateWords(int direction) {
//        for (int i = 0; i < outputDictionary.size(); i++) {
//              int currentIndex = i;
//            if (currentIndex < 0) {
//                currentIndex = 0; // Prevent going below the first element
//            } else if (currentIndex >= outputDictionary.size()) {
//                currentIndex = outputDictionary.size() - 1; // Prevent going above the last element
//            }
//
//           outputDictionary.get(currentIndex);
//        }
//    }

    public void onPressEnterSearchInput() {
        if (!outputDictionary.get(0).equals("")) {
            onClickResultButton(outputDictionary.get(0));
        }
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
                        searchInput.setText(searchResult);
                        onTypeSearchInput();
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

    public void onTypeSearchInput() {
        String input = searchInput.getText();
        currentTypeWord = input;
        countLazy = 0;
        Thread thread = new Thread(() -> {
            outputDictionary = Dictionary.dictionarySearcher(input);
            Platform.runLater(() -> {
                outputVbox.getChildren().clear();
                addListWordButton();
            });
        });
        thread.setDaemon(true);
        thread.start();
      }

    @FXML
    public void onPlayAudioButton() {
        AudioSetting.setConfig();
        if (!AudioSetting.getConfig()) {
            Thread thread = new Thread(() -> TextToSpeechAPIOffline.getTextToSpeech(wordLabel.getText()));
            thread.setDaemon(true);
            thread.start();
        } else {
            Thread thread = new Thread(() -> TextToSpeechAPIOnline.getTextToSpeech(wordLabel.getText(), "English"));
            thread.setDaemon(true);
            thread.start();
        }
    }

    private void addListWordButton() {
        if (outputDictionary.get(0).equals("")) {
            Label notExist = new Label("There were no matches");
            Button addWord = new Button("Add this word");
            EventHandler<ActionEvent> event = e -> new popWindow("Add", searchInput.getText());
            notExist.setWrapText(true);
            notExist.setPadding(new Insets(0, 0, 10, 0));
            addWord.getStyleClass().add("btn");
            addWord.setOnAction(event);
            outputVbox.getChildren().add(notExist);
            outputVbox.getChildren().add(addWord);
            return;
        }
        for (int i = countLazy; i < Math.min(outputDictionary.size(), countLazy + 50); i++) {
            String result = outputDictionary.get(i);
            Button resultButton = new Button(result);
            EventHandler<ActionEvent> event = e -> onClickResultButton(result);
            resultButton.getStyleClass().add("menu-btn");
            resultButton.setWrapText(true);
            resultButton.setOnAction(event);
            outputVbox.getChildren().add(resultButton);
        }
    }

    public void onClickResultButton(String result) {
        explainVbox.getChildren().clear();
        JSONObject selectedWord;
        selectedWord = Dictionary.dictionaryLookup(result);
        currentWord = result;
        wordLabel.setText(currentWord);
        if (!selectedWord.getString("pronoun").equals(""))
            pronounLabel.setText(String.format("[%s]", selectedWord.getString("pronoun")));
        else pronounLabel.setText("");
        definitionHBox.setVisible(true);
        JSONArray type = selectedWord.getJSONArray("type");
        createTree(type, 0);
    }

    private void createTree(JSONArray array, int depth) {
        for (int i = 0; i < array.length(); i++) {
            JSONObject child = array.getJSONObject(i);
            String childName = child.keys().next();
            if (child.get(childName) instanceof JSONArray) {
                Label childLabel = new Label(duplicateTab(depth) + childName);
                explainVbox.getChildren().add(setStyleLabelByDepth(childLabel, depth));
                JSONArray childArray = child.getJSONArray(childName);
                createTree(childArray, depth + 1);
            } else {
                Label childLabel = new Label(duplicateTab(depth) + childName + ": " + child.getString(childName));
                explainVbox.getChildren().add(setStyleLabelByDepth(childLabel, depth));
            }

        }
    }

    private Label setStyleLabelByDepth(Label label, int depth) {
        if (depth == 0) {
            label.getStyleClass().add("word-type-label");
        } else if (depth == 1) {
            label.getStyleClass().add("word-explain-label");
        } else if (depth == 2) {
            label.getStyleClass().add("word-example-label");
        }
        label.setWrapText(true);
        return label;
    }

    private String duplicateTab(int times) {
        return "\t".repeat(Math.max(0, times));
    }

    @FXML
    public void onEditWordButton() {
        new popWindow("Edit", currentWord);
    }

    @FXML
    public void onDeleteWordButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setResizable(false);
        alert.setContentText("Bạn chắc chắn muốn xoá từ này?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Dictionary.dictionaryDelete(currentWord);
            onTypeSearchInput();
            explainVbox.getChildren().clear();
            definitionHBox.setVisible(false);
            Notification.show("Xoá từ thành công", rootPane, cssSetting.getConfig());
        }
    }
}

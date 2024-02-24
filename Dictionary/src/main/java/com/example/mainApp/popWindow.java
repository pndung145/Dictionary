package com.example.mainApp;

import base.advanced.Dictionary;
import com.example.controllers.PopController;
import com.example.controllers.SwitchController;
import com.example.settings.cssSetting;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class popWindow {
    public popWindow(String title, String word) {
        try {
            Stage stage = new Stage();
            FXMLLoader root = new FXMLLoader(getClass().getResource("/com/example/pop.fxml"));
            Scene scene = new Scene(root.load(), 720, 720, false, SceneAntialiasing.BALANCED);
            scene.getStylesheets().add(cssSetting.getConfig() ? SwitchController.DARK_CSS : SwitchController.LIGHT_CSS);
            stage.setTitle(title);
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(SwitchController.getStage());
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
            PopController popController = root.getController();
            popController.setWordTextField(word);
            if (title.equals("Edit")) loadWordStructure(popController);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadWordStructure(PopController popController) {
        JSONObject selectedWord = Dictionary.dictionaryLookup(popController.getWordTextField().getText());
        popController.setPronounTextField(selectedWord.getString("pronoun"));
        JSONArray type = selectedWord.getJSONArray("type");
        for (int i = 0; i < type.length(); i++) {
            JSONObject typeChild = type.getJSONObject(i);
            String typeChildName = typeChild.keys().next();
            popController.setTypeText(typeChildName);
            popController.onClickAddTypeButton();
            JSONArray explain = typeChild.getJSONArray(typeChildName);
            for (int j = 0; j < explain.length(); j++) {
                JSONObject explainChild = explain.getJSONObject(j);
                String exampleChildName = explainChild.keys().next();
                popController.setExplainText(exampleChildName);
                popController.onClickAddExplainButton(popController.getCurrentTypeParentVBox());
                JSONArray example = explainChild.getJSONArray(exampleChildName);
                for (int k = 0; k < example.length(); k++) {
                    JSONObject exampleChild = example.getJSONObject(k);
                    String exampleLeft = exampleChild.keys().next();
                    String exampleRight = exampleChild.getString(exampleLeft);
                    popController.setExampleText1(exampleLeft);
                    popController.setExampleText2(exampleRight);
                    popController.onClickAddExampleButton(popController.getCurrentExplainParentVBox());
                }
            }
        }
    }
}

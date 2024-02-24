module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.json;
    requires freetts;
    requires mongo.java.driver;
    requires java.desktop;
    requires java.logging;
    requires org.apache.commons.text;
    requires voicerss.tts;
    requires org.controlsfx.controls;
    requires com.fasterxml.jackson.databind;

    opens com.example to javafx.fxml;
    exports com.example.controllers;
    opens com.example.controllers to javafx.fxml;
    exports com.example.mainApp;
    opens com.example.mainApp to javafx.fxml;
}
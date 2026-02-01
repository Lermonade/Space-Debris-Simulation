package com.lerstudios.space_debris_simulation;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public final class Console {

    private final VBox consoleBox;

    public Console(VBox consoleBox) {
        this.consoleBox = consoleBox;
    }

    public void log(String text) {
        if (Platform.isFxApplicationThread()) {
            addTextToConsole(text);
        } else {
            Platform.runLater(() -> addTextToConsole(text));
        }
    }

    public void addTextToConsole(String text) {
        Label label = new Label("> " + text);

        label.setTextFill(javafx.scene.paint.Color.WHITE);
        label.setFont(Font.font("Consolas", 12));
        label.setWrapText(true);

        VBox.setMargin(label, new javafx.geometry.Insets(0, 0, 16, 0));

        consoleBox.getChildren().add(label);
    }
}


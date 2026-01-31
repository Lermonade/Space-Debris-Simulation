package com.lerstudios.space_debris_simulation;

import com.lerstudios.space_debris_simulation.configurationUtilities.uiBuilders.ConstantsUIBuilder;
import com.lerstudios.space_debris_simulation.configurationUtilities.FileService;
import com.lerstudios.space_debris_simulation.configurationUtilities.SimulationSettings;
import com.lerstudios.space_debris_simulation.configurationUtilities.uiBuilders.PopulationsUIBuilder;
import com.lerstudios.space_debris_simulation.configurationUtilities.uiBuilders.SourcesUIBuilder;
import com.lerstudios.space_debris_simulation.configurationUtilities.uiBuilders.RemovalMethodsUIBuilder;
import com.lerstudios.space_debris_simulation.utils.Constants;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;

public class configurationController {

    @FXML
    private VBox consoleBox;

    @FXML
    private TextField simulationTitle;

    @FXML
    private TextField simulationDescription;

    @FXML
    private VBox constantsBox;

    @FXML
    private VBox populationsBox;

    @FXML
    private VBox sourcesBox;

    @FXML
    private VBox removalMethodsBox;

    private SimulationSettings settings;
    private Path settingsPath;

    @FXML
    private void initialize() {
    }

    public void runSimulation() {
        addTextToConsole("Running Simulation '" + settings.simulationName + "'");

        showSimulationProgressPopup("Running Simulation '" + settings.simulationName + "'");
    }

    public void addPopulation() {
        PopulationsUIBuilder.addPopulationComponent("Unnamed", populationsBox, settings.populationObjects);
    }

    public void addKeplerianDistribution() {
        SourcesUIBuilder.addKeplerianSourceComponent("Unnamed", sourcesBox, settings.keplerianDistributionSources);
    }

    public void addOrbitalLaserRemovalMethod() {
        RemovalMethodsUIBuilder.addOrbitalLaserComponent("Unnamed", removalMethodsBox, settings.removalmethods);
    }

    public void createNewProjectFile() throws IOException {
        FileService.createEmptyProjectFile(Constants.appName, "Project", "Project", "Newly Created Project");
        addTextToConsole("File Generated");
    }

    private void setupListeners() {
        simulationTitle.textProperty().addListener((obs, oldVal, newVal) -> {
            settings.simulationName = newVal;
        });

        simulationDescription.textProperty().addListener((obs, oldVal, newVal) -> {
            settings.simulationDescription = newVal;
        });
    }

    @FXML
    private void onSave() {
        settingsPath = FileService.saveProjectSettings(settingsPath, settings);
        addTextToConsole("Saved '" + settings.simulationName + "' in /user/documents/" + Constants.appName + ".");
    }

    @FXML
    public void addTextToConsole(String text) {
        Label label = new Label("> " + text);

        label.setTextFill(javafx.scene.paint.Color.WHITE);
        label.setFont(Font.font("Consolas", 12));
        label.setWrapText(true);

        VBox.setMargin(label, new javafx.geometry.Insets(0, 0, 16, 0));

        consoleBox.getChildren().add(label);
    }

    public void initProject(Path projectFile) {
        settings = FileService.parseProjectSettings(projectFile);
        settingsPath = projectFile;

        simulationTitle.setText(settings.simulationName);
        simulationDescription.setText(settings.simulationDescription);

        ConstantsUIBuilder.buildConstantsUI(constantsBox, settings);
        PopulationsUIBuilder.loadPopulationsFromSettings(settings, populationsBox);
        SourcesUIBuilder.loadSourcesFromSettings(settings, sourcesBox);
        RemovalMethodsUIBuilder.loadRemovalMethodsFromSettings(settings, removalMethodsBox);

        setupListeners();
    }

    public static Stage showSimulationProgressPopup(String title) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle(title);
        popupStage.setResizable(false);

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Label messageLabel = new Label("0 / 20 seconds");
        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(300);

        Button closeButton = new Button("Close");
        closeButton.setDisable(true);

        root.getChildren().addAll(messageLabel, progressBar, closeButton);

        Scene scene = new Scene(root);
        popupStage.setScene(scene);
        popupStage.centerOnScreen();
        popupStage.show();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                int totalSeconds = 20;
                for (int i = 1; i <= totalSeconds; i++) {
                    Thread.sleep(1000);
                    final int second = i;
                    Platform.runLater(() -> {
                        progressBar.setProgress(second / (double) totalSeconds);
                        messageLabel.setText(second + " / " + totalSeconds + " seconds");
                    });
                }
                return null;
            }
        };

        task.setOnSucceeded(ev -> {
            closeButton.setDisable(false);
        });

        closeButton.setOnAction(ev -> popupStage.close());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        return popupStage;
    }


}

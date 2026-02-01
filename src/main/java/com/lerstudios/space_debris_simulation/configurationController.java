package com.lerstudios.space_debris_simulation;

import com.lerstudios.space_debris_simulation.configurationUtilities.uiBuilders.ConstantsUIBuilder;
import com.lerstudios.space_debris_simulation.configurationUtilities.FileService;
import com.lerstudios.space_debris_simulation.configurationUtilities.SimulationSettings;
import com.lerstudios.space_debris_simulation.configurationUtilities.uiBuilders.PopulationsUIBuilder;
import com.lerstudios.space_debris_simulation.configurationUtilities.uiBuilders.SourcesUIBuilder;
import com.lerstudios.space_debris_simulation.configurationUtilities.uiBuilders.RemovalMethodsUIBuilder;
import com.lerstudios.space_debris_simulation.simulation.Simulation;
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

    private Console console;
    private Simulation simulation;

    @FXML
    private void initialize() {
        console = new Console(consoleBox);
        simulation = new Simulation(console);
    }

    public void runSimulation() {
        console.addTextToConsole("Running Simulation '" + settings.simulationName + "'");

        simulation.runSimulation("Running Simulation '" + settings.simulationName + "'", settings);
    }

    public void addPopulation() {
        PopulationsUIBuilder.addPopulationComponent("Unnamed", populationsBox, settings.populationObjects);
    }

    public void addKeplerianDistribution() {
        SourcesUIBuilder.addKeplerianSourceComponent("Unnamed", sourcesBox, settings.sources);
    }

    public void addOrbitalLaserRemovalMethod() {
        RemovalMethodsUIBuilder.addOrbitalLaserComponent("Unnamed", removalMethodsBox, settings.removalmethods);
    }

    public void createNewProjectFile() throws IOException {
        FileService.createEmptyProjectFile(Constants.appName, "Project", "Project", "Newly Created Project");
        console.addTextToConsole("File Generated");
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
        console.addTextToConsole("Saved '" + settings.simulationName + "' in /user/documents/" + Constants.appName + ".");
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
}

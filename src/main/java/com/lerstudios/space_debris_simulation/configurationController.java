package com.lerstudios.space_debris_simulation;

import com.lerstudios.space_debris_simulation.configurationUtilities.uiBuilders.ConstantsUIBuilder;
import com.lerstudios.space_debris_simulation.configurationUtilities.SimulationSettings;
import com.lerstudios.space_debris_simulation.configurationUtilities.uiBuilders.PopulationsUIBuilder;
import com.lerstudios.space_debris_simulation.configurationUtilities.uiBuilders.SourcesUIBuilder;
import com.lerstudios.space_debris_simulation.configurationUtilities.uiBuilders.RemovalMethodsUIBuilder;
import com.lerstudios.space_debris_simulation.simulation.Simulation;
import com.lerstudios.space_debris_simulation.simulation.exportFormats.VisualizationFile;
import com.lerstudios.space_debris_simulation.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

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
        onSave();
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

    public void openVisualizationFile(ActionEvent event) throws IOException {
        VisualizationFile format = FileService.openVisualizationFile(Constants.appName, settings.simulationName, sourcesBox.getScene().getWindow());

        FXMLLoader loader = new FXMLLoader(
                Objects.requireNonNull(getClass().getResource("visualization.fxml"))
        );
        Parent root = loader.load();

        visualizationController controller = loader.getController();
        controller.initVisualization(format);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }

    public void backToMain(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                Objects.requireNonNull(getClass().getResource("main.fxml"))
        );
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }

}

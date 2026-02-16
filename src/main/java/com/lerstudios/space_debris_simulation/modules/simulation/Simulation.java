package com.lerstudios.space_debris_simulation.modules.simulation;

import com.lerstudios.space_debris_simulation.Console;
import com.lerstudios.space_debris_simulation.FileService;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.DynamicSwitchingPropagationMethod;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.PropagationData;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData.StaticKeplerianPropagationData;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationMethod;
import com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.StaticKeplerianPropagationMethod;
import com.lerstudios.space_debris_simulation.modules.configuration.SimulationSettings;
import com.lerstudios.space_debris_simulation.modules.configuration.dataTypes.PopulationObject;
import com.lerstudios.space_debris_simulation.modules.configuration.dataTypes.orbitSources.KeplerianDistributionSource;
import com.lerstudios.space_debris_simulation.modules.configuration.dataTypes.orbitSources.SourceTemplate;
import com.lerstudios.space_debris_simulation.modules.configuration.dataTypes.removalMethods.OrbitalLaserRemovalMethod;
import com.lerstudios.space_debris_simulation.modules.configuration.dataTypes.removalMethods.RemovalMethodTemplate;
import com.lerstudios.space_debris_simulation.modules.simulation.removalMethods.Simulation_OrbitalLaser;
import com.lerstudios.space_debris_simulation.modules.simulation.removalMethods.Simulation_RemovalMethod;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.SimToVis_File;
import com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.SimToVis_Formatter;
import com.lerstudios.space_debris_simulation.modules.simulation.removalMethods.Simulation_RemovalMethodBase;
import com.lerstudios.space_debris_simulation.types.*;
import com.lerstudios.space_debris_simulation.modules.simulation.populations.dataSources.Simulation_KeplerianDistributionSource;
import com.lerstudios.space_debris_simulation.modules.simulation.populations.dataSources.Simulation_OrbitDataSource;
import com.lerstudios.space_debris_simulation.modules.simulation.populations.Simulation_Population;
import com.lerstudios.space_debris_simulation.modules.simulation.populations.populationObjects.Simulation_StaticKeplerianObject;
import com.lerstudios.space_debris_simulation.modules.simulation.populations.populationObjects.Simulation_Object;
import com.lerstudios.space_debris_simulation.utils.Constants;
import com.lerstudios.space_debris_simulation.types.RenderingMethod;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Simulation {

    private final Console console;
    private Results results;

    public Simulation(Console console) {
        this.console = console;
    }

    public Stage runSimulation(String title, SimulationSettings settings) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle(title);
        popupStage.setResizable(false);

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        Label messageLabel = new Label();
        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(300);

        Button closeButton = new Button("Close");
        closeButton.setDisable(true);

        root.getChildren().addAll(messageLabel, progressBar, closeButton);

        Scene scene = new Scene(root);
        popupStage.setScene(scene);
        popupStage.centerOnScreen();
        popupStage.show();

        // Create task
        Task<Void> task = createSimulationTask(settings);

        // Bind UI to task
        progressBar.progressProperty().bind(task.progressProperty());
        messageLabel.textProperty().bind(task.messageProperty());

        task.setOnSucceeded(ev -> closeButton.setDisable(false));

        closeButton.setOnAction(ev -> popupStage.close());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        return popupStage;
    }

    private Task<Void> createSimulationTask(SimulationSettings settings) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                double totalSteps = settings.getNumber("Total Time Steps", 10);
                double seconds = settings.getNumber("Time Step (s)", 10);
                SimUtils.setEndTimeStep(totalSteps);
                SimUtils.setSecondsPerTimeStep(seconds);
                SimUtils.setCurrentTimeStep(0);

                results = new Results(10, SimUtils.getEndTimeStep());

                SimUtils.resetID();

                ArrayList<Simulation_Population> populations = ParseSettingsFile.generateOrbitalPopulations(settings, console);
                ArrayList<Simulation_RemovalMethodBase> removalMethods = ParseSettingsFile.generateRemovalMethods(settings);

                for (int i = 0; i <= totalSteps; i++) {
                    SimUtils.setCurrentTimeStep(i);

                    for(Simulation_Population p: populations) {
                        p.updateAllObjects(i);
                    }

                    for(Simulation_RemovalMethodBase r: removalMethods) {
                        r.propagate(i);
                    }

                    results.update(SimUtils.getCurrentTimeStep());

                    Thread.sleep(1);
                    updateProgress(i, totalSteps);
                    updateMessage(i + " / " + totalSteps + " steps");
                }

                results.printFinalValues();

                if (SimUtils.getCreateVisualizationFile()) {
                    SimToVis_File file = SimToVis_Formatter.createVisualizationFile(settings, populations, removalMethods);
                    FileService.createEmptyVisualizationFile(Constants.appName, settings.simulationName, file);
                }

                return null;
            }
        };
    }

}

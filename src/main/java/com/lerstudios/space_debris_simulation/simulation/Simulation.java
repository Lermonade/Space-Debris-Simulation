package com.lerstudios.space_debris_simulation.simulation;

import com.lerstudios.space_debris_simulation.Console;
import com.lerstudios.space_debris_simulation.configurationUtilities.SimulationSettings;
import com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes.PopulationObject;
import com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes.orbitSources.KeplerianDistributionSource;
import com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes.orbitSources.SourceTemplate;
import com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes.orbitSources.SourceType;
import com.lerstudios.space_debris_simulation.simulation.OrbitDataSource.KeplerianDistribution;
import com.lerstudios.space_debris_simulation.simulation.OrbitDataSource.OrbitDataSource;
import com.lerstudios.space_debris_simulation.simulation.OrbitPopulations.ObjectClassification;
import com.lerstudios.space_debris_simulation.simulation.OrbitPopulations.OrbitalPopulation;
import com.lerstudios.space_debris_simulation.simulation.Propagation.KeplerianOrbitalObject;
import com.lerstudios.space_debris_simulation.simulation.Propagation.OrbitalObject;
import com.lerstudios.space_debris_simulation.simulation.Propagation.PropegationMethod;
import com.lerstudios.space_debris_simulation.simulation.Propagation.TimedKeplerianElements;
import com.lerstudios.space_debris_simulation.visualizationUtilities.RenderingMethod;
import javafx.application.Platform;
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
import java.util.concurrent.atomic.AtomicInteger;

public class Simulation {

    private final Console console;

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
                SimUtils.setEndTimeStep(totalSteps);
                SimUtils.setCurrentTimeStep(0);

                ArrayList<OrbitalPopulation> populations = generateOrbitalPopulations(settings);

                for (OrbitalPopulation pop : populations) {
                    for(OrbitalObject obj : pop.orbitalObjects) {
                        System.out.println(obj.getName());

                        if(pop.propegationMethod == PropegationMethod.DYNAMIC_FORCE_MODEL) {
                            KeplerianOrbitalObject obj2 = (KeplerianOrbitalObject) obj;
                            TimedKeplerianElements element1 = obj2.elements.getFirst();
                            System.out.println(element1.elements().inclination());
                        }
                    }
                }


                for (int i = 1; i <= totalSteps; i++) {
                    SimUtils.setCurrentTimeStep(i);

                    Thread.sleep(10);
                    updateProgress(i, totalSteps);
                    updateMessage(i + " / " + totalSteps + " steps");
                }
                return null;
            }
        };
    }

    private ArrayList<OrbitalPopulation> generateOrbitalPopulations(SimulationSettings settings) {
        Map<String, OrbitDataSource> sourceMap = new HashMap<>();

        ArrayList<OrbitalPopulation> populations = new ArrayList<>();

        for (SourceTemplate source : settings.sources) {
            if (source.getSourceType() == SourceType.KEPLERIAN) {
                sourceMap.put(source.getName(), new KeplerianDistribution(source.getName(), (KeplerianDistributionSource) source));
            }
        }

        for (PopulationObject population : settings.populationObjects) {

            String populationName = population.populationName;
            ObjectClassification classification = ObjectClassification.DEBRIS;
            PropegationMethod propegationMethod = PropegationMethod.DYNAMIC_FORCE_MODEL;
            RenderingMethod renderingMethod = RenderingMethod.OBJECTS_3D;
            String color = population.renderingColor;
            String sourceName = population.source;
            int count2 = Integer.parseInt(population.count);

            if (population.objectClassification.equals("Operational Satellite")) {
                classification = ObjectClassification.SATELLITE;
            } else if (population.objectClassification.equals("Debris")) {
                classification = ObjectClassification.DEBRIS;
            }

            if (population.propagationMethod.equals("Two-Body Keplerian")) {
                propegationMethod = PropegationMethod.STATIC_KEPLERIAN;
            } else if (population.propagationMethod.equals("Dynamic Propegation Switching")) {
                propegationMethod = PropegationMethod.DYNAMIC_FORCE_MODEL;
            }

            if (population.renderingMethod.equals("3D Objects")) {
                renderingMethod = RenderingMethod.OBJECTS_3D;
            } else if (population.renderingMethod.equals("Particle Billboards")) {
                renderingMethod = RenderingMethod.PARTICLE_BILLBOARDS;
            }

            OrbitalPopulation population1 = new OrbitalPopulation(
                    populationName,
                    classification,
                    propegationMethod,
                    renderingMethod,
                    color,
                    sourceName,
                    count2,
                    sourceMap
                    );

            population1.generateDebrisObjects();
            populations.add(population1);
        }

        console.log("Created " + SimUtils.getCurrentID() + " simulation objects!");
        return populations;
    }

}

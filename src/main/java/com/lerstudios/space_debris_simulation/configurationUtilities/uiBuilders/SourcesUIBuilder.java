package com.lerstudios.space_debris_simulation.configurationUtilities.uiBuilders;

import com.lerstudios.space_debris_simulation.configurationUtilities.SimulationSettings;
import com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes.orbitSources.KeplerianDistributionSource;
import com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes.orbitSources.SourceTemplate;
import com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes.orbitSources.SourceType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;

public class SourcesUIBuilder {

    public static void addKeplerianSourceComponent(
            KeplerianDistributionSource source,
            VBox sourcesBox,
            ArrayList<SourceTemplate> sourceList
    ) {

        VBox sourceVBox = new VBox();
        sourceVBox.setPrefWidth(300);
        sourceVBox.setStyle("-fx-border-color: #000000;");
        sourceVBox.setSpacing(0);

        java.util.function.BiFunction<String, javafx.scene.Node, HBox> createRow =
                (labelText, inputNode) -> {

                    Label label = new Label(labelText);
                    label.setFont(Font.font("Consolas", 12));

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    HBox row = new HBox(label, spacer, inputNode);
                    row.setAlignment(Pos.CENTER_LEFT);
                    row.setMinHeight(32);
                    row.setPrefHeight(32);
                    row.setMaxHeight(32);
                    row.setPadding(new Insets(0, 16, 0, 16));
                    row.setStyle(
                            "-fx-border-color: transparent transparent #e5e7eb transparent;" +
                                    "-fx-border-width: 0 0 2 0;"
                    );

                    return row;
                };

        TextField sourceNameField = new TextField(source.sourceName);

        TextField smaMinField = new TextField(source.semiMajorAxisMin);
        TextField smaAvgField = new TextField(source.semiMajorAxisAvg);
        TextField smaMaxField = new TextField(source.semiMajorAxisMax);

        TextField eccMinField = new TextField(source.eccentricityMin);
        TextField eccAvgField = new TextField(source.eccentricityAvg);
        TextField eccMaxField = new TextField(source.eccentricityMax);

        TextField incMinField = new TextField(source.inclinationMin);
        TextField incAvgField = new TextField(source.inclinationAvg);
        TextField incMaxField = new TextField(source.inclinationMax);

        TextField raanMinField = new TextField(source.raanMin);
        TextField raanAvgField = new TextField(source.raanAvg);
        TextField raanMaxField = new TextField(source.raanMax);

        TextField argPerMinField = new TextField(source.argPeriapsisMin);
        TextField argPerAvgField = new TextField(source.argPeriapsisAvg);
        TextField argPerMaxField = new TextField(source.argPeriapsisMax);

        TextField anomalyMinField = new TextField(source.anomalyMin);
        TextField anomalyAvgField = new TextField(source.anomalyAvg);
        TextField anomalyMaxField = new TextField(source.anomalyMax);

        TextField massMinField = new TextField(source.massMin);
        TextField massAvgField = new TextField(source.massAvg);
        TextField massMaxField = new TextField(source.massMax);

        if (!sourceList.contains(source)) {
            sourceList.add(source);
        }

        sourceNameField.textProperty().addListener(
                (obs, o, n) -> source.sourceName = n
        );

        smaMinField.textProperty().addListener((obs, o, n) -> source.semiMajorAxisMin = n);
        smaAvgField.textProperty().addListener((obs, o, n) -> source.semiMajorAxisAvg = n);
        smaMaxField.textProperty().addListener((obs, o, n) -> source.semiMajorAxisMax = n);

        eccMinField.textProperty().addListener((obs, o, n) -> source.eccentricityMin = n);
        eccAvgField.textProperty().addListener((obs, o, n) -> source.eccentricityAvg = n);
        eccMaxField.textProperty().addListener((obs, o, n) -> source.eccentricityMax = n);

        incMinField.textProperty().addListener((obs, o, n) -> source.inclinationMin = n);
        incAvgField.textProperty().addListener((obs, o, n) -> source.inclinationAvg = n);
        incMaxField.textProperty().addListener((obs, o, n) -> source.inclinationMax = n);

        raanMinField.textProperty().addListener((obs, o, n) -> source.raanMin = n);
        raanAvgField.textProperty().addListener((obs, o, n) -> source.raanAvg = n);
        raanMaxField.textProperty().addListener((obs, o, n) -> source.raanMax = n);

        argPerMinField.textProperty().addListener((obs, o, n) -> source.argPeriapsisMin = n);
        argPerAvgField.textProperty().addListener((obs, o, n) -> source.argPeriapsisAvg = n);
        argPerMaxField.textProperty().addListener((obs, o, n) -> source.argPeriapsisMax = n);

        anomalyMinField.textProperty().addListener((obs, o, n) -> source.anomalyMin = n);
        anomalyAvgField.textProperty().addListener((obs, o, n) -> source.anomalyAvg = n);
        anomalyMaxField.textProperty().addListener((obs, o, n) -> source.anomalyMax = n);

        massMinField.textProperty().addListener((obs, o, n) -> source.massMin = n);
        massAvgField.textProperty().addListener((obs, o, n) -> source.massAvg = n);
        massMaxField.textProperty().addListener((obs, o, n) -> source.massMax = n);

        Button deleteButton = new Button("Delete Source");
        deleteButton.setMaxWidth(Double.MAX_VALUE);
        deleteButton.setStyle(
                "-fx-background-color: #ef4444;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;"
        );

        deleteButton.setOnAction(e ->
                deleteKeplerianSourceComponent(
                        sourcesBox,
                        sourceVBox,
                        sourceList,
                        source
                )
        );

        HBox deleteRow = new HBox(deleteButton);
        deleteRow.setPadding(new Insets(8, 16, 8, 16));

        sourceVBox.getChildren().addAll(
                createRow.apply("Source Name", sourceNameField),

                createRow.apply("SMA Min", smaMinField),
                createRow.apply("SMA Avg", smaAvgField),
                createRow.apply("SMA Max", smaMaxField),

                createRow.apply("Eccentricity Min", eccMinField),
                createRow.apply("Eccentricity Avg", eccAvgField),
                createRow.apply("Eccentricity Max", eccMaxField),

                createRow.apply("Inclination Min", incMinField),
                createRow.apply("Inclination Avg", incAvgField),
                createRow.apply("Inclination Max", incMaxField),

                createRow.apply("RAAN Min", raanMinField),
                createRow.apply("RAAN Avg", raanAvgField),
                createRow.apply("RAAN Max", raanMaxField),

                createRow.apply("Arg Periapsis Min", argPerMinField),
                createRow.apply("Arg Periapsis Avg", argPerAvgField),
                createRow.apply("Arg Periapsis Max", argPerMaxField),

                createRow.apply("Anomaly Min", anomalyMinField),
                createRow.apply("Anomaly Avg", anomalyAvgField),
                createRow.apply("Anomaly Max", anomalyMaxField),

                createRow.apply("Mass Min", massMinField),
                createRow.apply("Mass Avg", massAvgField),
                createRow.apply("Mass Max", massMaxField),

                deleteRow
        );

        sourcesBox.getChildren().add(sourceVBox);
    }

    public static void addKeplerianSourceComponent(
            String sourceName,
            VBox sourcesBox,
            ArrayList<SourceTemplate> sourceList
    ) {

        KeplerianDistributionSource source =
                new KeplerianDistributionSource(
                        sourceName,

                        /* semi-major axis */ "7000", "7500", "8000",
                        /* eccentricity */   "0.0",  "0.01", "0.05",
                        /* inclination */    "0",    "53",   "98",

                        /* RAAN */           "0",    "180",  "360",
                        /* arg periapsis */  "0",    "180",  "360",
                        /* anomaly */        "0",    "180",  "360",

                        /* mass */           "1",    "10",   "100"
                );

        addKeplerianSourceComponent(source, sourcesBox, sourceList);
    }

    public static void deleteKeplerianSourceComponent(
            VBox sourcesBox,
            VBox sourceVBox,
            ArrayList<SourceTemplate> sourceList,
            KeplerianDistributionSource source
    ) {
        sourcesBox.getChildren().remove(sourceVBox);

        sourceList.remove(source);
    }

    public static void loadSourcesFromSettings(
            SimulationSettings settings,
            VBox sourcesBox
    ) {
        sourcesBox.getChildren().clear();

        for (SourceTemplate source : settings.keplerianDistributionSources) {
            if(source.getSourceType() == SourceType.KEPLERIAN) {
                addKeplerianSourceComponent(
                        (KeplerianDistributionSource) source,
                        sourcesBox,
                        settings.keplerianDistributionSources
                );
            }
        }
    }


}

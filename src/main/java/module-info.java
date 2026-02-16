module com.lerstudios.space_debris_simulation {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires org.fxyz3d.core;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires jdk.jshell;

    exports com.lerstudios.space_debris_simulation.modules.simulation;
    exports com.lerstudios.space_debris_simulation.modules.simulation.populations;
    exports com.lerstudios.space_debris_simulation.modules.simulation.populations.dataSources;
    exports com.lerstudios.space_debris_simulation.types;

    exports com.lerstudios.space_debris_simulation.modules.visualization.populations.Objects;
    exports com.lerstudios.space_debris_simulation.interfaces.PropagationMethods.PropagationData;

    exports com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.objects;
    opens com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.objects to com.fasterxml.jackson.databind;

    exports com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.removalMethods;
    opens com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.removalMethods to com.fasterxml.jackson.databind;

    opens com.lerstudios.space_debris_simulation.modules.simulation to com.fasterxml.jackson.databind;
    opens com.lerstudios.space_debris_simulation.modules.simulation.populations to com.fasterxml.jackson.databind;

    opens com.lerstudios.space_debris_simulation to javafx.fxml;
    exports com.lerstudios.space_debris_simulation;
    exports com.lerstudios.space_debris_simulation.modules.configuration;
    opens com.lerstudios.space_debris_simulation.modules.configuration to javafx.fxml;
    exports com.lerstudios.space_debris_simulation.utils;
    opens com.lerstudios.space_debris_simulation.utils to javafx.fxml;
    exports com.lerstudios.space_debris_simulation.modules.visualization;
    opens com.lerstudios.space_debris_simulation.modules.visualization to javafx.fxml;
    exports com.lerstudios.space_debris_simulation.modules.configuration.uiBuilders;
    opens com.lerstudios.space_debris_simulation.modules.configuration.uiBuilders to javafx.fxml;
    exports com.lerstudios.space_debris_simulation.modules.configuration.dataTypes;
    opens com.lerstudios.space_debris_simulation.modules.configuration.dataTypes to javafx.fxml;
    exports com.lerstudios.space_debris_simulation.modules.configuration.dataTypes.orbitSources;
    opens com.lerstudios.space_debris_simulation.modules.configuration.dataTypes.orbitSources to javafx.fxml;
    exports com.lerstudios.space_debris_simulation.modules.configuration.dataTypes.removalMethods;
    opens com.lerstudios.space_debris_simulation.modules.configuration.dataTypes.removalMethods to javafx.fxml;
    exports com.lerstudios.space_debris_simulation.modules.simulation.populations.populationObjects;
    opens com.lerstudios.space_debris_simulation.modules.simulation.populations.populationObjects to com.fasterxml.jackson.databind;
    opens com.lerstudios.space_debris_simulation.types to com.fasterxml.jackson.databind, javafx.fxml;
    exports com.lerstudios.space_debris_simulation.modules.simulation.exportFormats;
    opens com.lerstudios.space_debris_simulation.modules.simulation.exportFormats to com.fasterxml.jackson.databind;
    exports com.lerstudios.space_debris_simulation.modules.visualization.populations;
    opens com.lerstudios.space_debris_simulation.modules.visualization.populations to javafx.fxml;
    exports com.lerstudios.space_debris_simulation.interfaces.PropagationMethods;
    opens com.lerstudios.space_debris_simulation.interfaces.PropagationMethods to javafx.fxml;
    exports com.lerstudios.space_debris_simulation.interfaces.VisualizationMethods;
    opens com.lerstudios.space_debris_simulation.interfaces.VisualizationMethods to javafx.fxml;
    exports com.lerstudios.space_debris_simulation.interfaces.VisualizationMethods.VisualizationData;
    exports com.lerstudios.space_debris_simulation.modules.visualization.SceneObjects;
    opens com.lerstudios.space_debris_simulation.modules.visualization.SceneObjects to javafx.fxml;
    exports com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.propagationData;
    opens com.lerstudios.space_debris_simulation.modules.simulation.exportFormats.propagationData to com.fasterxml.jackson.databind;

}
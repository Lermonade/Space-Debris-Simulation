module com.lerstudios.space_debris_simulation {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires org.fxyz3d.core;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;

    exports com.lerstudios.space_debris_simulation.simulation;
    exports com.lerstudios.space_debris_simulation.simulation.OrbitPopulations;
    exports com.lerstudios.space_debris_simulation.simulation.OrbitPopulations.OrbitDataSource;
    exports com.lerstudios.space_debris_simulation.simulation.types;

    exports com.lerstudios.space_debris_simulation.visualizationUtilities.populations.Objects;
    exports com.lerstudios.space_debris_simulation.visualizationUtilities.populations.Objects.PropagationData;

    exports com.lerstudios.space_debris_simulation.simulation.exportFormats.objects;
    opens com.lerstudios.space_debris_simulation.simulation.exportFormats.objects to com.fasterxml.jackson.databind;

    opens com.lerstudios.space_debris_simulation.simulation to com.fasterxml.jackson.databind;
    opens com.lerstudios.space_debris_simulation.simulation.OrbitPopulations to com.fasterxml.jackson.databind;

    opens com.lerstudios.space_debris_simulation to javafx.fxml;
    exports com.lerstudios.space_debris_simulation;
    exports com.lerstudios.space_debris_simulation.configurationUtilities;
    opens com.lerstudios.space_debris_simulation.configurationUtilities to javafx.fxml;
    exports com.lerstudios.space_debris_simulation.utils;
    opens com.lerstudios.space_debris_simulation.utils to javafx.fxml;
    exports com.lerstudios.space_debris_simulation.visualizationUtilities;
    opens com.lerstudios.space_debris_simulation.visualizationUtilities to javafx.fxml;
    exports com.lerstudios.space_debris_simulation.configurationUtilities.uiBuilders;
    opens com.lerstudios.space_debris_simulation.configurationUtilities.uiBuilders to javafx.fxml;
    exports com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes;
    opens com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes to javafx.fxml;
    exports com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes.orbitSources;
    opens com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes.orbitSources to javafx.fxml;
    exports com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes.removalMethods;
    opens com.lerstudios.space_debris_simulation.configurationUtilities.dataTypes.removalMethods to javafx.fxml;
    exports com.lerstudios.space_debris_simulation.simulation.Propagation.PopulationObjects;
    opens com.lerstudios.space_debris_simulation.simulation.Propagation.PopulationObjects to com.fasterxml.jackson.databind;
    opens com.lerstudios.space_debris_simulation.simulation.types to com.fasterxml.jackson.databind, javafx.fxml;
    exports com.lerstudios.space_debris_simulation.simulation.exportFormats;
    opens com.lerstudios.space_debris_simulation.simulation.exportFormats to com.fasterxml.jackson.databind;
    exports com.lerstudios.space_debris_simulation.visualizationUtilities.populations;
    opens com.lerstudios.space_debris_simulation.visualizationUtilities.populations to javafx.fxml;
    exports com.lerstudios.space_debris_simulation.visualizationUtilities.populations.PropagationMethods;
    opens com.lerstudios.space_debris_simulation.visualizationUtilities.populations.PropagationMethods to javafx.fxml;
    exports com.lerstudios.space_debris_simulation.visualizationUtilities.populations.VisualizationMethods;
    opens com.lerstudios.space_debris_simulation.visualizationUtilities.populations.VisualizationMethods to javafx.fxml;
    exports com.lerstudios.space_debris_simulation.visualizationUtilities.populations.Objects.VisualizationData;

}
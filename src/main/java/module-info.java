module com.lerstudios.space_debris_simulation {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires org.fxyz3d.core;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;

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

}
module com.lerstudios.space_debris_simulation {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires org.fxyz3d.core;


    opens com.lerstudios.space_debris_simulation to javafx.fxml;
    exports com.lerstudios.space_debris_simulation;
}
module com.lerstudios.space_debris_simulation {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.lerstudios.space_debris_simulation to javafx.fxml;
    exports com.lerstudios.space_debris_simulation;
}
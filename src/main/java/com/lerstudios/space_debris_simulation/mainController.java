package com.lerstudios.space_debris_simulation;

import com.lerstudios.space_debris_simulation.configurationUtilities.SimulationSettings;
import com.lerstudios.space_debris_simulation.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

public class mainController {
    //@FXML
    //private Label welcomeText;

    //@FXML
    //protected void onHelloButtonClick() {
        //welcomeText.setText("Welcome to JavaFX Application!");
    //}

    private Stage stage;
    private Parent root;

    @FXML
    private GridPane projectGrid;

    public void switchToScene2(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                Objects.requireNonNull(getClass().getResource("visualization.fxml"))
        );
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }

    public void openProjectConfigurationWindow(ActionEvent event, Path projectFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                Objects.requireNonNull(getClass().getResource("configuration.fxml"))
        );
        Parent root = loader.load();

        configurationController controller = loader.getController();
        controller.initProject(projectFile);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
    }


    @FXML
    public void initialize() {
        loadExistingProjects();
    }

    public void loadExistingProjects() {
        try {
            loadProjects(Constants.appName, projectGrid, 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadProjects(String appName, GridPane grid, int columns) throws IOException {
        grid.getChildren().clear();

        grid.setHgap(16);
        grid.setVgap(16);

        Path projectsDir = Paths.get(
                System.getProperty("user.home"),
                "Documents",
                appName,
                "Projects"
        );

        Path[] projectFiles = new Path[0];

        if (Files.exists(projectsDir)) {
            try (Stream<Path> files = Files.list(projectsDir)) {
                projectFiles = files
                        .filter(Files::isDirectory)
                        .map(dir -> dir.resolve("project.json"))
                        .filter(Files::exists)
                        .toArray(Path[]::new);
            }
        }

        for (int i = 0; i < projectFiles.length; i++) {
            int col = i % columns;
            int row = i / columns;

            VBox projectBox = createProjectBox(projectFiles[i]);
            grid.add(projectBox, col, row);
            GridPane.setMargin(projectBox, new Insets(8));
        }

        int index = projectFiles.length;
        int col = index % columns;
        int row = index / columns;

        VBox createNewBox = createNewProjectBox();
        grid.add(createNewBox, col, row);
        GridPane.setMargin(createNewBox, new Insets(8));
    }

    private VBox createProjectBox(Path projectFile) {
        SimulationSettings settings = FileService.readSimulationSettingsForProjectList(projectFile);

        VBox outerBox = new VBox();
        outerBox.setPrefWidth(193);
        outerBox.setStyle("-fx-background-color: #0B0B0B;");

        VBox innerBox = new VBox();
        innerBox.setPrefHeight(185);
        innerBox.setPrefWidth(193);
        innerBox.setPadding(new Insets(16));
        innerBox.setSpacing(8);

        Label nameLabel = new Label(settings.simulationName);
        nameLabel.setFont(Font.font("Consolas", 16));
        nameLabel.setStyle("-fx-text-fill: white;");

        Label descLabel = new Label(settings.simulationDescription);
        descLabel.setFont(Font.font("Consolas", 12));
        descLabel.setStyle("-fx-text-fill: white;");
        descLabel.setWrapText(true);

        Button loadButton = new Button("Load Project");
        loadButton.setOnAction(event -> {
            try {
                openProjectConfigurationWindow(event, projectFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        innerBox.getChildren().addAll(nameLabel, descLabel, loadButton);
        outerBox.getChildren().add(innerBox);

        return outerBox;
    }

    private VBox createNewProjectBox() {
        VBox outerBox = new VBox();
        outerBox.setPrefWidth(193);
        outerBox.setStyle("-fx-background-color: #1A1A1A;");

        VBox innerBox = new VBox();
        innerBox.setPrefWidth(193);
        innerBox.setPrefHeight(185);
        innerBox.setPadding(new Insets(16));
        innerBox.setSpacing(8);

        Label titleLabel = new Label("Create New");
        titleLabel.setFont(Font.font("Consolas", 16));
        titleLabel.setStyle("-fx-text-fill: white;");
        VBox.setMargin(titleLabel, new Insets(0,0,8,0));

        Button createButton = new Button("New Project");
        createButton.setOnAction(event -> {
            try {
                FileService.createEmptyProjectFile(Constants.appName, "Project", "Project", "Newly Created Project");
                loadProjects(Constants.appName, projectGrid, 2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        innerBox.getChildren().addAll(titleLabel, createButton);
        outerBox.getChildren().add(innerBox);

        return outerBox;
    }
}
package com.lerstudios.space_debris_simulation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.lerstudios.space_debris_simulation.configurationUtilities.SimulationSettings;
import com.lerstudios.space_debris_simulation.simulation.exportFormats.VisualizationFile;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileService {
    public static Path createEmptyProjectFile(
            String appName,
            String baseFileName,
            String baseSimulationName,
            String simulationDescription
    ) throws IOException {

        Path projectsDir = Paths.get(
                System.getProperty("user.home"),
                "Documents",
                appName,
                "Projects"
        );

        Files.createDirectories(projectsDir);

        if (baseFileName.endsWith(".json")) {
            baseFileName = baseFileName.substring(0, baseFileName.length() - 5);
        }

        String folderName = baseFileName;
        Path projectDir = projectsDir.resolve(folderName);
        int counter = 1;
        while (Files.exists(projectDir)) {
            folderName = baseFileName + " " + counter;
            projectDir = projectsDir.resolve(folderName);
            counter++;
        }

        Files.createDirectories(projectDir);

        Path projectFile = projectDir.resolve("project.json");

        String simulationName = folderName;

        String json = String.format(
                "{\n  \"simulationName\": \"%s\",\n  \"simulationDescription\": \"%s\"\n}",
                escapeJson(simulationName),
                escapeJson(simulationDescription)
        );

        Files.writeString(projectFile, json);

        return projectFile;
    }

    private static String escapeJson(String input) {
        return input.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public static SimulationSettings readSimulationSettingsForProjectList(Path file) {
        SimulationSettings settings = new SimulationSettings();

        try {
            String content = Files.readString(file);

            if (content.contains("\"simulationName\"")) {
                settings.simulationName = content.split("\"simulationName\"\\s*:\\s*\"")[1].split("\"")[0];
            }
            if (content.contains("\"simulationDescription\"")) {
                settings.simulationDescription = content.split("\"simulationDescription\"\\s*:\\s*\"")[1].split("\"")[0];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return settings;
    }

    private static final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);;

    public static SimulationSettings parseProjectSettings(Path file) {
        try {
            return mapper.readValue(file.toFile(), SimulationSettings.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new SimulationSettings();
        }
    }

    public static Path saveProjectSettings(Path oldFile, SimulationSettings settings) {
        try {
            Path projectDir = oldFile.getParent();
            Path projectsDir = projectDir.getParent();

            String desiredBaseName = sanitizeFileName(settings.simulationName);
            if (desiredBaseName.isBlank()) {
                desiredBaseName = "Unnamed Project";
            }

            Path targetDir = projectDir;

            if (!desiredBaseName.equals(projectDir.getFileName().toString())) {
                targetDir = resolveFolderCollision(projectsDir, desiredBaseName, projectDir);
                Files.move(projectDir, targetDir);
            }

            Path newFile = targetDir.resolve("project.json");

            mapper.writeValue(newFile.toFile(), settings);

            return newFile;
        } catch (IOException e) {
            e.printStackTrace();
            return oldFile;
        }
    }


    private static String getBaseName(Path file) {
        String name = file.getFileName().toString();
        return name.endsWith(".json")
                ? name.substring(0, name.length() - 5)
                : name;
    }

    private static String sanitizeFileName(String input) {
        return input
                .replaceAll("[\\\\/:*?\"<>|]", "")
                .trim();
    }

    private static Path resolveFolderCollision(
            Path parent,
            String baseName,
            Path dirToIgnore
    ) {
        int counter = 0;
        Path candidate;

        do {
            String name = (counter == 0)
                    ? baseName
                    : baseName + " " + counter;

            candidate = parent.resolve(name);
            counter++;

        } while (Files.exists(candidate) && !candidate.equals(dirToIgnore));

        return candidate;
    }


    public static Path createEmptyVisualizationFile(
            String appName,
            String projectFolderName,
            VisualizationFile visualizationFileObject
    ) throws IOException {

        Path projectsDir = Paths.get(
                System.getProperty("user.home"),
                "Documents",
                appName,
                "Projects"
        );

        Files.createDirectories(projectsDir);

        Path projectDir = projectsDir.resolve(projectFolderName);
        Files.createDirectories(projectDir);

        Path visualizationsDir = projectDir.resolve("visualizations");
        Files.createDirectories(visualizationsDir);

        String timestamp = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

        String fileName = timestamp + "_visualization.json";
        Path visualizationFile = visualizationsDir.resolve(fileName);

        // Jackson handles JSON creation
        mapper.writeValue(visualizationFile.toFile(), visualizationFileObject);

        return visualizationFile;
    }

    public static VisualizationFile openVisualizationFile(
            String appName,
            String simulationName,
            javafx.stage.Window ownerWindow
    ) {

        try {
            // Resolve Projects directory
            Path projectsDir = Paths.get(
                    System.getProperty("user.home"),
                    "Documents",
                    appName,
                    "Projects"
            );

            Path visualizationDir = projectsDir
                    .resolve(simulationName)
                    .resolve("visualizations");

            if (!Files.exists(visualizationDir)) {
                throw new IOException("Visualization directory does not exist");
            }

            // File chooser
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Open Visualization");
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Visualization Files", "*.json")
            );
            chooser.setInitialDirectory(visualizationDir.toFile());

            File selectedFile = chooser.showOpenDialog(ownerWindow);
            if (selectedFile == null) {
                return null; // user cancelled
            }

            // Deserialize
            return mapper.readValue(selectedFile, VisualizationFile.class);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

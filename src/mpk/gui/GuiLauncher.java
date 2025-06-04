package mpk.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import mpk.engine.Simulation;
import mpk.io.CsvLoader;
import mpk.model.*;

import java.io.IOException;
import java.util.*;

public class GuiLauncher extends Application {

    private List<Vehicle> fleet;
    private List<List<Station>> availableRoutes;
    private List<int[]> vehicleStats;
    private VBox vehicleStatusBox;
    private GraphPane graphPane;
    private Simulation simulation;

    @Override
    public void start(Stage primaryStage) throws Exception {
        availableRoutes = CsvLoader.loadRoutes("src/mpk/input/routes.csv");
        vehicleStats = CsvLoader.loadProperties("src/mpk/input/vehicles.csv");
        showStartupDialog(primaryStage);  // Launch config window first
    }

    private void showStartupDialog(Stage primaryStage) {
        Stage configStage = new Stage();
        configStage.setTitle("Start Configuration");

        VBox configRoot = new VBox(10);
        configRoot.setPadding(new Insets(9));

        // Spinner for buses
        Label busesLabel = new Label("Number of Buses (0–10):");
        Spinner<Integer> busesSpinner = new Spinner<>(0, 10, 1);

        // Spinner for trams
        Label tramsLabel = new Label("Number of Trams (0–10):");
        Spinner<Integer> tramsSpinner = new Spinner<>(0, 10, 1);

        // Toggle simulation mode
        Label modeLabel = new Label("Simulation Mode:");
        ToggleGroup modeGroup = new ToggleGroup();
        RadioButton autoMode = new RadioButton("Automatic");
        autoMode.setToggleGroup(modeGroup);
        autoMode.setSelected(true);
        RadioButton manualMode = new RadioButton("Manual");
        manualMode.setToggleGroup(modeGroup);

        // Step control if auto mode selected
        Label stepsLabel = new Label("Number of Simulation Steps:");
        Spinner<Integer> stepsSpinner = new Spinner<>(1, 1000, 10);

        stepsLabel.setVisible(true);
        stepsSpinner.setVisible(true);

        // Show/hide steps depending on mode
        modeGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            boolean isAuto = newToggle == autoMode;
            stepsLabel.setVisible(isAuto);
            stepsSpinner.setVisible(isAuto);
        });

        CheckBox randomizeRoutesCheckbox = new CheckBox("Randomize route selection per vehicle");
        randomizeRoutesCheckbox.setSelected(true);

        // === Control Probability Slider ===
        Label probLabel = new Label("Control Probability:");
        Slider probSlider = new Slider(0.0, 1.0, 0.8);
        probSlider.setShowTickLabels(true);
        probSlider.setShowTickMarks(true);
        probSlider.setMajorTickUnit(0.2);
        probSlider.setMinorTickCount(1);
        probSlider.setBlockIncrement(0.05);
        Label probValue = new Label(String.format("%.2f", probSlider.getValue()));
        probSlider.valueProperty().addListener((obs, oldVal, newVal) ->
                probValue.setText(String.format("%.2f", newVal.doubleValue())));

        HBox probBox = new HBox(10, probLabel, probSlider, probValue);

        // === Penalty Slider ===
        Label penaltyLabel = new Label("Penalty Amount (PLN):");
        Slider penaltySlider = new Slider(0, 1000, 160);
        penaltySlider.setShowTickLabels(true);
        penaltySlider.setShowTickMarks(true);
        penaltySlider.setMajorTickUnit(200);
        penaltySlider.setMinorTickCount(3);
        penaltySlider.setBlockIncrement(10);
        Label penaltyValue = new Label(String.valueOf((int) penaltySlider.getValue()));
        penaltySlider.valueProperty().addListener((obs, oldVal, newVal) ->
                penaltyValue.setText(String.valueOf(newVal.intValue())));

        HBox penaltyBox = new HBox(10, penaltyLabel, penaltySlider, penaltyValue);

        // === Launch Simulation Button ===
        Button launchButton = new Button("Start Simulation");
        launchButton.setOnAction(e -> {
            int buses = busesSpinner.getValue();
            int trams = tramsSpinner.getValue();
            boolean manual = manualMode.isSelected();
            boolean randomizeRoutes = randomizeRoutesCheckbox.isSelected();
            int steps = autoMode.isSelected() ? stepsSpinner.getValue() : 0;
            double controllerProbability = probSlider.getValue();
            int controllerPenalty = (int) penaltySlider.getValue();

            configStage.close();
            try {
                showMainGui(primaryStage, buses, trams, manual, randomizeRoutes, steps, controllerProbability, controllerPenalty);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        configRoot.getChildren().addAll(
                busesLabel, busesSpinner,
                tramsLabel, tramsSpinner,
                modeLabel, autoMode, manualMode,
                stepsLabel, stepsSpinner,
                randomizeRoutesCheckbox,
                probBox,
                penaltyBox,
                launchButton
        );

        Scene configScene = new Scene(configRoot, 400, 450);
        configStage.setScene(configScene);
        configStage.show();
    }

    private void showMainGui(Stage primaryStage, int buses, int trams, boolean manual, boolean randomizeRoutes, int steps,
                             double controllerProbability, int controllerPenalty) throws Exception {
        primaryStage.setTitle("MPK Simulator - GUI");

        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        vehicleStatusBox = new VBox(10);
        ScrollPane vehicleScroll = new ScrollPane(vehicleStatusBox);
        vehicleScroll.setPrefHeight(150);
        vehicleScroll.setFitToWidth(true);

        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setPrefHeight(120);

        fleet = new ArrayList<>();
        Random rand = new Random();

        // Creating fleet
        for (int i = 0; i < buses + trams; i++) {
            List<Station> route = new ArrayList<>(availableRoutes.get(i % availableRoutes.size()));
            int stops = rand.nextInt(route.size() - 2) + 3;
            if (randomizeRoutes) Collections.shuffle(route);
            List<Station> selectedRoute = route.subList(0, stops);

            Vehicle v = (i < buses)
                    ? new Bus("Bus" + i, selectedRoute, vehicleStats.get(i)[0])
                    : new Tram("Tram" + (i - buses), selectedRoute, vehicleStats.get(i)[0]);
            fleet.add(v);

            ProgressBar progress = new ProgressBar(0);
            Label label = new Label(v.getName() + " - Passengers: " + v.getPassengers().size() + ", Tickets: " + v.getBoughtTickets());
            VBox box = new VBox(label, progress);
            vehicleStatusBox.getChildren().add(box);
        }

        graphPane = new GraphPane(fleet);
        simulation = new Simulation(fleet, 0.8, controllerProbability, controllerPenalty);

        HBox controlBox = new HBox(10);
        if (manual) {
            Button nextStepButton = new Button("Next Step");
            Button endButton = new Button("End");

            nextStepButton.setOnAction(e -> {
                try {
                    simulation.step();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                updateVehicleStatus();
                graphPane.updatePositions(fleet);
                outputArea.appendText("Manual step executed.\n");
            });

            endButton.setOnAction(e -> {
                outputArea.appendText("\n--- END OF SIMULATION ---\n");
                SummaryDialog.show(simulation);
                simulation.clearAll();
            });

            controlBox.getChildren().addAll(nextStepButton, endButton);
        } else {
            Button startButton = new Button("▶ Start");
            startButton.setOnAction(e -> runSimulation(steps, outputArea));
            controlBox.getChildren().add(startButton);
        }

        root.getChildren().addAll(vehicleScroll, graphPane, outputArea, controlBox);

        Scene scene = new Scene(root, 700, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void runSimulation(int steps, TextArea outputArea) {
        Thread simulationThread = new Thread(() -> {
            try {
                for (int i = 0; i < steps; i++) {
                    simulation.step();
                    final int step = i;
                    Platform.runLater(() -> {
                        updateVehicleStatus();
                        graphPane.updatePositions(fleet);
                        outputArea.appendText("Step " + (step + 1) + " completed.\n");
                    });
                    Thread.sleep(500);  // fixed delay between steps
                }

                Platform.runLater(() -> {
                    outputArea.appendText("\n--- END OF SIMULATION ---\n");
                    SummaryDialog.show(simulation);
                    simulation.clearAll();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        simulationThread.setDaemon(true);
        simulationThread.start();
    }

    private void updateVehicleStatus() {
        vehicleStatusBox.getChildren().clear();
        for (Vehicle v : fleet) {
            double progress = (double) v.getCurrentStationNumber() / Math.max(1, v.getRoute().size() - 1);
            ProgressBar progressBar = new ProgressBar(progress);

            Label label = new Label(v.getName() + " - Passengers: " + v.getPassengers().size() + ", Tickets: " + v.getBoughtTickets());
            VBox box = new VBox(label, progressBar);
            vehicleStatusBox.getChildren().add(box);
        }
    }

    public static void launchGui() {
        launch();
    }

    public static void main(String[] args) {
        launch();
    }
}

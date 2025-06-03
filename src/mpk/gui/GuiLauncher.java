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
        showStartupDialog(primaryStage);
    }

    private void showStartupDialog(Stage primaryStage) {
        Stage configStage = new Stage();
        configStage.setTitle("Start Configuration");

        VBox configRoot = new VBox(10);
        configRoot.setPadding(new Insets(15));

        Label busesLabel = new Label("Number of Buses (0–10):");
        Spinner<Integer> busesSpinner = new Spinner<>(0, 10, 1);

        Label tramsLabel = new Label("Number of Trams (0–10):");
        Spinner<Integer> tramsSpinner = new Spinner<>(0, 10, 1);

        Label modeLabel = new Label("Simulation Mode:");
        ToggleGroup modeGroup = new ToggleGroup();
        RadioButton autoMode = new RadioButton("Automatic");
        autoMode.setToggleGroup(modeGroup);
        autoMode.setSelected(true);
        RadioButton manualMode = new RadioButton("Manual");
        manualMode.setToggleGroup(modeGroup);

        CheckBox randomizeRoutesCheckbox = new CheckBox("Randomize route selection per vehicle");
        randomizeRoutesCheckbox.setSelected(true);

        Button launchButton = new Button("Start Simulation");
        launchButton.setOnAction(e -> {
            int buses = busesSpinner.getValue();
            int trams = tramsSpinner.getValue();
            boolean manual = manualMode.isSelected();
            boolean randomizeRoutes = randomizeRoutesCheckbox.isSelected();
            configStage.close();
            try {
                showMainGui(primaryStage, buses, trams, manual, randomizeRoutes);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        configRoot.getChildren().addAll(
                busesLabel, busesSpinner,
                tramsLabel, tramsSpinner,
                modeLabel, autoMode, manualMode,
                randomizeRoutesCheckbox,
                launchButton
        );

        Scene configScene = new Scene(configRoot, 300, 350);
        configStage.setScene(configScene);
        configStage.show();
    }

    private void showMainGui(Stage primaryStage, int buses, int trams, boolean manual, boolean randomizeRoutes) throws Exception {
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

        simulation = new Simulation(fleet, 0.8);

        Button startButton = new Button("▶ Start");
        startButton.setOnAction(e -> runSimulation(manual, outputArea));

        root.getChildren().addAll(vehicleScroll, graphPane, outputArea, startButton);

        Scene scene = new Scene(root, 700, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void runSimulation(boolean manual, TextArea outputArea) {
        Thread simulationThread = new Thread(() -> {
            try {
                int steps = 10;
                for (int i = 0; i < steps; i++) {
                    simulation.step();
                    final int step = i;
                    Platform.runLater(() -> {
                        updateVehicleStatus();
                        graphPane.updatePositions(fleet);
                        outputArea.appendText("Krok " + (step + 1) + " zakończony.\n");
                    });
                    Thread.sleep(manual ? 1000 : 500);
                }

                Platform.runLater(() -> {
                    outputArea.appendText("\n--- KONIEC SYMULACJI ---\n");
                    SummaryDialog.show(simulation);
                });
                simulation.clearAll();
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

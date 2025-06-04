package mpk.gui;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.beans.property.SimpleStringProperty;
import mpk.engine.Simulation;
import mpk.io.CsvSaver;
import mpk.model.Vehicle;

import java.io.IOException;
import java.util.*;

public class SummaryDialog {

    public static void show(Simulation sim) {
        if (sim == null) return;

        Stage dialog = new Stage();
        dialog.setTitle("Simulation Summary");  // was: "Podsumowanie symulacji"
        dialog.initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20; -fx-background-color: white;");

        // General overview section
        Label totalLabel = new Label("Global Summary:");
        totalLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        Label earnings = new Label("Earnings: " + sim.getTotalEarnings() + " PLN");
        Label tickets = new Label("Tickets Sold: " + sim.getTotalBoughtTickets());
        Label fines = new Label("Fines Issued: " + sim.getTotalCaptures());

        root.getChildren().addAll(totalLabel, earnings, tickets, fines, new Separator());

        // === Ticket control section, kind of interesting analytics ===
        Label caughtLabel = new Label("Ticket Inspections – Caught Without Ticket per Station (%):");
        caughtLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        root.getChildren().add(caughtLabel);

        TableView<CatchRow> caughtTable = new TableView<>();

        // Vehicle column
        TableColumn<CatchRow, String> vCol = new TableColumn<>("Vehicle");
        vCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().vehicle));

        // Station column
        TableColumn<CatchRow, String> sCol = new TableColumn<>("Station");
        sCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().station));

        // Number of fare dodgers caught
        TableColumn<CatchRow, String> nCol = new TableColumn<>("Caught");
        nCol.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().count)));

        // Share percentage column
        TableColumn<CatchRow, String> pCol = new TableColumn<>("Share (%)");
        int totalCaught = sim.getTotalCaptures();
        pCol.setCellValueFactory(cell -> {
            int count = cell.getValue().count;
            String percent = totalCaught > 0 ? String.format("%.1f", (count * 100.0) / totalCaught) : "0.0";
            return new SimpleStringProperty(percent + " %");
        });

        caughtTable.getColumns().addAll(vCol, sCol, nCol, pCol);
        caughtTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Prepare table data from simulation
        List<CatchRow> catches = new ArrayList<>();
        Map<String, Map<String, Integer>> controlMap = sim.getControlResults();
        if (controlMap != null) {
            for (Map.Entry<String, Map<String, Integer>> vehicleEntry : controlMap.entrySet()) {
                String vehicleName = vehicleEntry.getKey();
                for (Map.Entry<String, Integer> stationEntry : vehicleEntry.getValue().entrySet()) {
                    catches.add(new CatchRow(vehicleName, stationEntry.getKey(), stationEntry.getValue()));
                }
            }
        }

        caughtTable.setItems(FXCollections.observableArrayList(catches));
        caughtTable.setPrefHeight(200);
        root.getChildren().add(caughtTable);

        // Close app button — shuts down JVM
        Button close = new Button("Exit Program");
        close.setOnAction(e -> System.exit(0));
        root.getChildren().add(close);

        // Save CSV with results — might fail if file is locked
        Button save = new Button("Save Summary to CSV");
        save.setOnAction(e -> {
            sim.getBoughtTicketsMap().clear();
            List<Vehicle> vehicles = sim.getVehicles();

            // Loading bought ticket map
            Map<String, Integer> ticketsMap = new TreeMap<>();
            for (Vehicle v : vehicles) {
                ticketsMap.put(v.getName(), v.getBoughtTickets());
            }

            try {
                CsvSaver.saveControlResults(
                        sim.getControlResults(),
                        ticketsMap,
                        sim.getTotalCaptures(),
                        sim.getTotalEarnings(),
                        sim.getTotalBoughtTickets()
                );
                showAlert(Alert.AlertType.INFORMATION, "CSV file saved to: src/mpk/output");
            } catch (IOException ex) {
                showAlert(Alert.AlertType.ERROR, "Error while saving CSV: " + ex.getMessage());
            }
        });

        root.getChildren().add(save);

        Scene scene = new Scene(root);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    // Table row model
    public static class CatchRow {
        public final String vehicle;
        public final String station;
        public final int count;

        public CatchRow(String vehicle, String station, int count) {
            this.vehicle = vehicle;
            this.station = station;
            this.count = count;
        }
    }

    // Simple helper to show popups
    private static void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle("CSV Export");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}

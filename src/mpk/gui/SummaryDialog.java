package mpk.gui;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.beans.property.SimpleStringProperty;
import mpk.engine.Simulation;
import mpk.io.CsvSaver;

import java.io.IOException;
import java.util.*;

public class SummaryDialog {

    public static void show(Simulation sim) {
        if (sim == null) return;

        Stage dialog = new Stage();
        dialog.setTitle("Podsumowanie symulacji");
        dialog.initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20; -fx-background-color: white;");

        Label totalLabel = new Label("Podsumowanie globalne:");
        totalLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        Label earnings = new Label("Zarobki: " + sim.getTotalEarnings() + " PLN");
        Label tickets = new Label("Sprzedane bilety: " + sim.getTotalBoughtTickets());
        Label fines = new Label("Mandaty: " + sim.getTotalCaptures());

        root.getChildren().addAll(totalLabel, earnings, tickets, fines, new Separator());

        // ===== Złapani bez biletu per przystanek + % =====
        Label caughtLabel = new Label("Kontrole biletów – pasażerowie bez biletu per przystanek (%):");
        caughtLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        root.getChildren().add(caughtLabel);

        TableView<CatchRow> caughtTable = new TableView<>();

        TableColumn<CatchRow, String> vCol = new TableColumn<>("Pojazd");
        vCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().vehicle));

        TableColumn<CatchRow, String> sCol = new TableColumn<>("Przystanek");
        sCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().station));

        TableColumn<CatchRow, String> nCol = new TableColumn<>("Złapani");
        nCol.setCellValueFactory(cell -> new SimpleStringProperty(String.valueOf(cell.getValue().count)));

        TableColumn<CatchRow, String> pCol = new TableColumn<>("Udział (%)");
        int totalCaught = sim.getTotalCaptures();
        pCol.setCellValueFactory(cell -> {
            int count = cell.getValue().count;
            String percent = totalCaught > 0 ? String.format("%.1f", (count * 100.0) / totalCaught) : "0.0";
            return new SimpleStringProperty(percent + " %");
        });

        caughtTable.getColumns().addAll(vCol, sCol, nCol, pCol);
        caughtTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

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

        Button close = new Button("Zamknij program");
        close.setOnAction(e -> System.exit(0));
        root.getChildren().add(close);

        Button save = new Button("Zapisz podsumowanie do CSV");
        save.setOnAction(e -> {
            try {
                CsvSaver.saveControlResults(
                        sim.getControlResults(),
                        sim.getBoughtTicketsMap(),
                        sim.getTotalCaptures(),
                        sim.getTotalEarnings(),
                        sim.getTotalBoughtTickets()
                );
                showAlert(Alert.AlertType.INFORMATION, "Zapisano plik CSV w folderze: src/mpk/output");
            } catch (IOException ex) {
                showAlert(Alert.AlertType.ERROR, "Błąd podczas zapisu pliku: " + ex.getMessage());
            }
        });

        root.getChildren().add(save);


        Scene scene = new Scene(root);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

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

    private static void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle("Zapis CSV");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

}

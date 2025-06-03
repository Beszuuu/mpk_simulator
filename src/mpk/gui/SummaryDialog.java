// === SummaryDialog.java ===
package mpk.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.application.Platform;
import mpk.engine.Simulation;

import java.util.*;

public class SummaryDialog {

    public static void show(Simulation sim) {
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

        root.getChildren().addAll(totalLabel, earnings, tickets, fines);

        Separator separator = new Separator();
        root.getChildren().add(separator);

        Label perVehicleLabel = new Label("Podsumowanie per pojazd:");
        perVehicleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        root.getChildren().add(perVehicleLabel);

        TableView<Map.Entry<String, Integer>> ticketTable = new TableView<>();
        TableColumn<Map.Entry<String, Integer>, String> vehicleCol = new TableColumn<>("Pojazd");
        vehicleCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getKey()));

        TableColumn<Map.Entry<String, Integer>, String> countCol = new TableColumn<>("Bilety");
        countCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cell.getValue().getValue())));

        ticketTable.getColumns().addAll(vehicleCol, countCol);
        ticketTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        ticketTable.setItems(FXCollections.observableArrayList(sim.getBoughtTicketsMap().entrySet()));
        ticketTable.setPrefHeight(150);

        root.getChildren().add(ticketTable);

        Separator separator2 = new Separator();
        root.getChildren().add(separator2);

        Label caughtLabel = new Label("Kontrole biletów – pasażerowie bez biletu per przystanek:");
        caughtLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");
        root.getChildren().add(caughtLabel);

        TableView<CatchRow> caughtTable = new TableView<>();

        TableColumn<CatchRow, String> vCol = new TableColumn<>("Pojazd");
        vCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().vehicle));

        TableColumn<CatchRow, String> sCol = new TableColumn<>("Przystanek");
        sCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().station));

        TableColumn<CatchRow, String> nCol = new TableColumn<>("Złapani bez biletu");
        nCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cell.getValue().count)));

        caughtTable.getColumns().addAll(vCol, sCol, nCol);
        caughtTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        List<CatchRow> catches = new ArrayList<>();
        for (Map.Entry<String, Map<String, Integer>> vehicleEntry : sim.getControlResults().entrySet()) {
            String vehicleName = vehicleEntry.getKey();
            for (Map.Entry<String, Integer> stationEntry : vehicleEntry.getValue().entrySet()) {
                catches.add(new CatchRow(vehicleName, stationEntry.getKey(), stationEntry.getValue()));
            }
        }

        caughtTable.setItems(FXCollections.observableArrayList(catches));
        caughtTable.setPrefHeight(200);
        root.getChildren().add(caughtTable);

        Button close = new Button("Zamknij program");
        close.setOnAction(e -> {
            //dialog.close();
            //Platform.exit();
            System.exit(0);
        });

        root.getChildren().add(close);

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
}
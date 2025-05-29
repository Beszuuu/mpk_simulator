package mpk.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import mpk.engine.Simulation;
import mpk.io.CsvLoader;
import mpk.model.*;

import java.util.*;

public class MainViewController {

    @FXML
    public void onAutoMode() {
        runSimulation("a");
    }

    @FXML
    public void onManualMode() {
        runSimulation("m");
    }

    private void runSimulation(String mode) {
        try {
            List<List<Station>> routes = CsvLoader.loadRoutes("src/mpk/input/routes.csv");
            List<int[]> vehicleStats = CsvLoader.loadProperties("src/mpk/input/vehicles.csv");

            int busesCount = 1;  // możesz to rozbudować do inputów w GUI
            int tramsCount = 1;

            List<Vehicle> fleet = new ArrayList<>();
            for (int i = 0; i < busesCount + tramsCount; i++) {
                List<Station> route = routes.get(i);
                List<Station> shortRoute = route.subList(0, Math.min(5, route.size()));
                Vehicle v = (i < busesCount)
                        ? new Bus("Bus" + i, shortRoute, vehicleStats.get(i)[0])
                        : new Tram("Tram" + i, shortRoute, vehicleStats.get(i)[0]);
                fleet.add(v);
            }

            Simulation sim = new Simulation(fleet, 0.8);
            if (mode.equalsIgnoreCase("a")) {
                for (int i = 0; i < 5; i++) {
                    sim.step();
                }
            } else {
                for (int i = 0; i < 5; i++) {
                    sim.step(); // można to zintegrować z przyciskiem "Dalej"
                }
            }

            sim.summary();

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Symulacja zakończona");
            alert.setHeaderText("Sukces");
            alert.setContentText("Symulacja zakończona pomyślnie. Wyniki zapisane.");
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText("Błąd podczas uruchamiania symulacji");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}

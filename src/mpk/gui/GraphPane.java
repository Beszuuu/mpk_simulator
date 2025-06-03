package mpk.gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import mpk.model.*;

import java.util.*;

public class GraphPane extends Pane {

    private final Canvas canvas;
    private final Map<String, double[]> stationCoords = new HashMap<>();
    private final Map<Vehicle, Integer> vehiclePositions = new HashMap<>();

    public GraphPane(List<Vehicle> vehicles) {
        this.canvas = new Canvas(600, 400);
        getChildren().add(canvas);

        for (Vehicle v : vehicles) {
            for (Station s : v.getRoute()) {
                stationCoords.putIfAbsent(s.getName(), new double[]{
                        50 + Math.random() * 500,
                        50 + Math.random() * 300
                });
            }
            vehiclePositions.put(v, 0);
        }

        drawGraph(vehicles);
    }

    public void updatePositions(List<Vehicle> vehicles) {
        for (Vehicle v : vehicles) {
            vehiclePositions.put(v, v.getCurrentStationNumber());
        }
        drawGraph(vehicles);
    }

    private void drawGraph(List<Vehicle> vehicles) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw connections
        gc.setStroke(Color.LIGHTGRAY);
        for (Vehicle v : vehicles) {
            List<Station> route = v.getRoute();
            for (int i = 0; i < route.size() - 1; i++) {
                double[] a = stationCoords.get(route.get(i).getName());
                double[] b = stationCoords.get(route.get(i + 1).getName());
                gc.strokeLine(a[0], a[1], b[0], b[1]);
            }
        }

        // Draw stations
        for (Map.Entry<String, double[]> entry : stationCoords.entrySet()) {
            double[] coords = entry.getValue();
            gc.setFill(Color.BLACK);
            gc.fillOval(coords[0] - 4, coords[1] - 4, 8, 8);
            gc.setFont(new Font(10));
            gc.fillText(entry.getKey(), coords[0] + 5, coords[1] - 5);
        }

        // Draw vehicles
        for (Vehicle v : vehicles) {
            int pos = vehiclePositions.getOrDefault(v, 0);
            if (pos >= v.getRoute().size()) continue;

            Station s = v.getRoute().get(pos);
            double[] coords = stationCoords.get(s.getName());
            gc.setFill(Color.BLUE);
            gc.fillOval(coords[0] - 6, coords[1] - 6, 12, 12);
            gc.setFill(Color.WHITE);
            gc.fillText(v.getName(), coords[0] - 15, coords[1] - 10);
        }
    }
}

package mpk.io;

import mpk.model.Station;
import java.io.*;
import java.util.*;

public class CsvLoader {
    public static List<List<Station>> loadRoutes(String filePath) throws IOException {
        List<List<Station>> routes = new ArrayList<>();
        BufferedReader csv = new BufferedReader(new FileReader(filePath));

        String line;
        while ((line = csv.readLine()) != null) {
            String[] parts = line.split(",");
            List<Station> route = new ArrayList<>();
            for (int i = 1; i < parts.length; i += 2) {
                String name = parts[i];
                double pop = Double.parseDouble(parts[i + 1]);
                route.add(new Station(name, pop));
            }
            routes.add(route);
        }
        return routes;
    }

    public static List<List<Station>> loadProperties(String filePath) throws IOException {
        List<List<Station>> properties = new ArrayList<>();
        BufferedReader csv = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = csv.readLine()) != null) {
            String[] parts = line.split(",");
            List<Station> property = new ArrayList<>();

            int seatingCapacity = Integer.parseInt(parts[1]);
            int standingCapacity = Integer.parseInt(parts[2]);
            System.out.println("Pojazd: " + parts[0] + ", Miejsca siedzące: " + seatingCapacity + ", Miejsca stojące: " + standingCapacity);

            properties.add(property);
        }
        return properties;
    }
}
package mpk.io;

import mpk.model.Station;
import mpk.model.Vehicle;

import java.io.*;
import java.util.*;

public class CsvLoader {

    // Reads a CSV file and builds route lists with Station objects.
    // Each line is expected to contain alternating station names and popularity scores.
    public static List<List<Station>> loadRoutes(String filePath) throws IOException {
        List<List<Station>> routes = new ArrayList<>();
        BufferedReader csv = new BufferedReader(new FileReader(filePath));

        String line;
        while ((line = csv.readLine()) != null) {
            String[] parts = line.split(",");
            List<Station> route = new ArrayList<>();

            // Note: We skip the first entry (assuming it's an ID)
            for (int i = 1; i < parts.length; i += 2) {
                String name = parts[i];
                double pop = Double.parseDouble(parts[i + 1]);
                route.add(new Station(name, pop));
            }

            routes.add(route);
        }

        return routes;
    }

    // Loads vehicle capacity properties from a CSV.
    // Each row should contain: VehicleName,Seating,Standing
    public static List<int[]> loadProperties(String filePath) throws IOException {
        List<int[]> properties = new ArrayList<>();
        BufferedReader csv = new BufferedReader(new FileReader(filePath));

        String line;
        while ((line = csv.readLine()) != null) {
            String[] parts = line.split(",");

            String name = parts[0];  // Currently unused, could be useful for mapping later
            int seatingCapacity = Integer.parseInt(parts[1]);
            int standingCapacity = Integer.parseInt(parts[2]);

            // Add the pair of capacities as a 2-element int array
            properties.add(new int[]{seatingCapacity, standingCapacity});
        }

        csv.close();
        return properties;
    }

    // Loads vehicle names from a CSV file (only the first column is used)
    public static List<String> loadNames(String filePath) throws IOException {
        List<String> names = new ArrayList<>();
        BufferedReader csv = new BufferedReader(new FileReader(filePath));

        String line;
        while ((line = csv.readLine()) != null) {
            String[] parts = line.split(",");

            String name = parts[0];

            names.add(name);
        }

        csv.close();
        return names;
    }
}

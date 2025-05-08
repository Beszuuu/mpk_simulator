package mpk.io;

import mpk.model.Station;
import mpk.model.Vehicle;

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

    public static List<int[]> loadProperties(String filePath) throws IOException {
        List<int[]> properties = new ArrayList<>();
        BufferedReader csv = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = csv.readLine()) != null) {
            String[] parts = line.split(",");
            // Zakładamy, że każdy wiersz ma format: NazwaPojazdu,MiejscaSiedzące,MiejscaStojące
            String name = parts[0]; // Nazwa pojazdu może kiedyś do użycia ???
            int seatingCapacity = Integer.parseInt(parts[1]); // Miejsca siedzące
            int standingCapacity = Integer.parseInt(parts[2]); // Miejsca stojące

            // Dodajemy pojemności do listy
            properties.add(new int[]{seatingCapacity, standingCapacity});
            //System.out.println("Pojazd: " + name + ", Miejsca siedzące: " + seatingCapacity + ", Miejsca stojące: " + standingCapacity);
        }
        csv.close();
        return properties; // Zwracamy listę pojemności
    }

    public static List<String> loadNames(String filePath) throws IOException {
        List<String> names = new ArrayList<>();
        BufferedReader csv = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = csv.readLine()) != null) {
            String[] parts = line.split(",");

            String name = parts[0]; // Nazwa pojazdu może kiedyś do użycia ???

            // Dodajemy pojemności do listy
            names.add(name);
            //System.out.println("Pojazd: " + name + ", Miejsca siedzące: " + seatingCapacity + ", Miejsca stojące: " + standingCapacity);
        }
        csv.close();
        return names; // Zwracamy listę pojemności
    }

}
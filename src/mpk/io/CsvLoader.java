package mpk.io;

import mpk.model.Stop;
import java.io.*;
import java.util.*;

public class CsvLoader {
    public static List<List<Stop>> loadRoutes(String filePath) throws IOException {
        List<List<Stop>> routes = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filePath));

        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            List<Stop> route = new ArrayList<>();
            for (int i = 1; i < parts.length; i += 2) {
                String name = parts[i];
                double pop = Double.parseDouble(parts[i + 1]);
                route.add(new Stop(name, pop));
            }
            routes.add(route);
        }
        return routes;
    }
}
package mpk;

import mpk.io.CsvLoader;
import mpk.model.*;
import mpk.engine.Simulation;
import java.util.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        List<List<Stop>> routes = CsvLoader.loadRoutes("src\\mpk\\utils\\routes.csv");

        List<Vehicle> vehicles = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            vehicles.add(new Bus("Bus" + i, routes.get(i)));
            vehicles.add(new Tram("Tram" + i, routes.get(i + 3)));
        }

        Simulation sim = new Simulation(vehicles, 0.8);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Naciśnij SPACJĘ aby kontynuować, lub Q aby zakończyć.");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("q")) break;
            sim.step();
        }

        sim.summary();
    }
}

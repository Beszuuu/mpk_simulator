package mpk.engine;

import mpk.io.CsvLoader;
import mpk.io.CsvSaver;
import mpk.model.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
//import java.util.*;

//definicja klasy
public class Simulation {
    private List<Vehicle> vehicles;
    private Random rand = new Random();
    private double ticketProbability;
    private double controllerChance = 0.1;
    private int earnings = 0;
    private Map<String, Map<String, Integer>> controlResults = new HashMap<>();

    public Simulation(List<Vehicle> vehicles, double ticketProbability) {
        this.vehicles = vehicles;
        this.ticketProbability = ticketProbability;
    }

    public void step() throws IOException {
        List<String> names = CsvLoader.loadNames("src\\mpk\\input\\names.csv");
        for (Vehicle v : vehicles) {
            v.unloadPassengers();
            Station current = v.getCurrentStation();

            // Generowanie nowych pasażerów
            if (rand.nextDouble() < current.getPopularity()) {
                int numPassengers = rand.nextInt(v.capacity - v.passengers.size());
                for (int i = 0; i < numPassengers; i++) {
                    String name = names.get(rand.nextInt(names.size()));
                    String dest = v.route.get(rand.nextInt(v.route.size())).getName();
                    //System.out.println(dest);
                    boolean hasTicket = rand.nextDouble() < ticketProbability;
                    v.boardPassenger(new Passenger(name, dest, hasTicket));
                }
            }

            // Kontrola biletów
            if (rand.nextDouble() < controllerChance) {
                Controller c = new Controller();
                int caught = c.checkPassengers(v.getPassengers());
                earnings += caught * 160;
                System.out.println("\n\nKontroler w " + v.getName() + ": bez biletu: " + caught);

                // Dopisywanie wyników do HashMap
                String vehicleName = v.getName();
                String stationName = v.getCurrentStation().getName();

                controlResults.putIfAbsent(vehicleName, new HashMap<>());
                Map<String, Integer> vehicleMap = controlResults.get(vehicleName);
                vehicleMap.put(stationName, vehicleMap.getOrDefault(stationName, 0) + caught);
            }
            System.out.println();
            System.out.print(v.getName() + " " + v.getCurrentStation().toString() + " " + " " + v.getCurrentStationNumber());
            for (Passenger p : v.getPassengers()) {
                System.out.print(" | " + p.getName() + " → " + p.getDestination());
            }
            v.nextStation();
        }
    }

    public void summary() {
        System.out.println("\n\nKoniec symulacji. Dochód: " + earnings + " PLN");

        try {
            CsvSaver.saveControlResults(controlResults);
        } catch (IOException e) {
            System.err.println("Błąd przy zapisie wyników do pliku CSV: " + e.getMessage());
        }
    }
}
package mpk.engine;

import mpk.io.CsvLoader;
import mpk.io.CsvSaver;
import mpk.model.*;

import java.io.IOException;
import java.util.*;

public class Simulation {
    private List<Vehicle> vehicles;
    private Random rand = new Random();
    private double ticketProbability;
    private double controllerChance = 0.1; // Chance a controller shows up on a vehicle
    private int earnings = 0;
    private int totalCaptures = 0;
    // Keeps track of how many passengers without tickets were caught per vehicle and station
    private Map<String, Map<String, Integer>> controlResults = new HashMap<>();

    // Constructor takes a list of vehicles and probability that passengers have tickets
    public Simulation(List<Vehicle> vehicles, double ticketProbability) {
        this.vehicles = vehicles;
        this.ticketProbability = ticketProbability;
    }

    // Run one simulation step: unload passengers, generate new passengers, run ticket control, and move to next station
    public void step() throws IOException {
        // Load random names for passengers
        List<String> names = CsvLoader.loadNames("src\\mpk\\input\\names.csv");

        for (Vehicle v : vehicles) {
            v.unloadPassengers(); // Passengers whose destination is current station get off

            genPassengers(v, names); // Generate new passengers boarding this vehicle

            controlEvent(v); // Maybe a ticket controller appears and checks passengers

            // Display vehicle info and its passengers
            System.out.println();
            System.out.print(v.getName() + " at " + v.getCurrentStation() + " (Stop " + v.getCurrentStationNumber() + ")");
            for (Passenger p : v.getPassengers()) {
                System.out.print(" | " + p.getName() + " → " + p.getDestination());
            }

            v.nextStation(); // Move vehicle to the next stop
        }
    }

    // Generate passengers at the current station who want to board the vehicle
    public void genPassengers(Vehicle v, List<String> names) {
        Station current = v.getCurrentStation();
        // Check if the station is popular enough to generate passengers this step
        if (rand.nextDouble() < current.getPopularity()) {
            int availableSeats = v.capacity - v.passengers.size();
            if (availableSeats <= 0) return; // No free seats left

            // Decide how many passengers to add, at most the free seats available
            int numPassengers = rand.nextInt(availableSeats) + 1; // +1 so at least 1 if available
            for (int i = 0; i < numPassengers; i++) {
                String name = names.get(rand.nextInt(names.size()));
                // Pick a random destination on the vehicle's route (not including last stop)
                String dest = v.route.get(rand.nextInt(v.route.size() - 1)).getName();
                boolean hasTicket = rand.nextDouble() < ticketProbability; // Decide if passenger has ticket
                v.boardPassenger(new Passenger(name, dest, hasTicket));
            }
        }
    }

    // Maybe a controller appears and checks passengers for tickets
    public void controlEvent(Vehicle v) {
        if (rand.nextDouble() < controllerChance) {
            Controller c = new Controller();
            int caught = c.checkPassengers(v.getPassengers()); // How many without tickets got caught
            earnings += caught * 160; // Each caught passenger pays a fine of 160 PLN
            totalCaptures += caught;
            System.out.println("\n\n-> Controller on " + v.getName() + ": caught without ticket: " + caught);

            // Save control results for this vehicle and station
            String vehicleName = v.getName();
            String stationName = v.getCurrentStation().getName();

            controlResults.putIfAbsent(vehicleName, new HashMap<>());
            Map<String, Integer> vehicleMap = controlResults.get(vehicleName);
            vehicleMap.put(stationName, vehicleMap.getOrDefault(stationName, 0) + caught);
        }
    }

    // After simulation ends, print earnings and save control results to CSV
    public void summary() {
        System.out.println("\n\n-> Simulation finished. Total earnings: " + earnings + " PLN");

        try {
            CsvSaver.saveControlResults(controlResults, totalCaptures, earnings);
        } catch (IOException e) {
            System.err.println("-> Error saving control results to CSV: " + e.getMessage());
        }
    }

    // Clear all vehicles and control results to prepare for a new simulation run
    public void clearAll() {
        System.out.println("\n\n-> Clearing simulation data");
        vehicles.clear();
        controlResults.clear();
    }
}

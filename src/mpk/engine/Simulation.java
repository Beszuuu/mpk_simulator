package mpk.engine;

import mpk.io.CsvLoader;
import mpk.io.CsvSaver;
import mpk.model.*;

import java.io.IOException;
import java.util.*;

public class Simulation {
    // COMPOSITION Owns Vehicles
    private final List<Vehicle> vehicles;
    private final Random rand = new Random();
    // ENCAPSULATION - private field
    private final double ticketProbability;
    private double controllerProbability = 0.1; // Chance a controller shows up on a vehicle
    private int controllerPenalty = 160;
    private int earnings = 0;
    private int totalCaptures = 0;
    private int totalBoughtTickets = 0;
    // Stores how many tickets were bought per vehicle
    private Map<String, Integer> boughtTicketsMap = new HashMap<>();
    // Keeps track of how many passengers without tickets were caught per vehicle and station
    private Map<String, Map<String, Integer>> controlResults = new HashMap<>();

    // Constructor takes a list of vehicles and probability that passengers have tickets
    public Simulation(List<Vehicle> vehicles, double ticketProbability, double controllerProbability, int controllerPenalty) {
        this.vehicles = vehicles;
        this.ticketProbability = ticketProbability;
        this.controllerProbability = controllerProbability; // Chance a controller shows up on a vehicle
        this.controllerPenalty = controllerPenalty;
    }



    public Map<String, Integer> getBoughtTicketsMap() {
        return boughtTicketsMap;
    }

    public int getTotalCaptures() {
        return totalCaptures;
    }

    public int getTotalBoughtTickets() {
        return totalBoughtTickets;
    }

    public int getTotalEarnings() {
        earnings += totalBoughtTickets * 2;
        return earnings;
    }

    public Map<String, Map<String, Integer>> getControlResults() {
        return controlResults;
    }




    // Run one simulation step: unload passengers, generate new passengers, run ticket control, and move to next station
    public void step() throws IOException {
        // Load random names for passengers
        List<String> names = CsvLoader.loadNames("src\\mpk\\input\\names.csv");
        List<String> surnames = CsvLoader.loadNames("src\\mpk\\input\\surnames.csv");

        for (Vehicle v : vehicles) {
            if (v.isActive()) { // Only move active vehicles
                v.unloadPassengers(); // Passengers whose destination is current station get off

                genPassengers(v, names, surnames); // Generate new passengers boarding this vehicle

                controlEvent(v); // Maybe a ticket controller appears and checks passengers

                // Display vehicle info and its passengers
                System.out.println();
                System.out.print(v.getName() + " at " + v.getCurrentStation() + " (Stop " + v.getCurrentStationNumber() + ")");
                for (Passenger p : v.getPassengers()) {
                    System.out.print(" | " + p.getName() + " → " + p.getDestination());
                }

                v.nextStation(); // Move vehicle to the next stop
            } else {
                v.unloadPassengers(); // Passengers whose destination is current station get off
                System.out.println();
                System.out.print(v.getName() + " at DEPOT (Stop end)");
                /*
                for (Passenger p : v.getPassengers()) {
                    System.out.print(" | " + p.getName() + " → " + p.getDestination());
                }*/
            }
        }
    }

    // Generate passengers at the current station who want to board the vehicle
    public void genPassengers(Vehicle v, List<String> names, List<String> surnames) {
        Station current = v.getCurrentStation();
        if (rand.nextDouble() < current.getPopularity()) {
            int availableSeats = v.capacity - v.passengers.size();
            if (availableSeats <= 0) return;

            int numPassengers = rand.nextInt(availableSeats) + 1;

            for (int i = 0; i < numPassengers; i++) {
                String name = names.get(rand.nextInt(names.size()));
                String surname = surnames.get(rand.nextInt(surnames.size()));
                String fullName = name + " " + surname;

                // Wybierz cel: tylko stacje dalej na trasie
                int currentIndex = v.getCurrentStationNumber();
                List<Station> futureStops = v.route.subList(currentIndex + 1, v.route.size());

                if (futureStops.isEmpty()) break; // Nie ma już dokąd jechać

                String dest = futureStops.get(rand.nextInt(futureStops.size())).getName();

                boolean hasTicket = rand.nextDouble() < ticketProbability;
                v.boardPassenger(new Passenger(fullName, dest, hasTicket));
                if(hasTicket){
                    v.incrementBoughtTickets();
                    totalBoughtTickets++;
                }
            }
        }
    }


    // Maybe a controller appears and checks passengers for tickets
    public void controlEvent(Vehicle v) {
        if (rand.nextDouble() < controllerProbability) {
            Controller c = new Controller();
            int caught = c.checkPassengers(v.getPassengers()); // How many without tickets got caught
            earnings += caught * controllerPenalty; // Each caught passenger pays a penalty
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

        System.out.println("\n\n-> Simulation finished. Total earnings: " + getTotalEarnings() + " PLN");

        // Build ticket count summary per vehicle
        for (Vehicle v : vehicles) {
            boughtTicketsMap.put(v.getName(), v.getBoughtTickets());
        }

        try {
            CsvSaver.saveControlResults(controlResults, boughtTicketsMap, totalCaptures, earnings, totalBoughtTickets);
        } catch (IOException e) {
            System.err.println("-> Error saving control results to CSV: " + e.getMessage());
        }
    }

    // Clear all vehicles and control results to prepare for a new simulation run
    public void clearAll() {
        System.out.println("\n\n-> Clearing simulation data");
        vehicles.clear();
        controlResults.clear();
        boughtTicketsMap.clear();
    }

    public List<Vehicle> getVehicles() {
        return this.vehicles;
    }
}

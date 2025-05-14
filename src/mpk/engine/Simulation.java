package mpk.engine;

import mpk.io.CsvLoader;
import mpk.model.*;

import java.io.IOException;
import java.util.*;

//definicja klasy
public class Simulation {
    private List<Vehicle> vehicles;
    private Random rand = new Random();
    private double ticketProbability;
    private double controllerChance = 0.1;
    private int earnings = 0;

    public Simulation(List<Vehicle> vehicles, double ticketProbability) {
        this.vehicles = vehicles;
        this.ticketProbability = ticketProbability;
    }

    public void step() throws IOException {
        List<String> names = CsvLoader.loadNames("src\\mpk\\utils\\names.csv");
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
            }
            System.out.println();
            System.out.print(v.getName() + " " + v.getCurrentStation().toString() + " " + " " + v.getCurrentStationNumber());
            for (Passenger p : v.getPassengers()) {
                System.out.print(" | " + p.getName() + " → " + p.getDestination());
            }

        }
    }

    public void summary() {
        System.out.println("\n\nKoniec symulacji. Dochód: " + earnings + " PLN");
    }
}
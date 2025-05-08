package mpk.engine;

import mpk.model.*;
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

    public void step() {
        for (Vehicle v : vehicles) {
            v.unloadPassengers();
            Stop current = v.getCurrentStop();

            // Generowanie nowych pasażerów
            if (rand.nextDouble() < current.getPopularity()) {
                int numPassengers = rand.nextInt(v.capacity - v.passengers.size());
                for (int i = 0; i < numPassengers; i++) {
                    String dest = v.route.get(rand.nextInt(v.route.size())).getName();
                    boolean hasTicket = rand.nextDouble() < ticketProbability;
                    v.boardPassenger(new Passenger(dest, hasTicket));
                }
            }

            // Kontrola biletów
            if (rand.nextDouble() < controllerChance) {
                Controller c = new Controller();
                int caught = c.checkPassengers(v.getPassengers());
                earnings += caught * 160;
                System.out.println("Kontroler w " + v.getId() + ": bez biletu: " + caught);
            }

            v.nextStop();
        }
    }

    public void summary() {
        System.out.println("Koniec symulacji. Dochód: " + earnings + " zł");
    }
}
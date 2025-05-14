package mpk.model;

import java.util.ArrayList;
import java.util.List;

public class Vehicle {

    // --- Pola ---
    protected String name;
    protected int currentStationNumber = 0;
    public int capacity;
    public List<Station> route;
    public List<Passenger> passengers = new ArrayList<>();

    // --- Konstruktor ---
    public Vehicle(String name, List<Station> route, int capacity) {
        this.name = name;
        this.route = route;
        this.capacity = capacity;
    }

    // --- Gettery ---
    public String getName() {
        return name;
    }
    public List<Station> getRoute() {
        return route;
    }
    public Integer getCapacity() {
        return capacity;
    }
    public int getCurrentStationNumber() {
        return currentStationNumber;
    }
    public Station getCurrentStation() { return route.get(currentStationNumber);
    }
    public List<Passenger> getPassengers() {
        return passengers;
    }



    // --- Logika działania pojazdu ---
    public void nextStation() {
        if (currentStationNumber < route.size() - 1) {
            currentStationNumber++;
        }
    }

    public void boardPassenger(Passenger passenger) {
        if (passengers.size() < capacity) {
            passengers.add(passenger);
        }
    }

    public void unloadPassengers() {
        String currentStationName = getCurrentStation().getName();
        passengers.removeIf(p -> p.getDestination().equals(currentStationName));
    }



}

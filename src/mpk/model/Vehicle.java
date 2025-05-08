package mpk.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Vehicle {

    // --- Pola ---
    protected String id;
    protected int currentStationIndex = 0;
    public int capacity;
    public List<Station> route;
    public List<Passenger> passengers = new ArrayList<>();

    // --- Konstruktor ---
    public Vehicle(String id, List<Station> route, int capacity) {
        this.id = id;
        this.route = route;
        this.capacity = capacity;
    }

    // --- Gettery ---
    public String getId() {
        return id;
    }

    public int getCurrentStationIndex() {
        return currentStationIndex;
    }

    public Station getCurrentStation() {
        return route.get(currentStationIndex);
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public boolean hasSpace() {
        return passengers.size() < capacity;
    }

    // --- Logika działania pojazdu ---
    public void nextStation() {
        if (currentStationIndex < route.size() - 1) {
            currentStationIndex++;
        }
    }

    public void boardPassenger(Passenger passenger) {
        if (hasSpace()) {
            passengers.add(passenger);
        }
    }

    public void unloadPassengers() {
        String currentStationName = getCurrentStation().getName();
        passengers.removeIf(p -> p.getDestination().equals(currentStationName));
    }
}

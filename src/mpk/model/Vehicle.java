package mpk.model;

import java.util.*;

public abstract class Vehicle {
    protected String id;
    public List<Stop> route;
    protected int currentStopIndex = 0;
    public int capacity;
    public List<Passenger> passengers = new ArrayList<>();

    public Vehicle(String id, List<Stop> route, int capacity) {
        this.id = id;
        this.route = route;
        this.capacity = capacity;
    }

    public void nextStop() {
        if (currentStopIndex < route.size() - 1) {
            currentStopIndex++;
        }
    }

    public Stop getCurrentStop() {
        return route.get(currentStopIndex);
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public boolean hasSpace() {
        return passengers.size() < capacity;
    }

    public void boardPassenger(Passenger p) {
        if (hasSpace()) {
            passengers.add(p);
        }
    }

    public void unloadPassengers() {
        String currentStopName = getCurrentStop().getName();
        passengers.removeIf(p -> p.getDestination().equals(currentStopName));
    }

    public String getId() {
        return id;
    }
}
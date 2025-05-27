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


    public Vehicle(String name, List<Station> route, int capacity) {
        this.name = name;
        this.route = route;
        this.capacity = capacity;
    }

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
    public Station getCurrentStation() {
        return route.get(currentStationNumber);
    }
    public List<Passenger> getPassengers() {
        return passengers;
    }



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
        // This removes passengers who’ve reached their stop
        String stopNow = getCurrentStation().getName();

        // this mutates the list while iterating, but removeIf handles that fine
        passengers.removeIf(passenger -> stopNow.equals(passenger.getDestination()));
    }

}

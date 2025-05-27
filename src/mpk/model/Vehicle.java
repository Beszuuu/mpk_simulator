package mpk.model;

import java.util.ArrayList;
import java.util.List;

public class Vehicle implements InterfaceVehicle {

    protected String name;
    protected int currentStationNumber = 0;
    public int capacity;
    public List<Station> route;
    public List<Passenger> passengers = new ArrayList<>();
    protected boolean active = true;
    private int boughtTickets = 0;

    public Vehicle(String name, List<Station> route, int capacity) {
        this.name = name;
        this.route = route;
        this.capacity = capacity;
    }

    public void incrementBoughtTickets() {
        boughtTickets++;
    }

    public int getBoughtTickets() {
        return boughtTickets;
    }

    @Override
    public String getName() {
        return name;
    }
    @Override
    public List<Station> getRoute() {
        return route;
    }
    @Override
    public Integer getCapacity() { return capacity; }
    @Override
    public boolean isActive() { return active; }
    @Override
    public int getCurrentStationNumber() {
        return currentStationNumber;
    }
    @Override
    public Station getCurrentStation() {
        // Kind of risky if currentStationNumber is ever out of bounds... maybe add guard later?
        return route.get(currentStationNumber);
    }
    @Override
    public List<Passenger> getPassengers() {
        return passengers;
    }



    @Override
    public void nextStation() {
        if (!active) return;

        unloadPassengers();

        if (currentStationNumber < (route.size() - 1)) {
            currentStationNumber++;
        } else {
            active = false;
        }
    }



    @Override
    public void boardPassenger(Passenger p) {
        // Thought about throwing an exception, but maybe we just ignore silently if full
        if (passengers.size() < capacity) {
            passengers.add(p);
        } else {
            // Optional: log or print warning if over capacity?
        }
    }

    @Override
    public void unloadPassengers() {
        // Drop off passengers who have reached their intended stop
        String currentStopName = getCurrentStation().getName();

        // Could use iterator manually, but removeIf is neater here
        passengers.removeIf(passenger -> {
            // Might want to log this unload later for debugging
            return currentStopName.equals(passenger.getDestination());
        });

        // TODO: Possibly return list of removed passengers for further processing
    }

}

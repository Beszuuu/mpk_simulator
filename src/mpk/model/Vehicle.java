package mpk.model;

import java.util.ArrayList;
import java.util.List;

public class Vehicle implements InterfaceVehicle {

    protected String name;
    protected int currentStationNumber = 0;  // Index in the route
    public int capacity;
    public List<Station> route;
    public List<Passenger> passengers = new ArrayList<>();  // Who’s currently onboard
    protected boolean active = true;  // Whether vehicle still running
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
    public Integer getCapacity() {
        return capacity;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public int getCurrentStationNumber() {
        return currentStationNumber;
    }

    @Override
    public Station getCurrentStation() {
        // NOTE: we assume index is valid — might wanna guard against overflow later
        return route.get(currentStationNumber);
    }

    @Override
    public List<Passenger> getPassengers() {
        return passengers;
    }

    @Override
    public void nextStation() {
        if (!active) return;

        unloadPassengers();  // Drop off anyone whose stop is here

        if (currentStationNumber < (route.size() - 1)) {
            currentStationNumber++;
        } else {
            active = false;  // Reached end of the line
        }
    }

    @Override
    public void boardPassenger(Passenger p) {
        // We could throw an exception if full, but for now just silently ignore
        if (passengers.size() < capacity) {
            passengers.add(p);
        } else {
            // Could log or warn about overboarding attempt
        }
    }

    @Override
    public void unloadPassengers() {
        // Remove passengers whose destination matches current stop
        String currentStopName = getCurrentStation().getName();

        // removeIf feels nicer here than iterating manually
        passengers.removeIf(passenger -> {
            // Optionally log each removal for tracing/debug
            return currentStopName.equals(passenger.getDestination());
        });

        // Note: not tracking dropped-off passengers anywhere... maybe later
    }
}

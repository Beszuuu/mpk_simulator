package mpk.model;

import java.util.List;

// INHERITANCE Extends Vehicle
public class Tram extends Vehicle {
    // POLYMORPHISM Can override Vehicle methods
    public Tram(String id, List<Station> route, int capacity) {
        super(id, route, capacity);
    }
}

package mpk.model;

import java.util.List;

// INHERITANCE Extends Vehicle
public class Bus extends Vehicle {
    // POLYMORPHISM Can override Vehicle methods
    public Bus(String id, List<Station> route, int capacity) {
        super(id, route, capacity);
    }
}
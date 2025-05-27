package mpk.model;

import java.util.List;

public class Bus extends Vehicle {
    public Bus(String id, List<Station> route, int capacity) {
        super(id, route, capacity);
    }
}
package mpk.model;

import java.util.List;

public class Bus extends Vehicle {
    public Bus(String id, List<Stop> route) {
        super(id, route, 30);
    }
}
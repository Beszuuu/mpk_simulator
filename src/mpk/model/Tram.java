package mpk.model;

import java.util.List;

public class Tram extends Vehicle {
    public Tram(String id, List<Station> route, int capacity) {
        super(id, route, capacity);
    }
}

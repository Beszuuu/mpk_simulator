package mpk.model;

import java.util.List;

public class Tram extends Vehicle {
    public Tram(String id, List<Stop> route) {
        super(id, route, 40);
    }
}

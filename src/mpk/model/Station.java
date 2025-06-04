package mpk.model;

// AGGREGATION Independent existence
public class Station {
    // ENCAPSULATION Private fields
    private final String name;
    private final double popularity; // A value representing how "busy" or "well-known" the station is

    public Station(String name, double popularity) {
        this.name = name;
        this.popularity = popularity;
    }

    public String getName() {
        return name;
    }

    public double getPopularity() {
        return popularity;
    }

    @Override
    public String toString() {
        return name;
    }
}

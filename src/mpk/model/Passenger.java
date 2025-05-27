package mpk.model;


public class Passenger {
    private final String name;
    private final String destination;
    private final boolean hasTicket;

    public Passenger(String name, String destination, boolean hasTicket) {
        this.name = name;
        this.destination = destination;
        this.hasTicket = hasTicket;
    }

    public String getName() {
        return name;
    }

    public String getDestination() {
        return destination;
    }

    public boolean hasTicket() {
        return hasTicket;
    }
}

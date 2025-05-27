package mpk.model;


public class Passenger {
    private final String fullName;
    private final String destination;
    private final boolean hasTicket;

    public Passenger(String fullName, String destination, boolean hasTicket) {
        this.fullName = fullName;
        this.destination = destination;
        this.hasTicket = hasTicket;
    }

    public String getName() {
        return fullName;
    }

    public String getDestination() {
        return destination;
    }

    public boolean hasTicket() {
        return hasTicket;
    }
}

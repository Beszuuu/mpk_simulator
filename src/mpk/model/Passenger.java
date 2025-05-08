package mpk.model;

public class Passenger {
    private String destination;
    private boolean hasTicket;

    public Passenger(String destination, boolean hasTicket) {
        this.destination = destination;
        this.hasTicket = hasTicket;
    }

    public String getDestination() {
        return destination;
    }

    public boolean hasTicket() {
        return hasTicket;
    }
}

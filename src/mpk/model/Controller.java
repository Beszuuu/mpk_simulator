package mpk.model;

public class Controller {
    public int checkPassengers(java.util.List<Passenger> passengers) {
        int withoutTicket = 0;
        for (Passenger p : passengers) {
            if (!p.hasTicket()) withoutTicket++;
        }
        return withoutTicket;
    }
}
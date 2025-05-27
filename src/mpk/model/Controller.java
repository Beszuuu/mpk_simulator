package mpk.model;

import java.util.List;

public class Controller {

    // This method goes through all passengers and counts how many don't have a ticket
    public int checkPassengers(List<Passenger> passengerList) {
        int counter = 0;

        for (Passenger person : passengerList) {
            if (!person.hasTicket()) {
                counter++;  //caught one without a ticket
            }
        }

        return counter;
    }
}

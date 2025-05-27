package mpk.model;

import java.util.List;

public interface InterfaceVehicle {
    String getName();

    List<Station> getRoute();

    Integer getCapacity();

    boolean isActive();

    int getCurrentStationNumber();

    Station getCurrentStation();

    List<Passenger> getPassengers();

    void nextStation();

    void boardPassenger(Passenger p);

    void unloadPassengers();
}

package mpk.model;

import java.util.List;

public interface InterfaceVehicle {
    // ENCAPSULATION - only exposing method signatures, not implementation
    String getName();

    List<Station> getRoute();

    Integer getCapacity();

    boolean isActive();

    int getCurrentStationNumber();

    Station getCurrentStation();

    List<Passenger> getPassengers();

    // POLYMORPHISM To be implemented differently
    void nextStation();

    void boardPassenger(Passenger p);

    void unloadPassengers();
}

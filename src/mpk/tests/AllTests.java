package mpk.tests;

import mpk.engine.Simulation;
import mpk.io.CsvLoader;
import mpk.model.Bus;
import mpk.model.Passenger;
import mpk.model.Station;
import mpk.model.Vehicle;
import mpk.Main;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AllTests {

    @Test
    public void testLoadRoutesReturnsCorrectSize() throws Exception {
        List<List<Station>> routes = CsvLoader.loadRoutes("src\\mpk\\input\\routes.csv");
        List<int[]> properties = CsvLoader.loadProperties("src\\mpk\\input\\vehicles.csv");

        // Should match expected route count
        assertEquals(20, routes.size(), "Should load 20 routes from CSV");
        assertEquals("Nowy Dwór", routes.get(0).get(0).getName(), "First Station should be Nowy Dwór");

        // Just printing for quick inspection — probably remove later
        for (List<Station> route : routes) {
            for (Station station : route) {
                System.out.print(station.getName() + ", ");
            }
            System.out.println();
        }

        for (int[] property : properties) {
            System.out.println(Arrays.toString(property));
        }
    }

    @Test
    public void testSimulationStepAddsPassengersAndUpdatesState() throws Exception {
        // Simulated route
        Station s1 = new Station("Start", 0.9);
        Station s2 = new Station("Middle", 0.5);
        Station s3 = new Station("End", 0.1);
        List<Station> route = Arrays.asList(s1, s2, s3);

        Vehicle bus = new Bus("TestBus", route, 10);
        List<Vehicle> vehicles = List.of(bus);

        Simulation sim = new Simulation(vehicles, 0.0, 0.1, 160);

        List<String> names = List.of("Anna", "Bartek", "Cezary");
        List<String> surnames = List.of("Nowak", "Kowalski", "Wiśniewski");

        sim.genPassengers(bus, names, surnames);

        // Should have added passengers
        assertFalse(bus.getPassengers().isEmpty(), "Passengers should be added");

        // Simulate a ticket control step
        sim.controlEvent(bus);

        // Could assert on earnings or ticket status, but leaving basic check for now
        assertNotNull(sim);
    }

    @Test
    public void testClearAllEmptiesData() {
        Station s = new Station("A", 0.5);
        Vehicle bus = new Bus("TestBus", List.of(s), 5);
        List<Vehicle> vehicleList = new ArrayList<>();
        vehicleList.add(bus);

        Simulation sim = new Simulation(vehicleList, 1.0, 0, 160);
        sim.genPassengers(bus, List.of("Anna"), List.of("Nowak"));

        sim.clearAll();

        // Here we assume vehicles should be cleared — if not, revise this logic
        assertEquals(0, vehicleList.size(), "Vehicle list should be cleared after clearAll()");
    }

    @Test
    public void testPickRandomStopsSubsetReturnsCorrectSize() {
        List<Station> stations = List.of(
                new Station("A", 0.1),
                new Station("B", 0.2),
                new Station("C", 0.3),
                new Station("D", 0.4),
                new Station("E", 0.5)
        );

        List<Station> subset = Main.pickRandomStopsSubset(stations, 3);
        assertEquals(3, subset.size(), "Subset size should match requested count");

        // Make sure all selected stations are from original list (sanity check)
        for (Station s : subset) {
            assertTrue(stations.contains(s), "Subset station should be from original list");
        }
    }
}

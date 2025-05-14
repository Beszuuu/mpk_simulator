package mpk.tests;

import mpk.engine.Simulation;
import mpk.io.CsvLoader;
import mpk.model.Bus;
import mpk.model.Passenger;
import mpk.model.Station;
import mpk.model.Vehicle;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AllTests {
    @Test
    public void testLoadRoutesReturnsCorrectSize() throws Exception {
        List<List<Station>> routes = CsvLoader.loadRoutes("src\\mpk\\input\\routes.csv");
        List<int[]> properties = CsvLoader.loadProperties("src\\mpk\\input\\vehicles.csv");

        assertEquals(20, routes.size(), "Should load 20 routes from CSV");
        assertEquals("Nowy Dwór", routes.get(0).get(0).getName(), "First Station should be Nowy Dwór");

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
}

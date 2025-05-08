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
        List<List<Station>> routes = CsvLoader.loadRoutes("src\\mpk\\utils\\routes.csv");
        List<int[]> properties = CsvLoader.loadProperties("src\\mpk\\utils\\vehicles.csv");

        assertEquals(20, routes.size(), "Should load 2 routes from CSV");
        assertEquals("Plac Grunwaldzki", routes.get(0).get(0).getName(), "First Station should be Plac Grunwaldzki");

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

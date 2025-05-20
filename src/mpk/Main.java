package mpk;
import mpk.io.*;
import mpk.model.*;
import mpk.engine.*;
import java.util.*;
public class Main {
    public static void main(String[] args) throws Exception {
        List<List<Station>> routes = CsvLoader.loadRoutes("src\\mpk\\input\\routes.csv");
        List<int[]> properties = CsvLoader.loadProperties("src\\mpk\\input\\vehicles.csv");

        System.out.println("Witaj w symulatorze MPK!");

        Scanner scanner = new Scanner(System.in);
        int bus = -1;
        int tram = -1;
        // Wprowadzenie liczby autobusów
        while (bus < 0 || bus > 10) {
            System.out.println("Podaj ilość kursujących autobusów (0->10): ");
            bus = scanner.nextInt();
            if (bus < 0 || bus > 10) {
                System.out.println("Nieprawidłowa wartość. Proszę spróbować ponownie.");
            }
        }
        // Wprowadzenie liczby tramwajów
        while (tram < 0 || tram > 10) {
            System.out.println("Podaj ilość kursujących tramwajów (0->10): ");
            tram = scanner.nextInt();
            if (tram < 0 || tram > 10) {
                System.out.println("Nieprawidłowa wartość. Proszę spróbować ponownie.");
            }
        }
        List<Vehicle> vehicles = new ArrayList<>();
        // Tworzenie autobusów
        for (int i = 0; i < bus; i++) {
            vehicles.add(new Bus("Bus" + i, routes.get(i), properties.get(i)[0]));
            System.out.println("Stworzono : " + vehicles.get(i).getName() + " z pojemnością: " + vehicles.get(i).getCapacity() + " pasażerów" + " na linii rozpoczynającej się od:" + vehicles.get(i).getRoute().getFirst().getName() + vehicles.get(i).getRoute().getLast().getName());
        }
        // Tworzenie tramwajów
        for (int i = bus; i < tram + bus; i++) {
            vehicles.add(new Tram("Tram" + i, routes.get(i), properties.get(i)[0])); // Używamy i + bus, aby nie kolidować z autobusami
            System.out.println("Stworzono : " + vehicles.get(i).getName() + " z pojemnością: " + vehicles.get(i).getCapacity() + " pasażerów" + " na linii rozpoczynającej się od:" + vehicles.get(i).getRoute().getFirst().getName());
        }
        Simulation sim = new Simulation(vehicles, 0.8);
        // Menu wyboru trybu symulacji
        String mode = "";
        while (!mode.equalsIgnoreCase("a") && !mode.equalsIgnoreCase("m")) {
            System.out.println("Wybierz tryb symulacji:");
            System.out.println("A - Automatyczny");
            System.out.println("M - Manualny (SPACJA i Q)");
            mode = scanner.next();
            if (!mode.equalsIgnoreCase("a") && !mode.equalsIgnoreCase("m")) {
                System.out.println("Nieprawidłowy wybór. Proszę spróbować ponownie.");
            }
        }
        // Tryb manualny
        if (mode.equalsIgnoreCase("m")) {
            while (true) {
                System.out.println("Naciśnij SPACJĘ aby kontynuować, lub Q aby zakończyć.");
                scanner.nextLine(); // Oczekiwanie na wprowadzenie z klawiatury
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("q")) break;
                sim.step();
            }
        }
        // Tryb automatyczny
        else {
            System.out.println("Ile razy symulacja ma być wykonana?");
            int iterations = scanner.nextInt();
            for (int i = 0; i < iterations; i++) {
                sim.step();
                try {
                    Thread.sleep(1000); // Zatrzymanie na 1 sekundę przed kolejnym krokiem
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            sim.step();

        }
        sim.summary();
        sim.clearAll();
        scanner.close();
    }
}
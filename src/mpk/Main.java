package mpk;

// Imports — left a few wildcards even though I know it's not best practice...
import mpk.engine.*;
import mpk.gui.GuiLauncher;
import mpk.io.*;
import mpk.model.*;

import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {

        Scanner userInput = new Scanner(System.in);  // Renamed for clarity... kinda

        do {
            System.out.println("-! Welcome to the MPK Simulator!");

            System.out.print("Wybierz interfejs (G - GUI, T - terminal): ");
            String choice = userInput.nextLine().trim().toLowerCase();

            if (choice.equals("g")) {
                GuiLauncher.launchGui();
                return;
            }

            // Load data from the input files (this is hardcoded)
            List<List<Station>> availableRoutes = CsvLoader.loadRoutes("src/mpk/input/routes.csv");
            List<int[]> vehicleStats = CsvLoader.loadProperties("src/mpk/input/vehicles.csv");

            // User input — get how many of each vehicle type we're dealing with today
            int busesCount = getUserNumber(userInput, "-> Enter number of buses (0–10): ", 0, 10);
            int tramsCount;
            if(busesCount == 0){
                tramsCount = getUserNumber(userInput, "-> Enter number of trams (1–10): ", 1, 10);
            }
            else {
                tramsCount = getUserNumber(userInput, "-> Enter number of trams (0–10): ", 0, 10);
            }

            // Ask user if they want to perform auto / manulal mode
            String simulationMode = getSimulationMode(userInput);

            // We'll fill this list with all the buses/trams
            List<Vehicle> fleet = new ArrayList<>();

            // Manual mode
            if (simulationMode.equalsIgnoreCase("m")) {
                userInput.nextLine(); // Ugh, Java Scanner is ....

                for (int i = 0; i < busesCount + tramsCount; i++) {
                    List<Station> fullRoute = availableRoutes.get(i); // Get the full route for the current vehicle
                    int maxAvailableStops = fullRoute.size(); // Get the number of stops in the full route

                    System.out.println("-! Vehicle " + i + " has " + maxAvailableStops + " stops to choose from.");

                    boolean pickRandomStops = promptYesNo(userInput, "-> Randomize stops for vehicle " + i + "? (y/n): ");
                    int chosenStops;

                    if (pickRandomStops) {
                        // Miłosz I swear this works  XD
                        chosenStops = new Random().nextInt(maxAvailableStops - 2) + 3;
                    } else {
                        chosenStops = getUserNumber(userInput, "-> Enter number of stops to include (2 - " + maxAvailableStops + "): ", 3, maxAvailableStops);
                    }

                    List<Station> selectedRoute;
                    if (pickRandomStops) {
                        // Pick a random subset of stops from the full route
                        selectedRoute = pickRandomStopsSubset(fullRoute, chosenStops);
                    } else {
                        // Pick the first 'chosenStops' stations from the route
                        selectedRoute = fullRoute.subList(0, chosenStops);
                    }

                    Vehicle v;
                    if (i < busesCount) {
                        // Create a Bus if index is within the bus count
                        v = new Bus("Bus" + i, selectedRoute, vehicleStats.get(i)[0]);
                    } else {
                        // Otherwise, create a Tram
                        v = new Tram("Tram" + i, selectedRoute, vehicleStats.get(i)[0]);
                    }

                    fleet.add(v);
                    System.out.println("-! Created: " + v.getName() + " with " + selectedRoute.size() + " stops.");
                }

            } else {
                // Automatic mode still asks a few things but doesn’t go full control freak
                userInput.nextLine(); // Scanner cleanup again

                for (int i = 0; i < busesCount + tramsCount; i++) {
                    List<Station> fullRoute = availableRoutes.get(i); // Get the full route for the current vehicle
                    int maxStops = fullRoute.size(); // Get the number of stops in the full route

                    boolean shuffleStops = promptYesNo(userInput, "-> Randomize stop selection for vehicle " + i + "? (y/n): ");
                    int stopCount;
                    if (shuffleStops) {
                        stopCount = new Random().nextInt(maxStops - 2) + 3; // range: 3 to maxStops
                    } else {
                        stopCount = getUserNumber(userInput, "-> Enter number of stops to include (3 - " + maxStops + "): ", 3, maxStops);
                    }

                    List<Station> shortRoute;
                    if (shuffleStops) {
                        shortRoute = pickRandomStopsSubset(fullRoute, stopCount);
                    } else {
                        shortRoute = fullRoute.subList(0, stopCount);
                    }

                    Vehicle v;
                    if (i < busesCount) {
                        v = new Bus("Bus" + i, shortRoute, vehicleStats.get(i)[0]);
                    } else {
                        v = new Tram("Tram" + i, shortRoute, vehicleStats.get(i)[0]);
                    }

                    fleet.add(v);
                    System.out.println("-! Auto-created: " + v.getName() + ", stops: " + shortRoute.size());
                }
            }

            // Finally, the sim runs. Hardcoded 80% passenger chance (tweak later?)
            Simulation simulation = new Simulation(fleet, 0.8);

            // Depending on mode, run sim differently
            if (simulationMode.equalsIgnoreCase("m")) {
                stepThroughSimulation(simulation, userInput);
            } else {
                runAutoSimulation(simulation, userInput);
            }

            simulation.summary();
            simulation.clearAll();  // Reset state for next run

        } while (askToRestart(userInput));  // Loop if user wants to try again

        userInput.close();
        System.out.println("-! Simulation ended. Goodbye!");
    }

    // ### Utility methods ###

    // I take this shit from internet but it works XD
    private static int getUserNumber(Scanner scanner, String prompt, int min, int max) {
        int value = -99;  // I take this shit from internet but it works
        while (value < min || value > max) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                value = scanner.nextInt();
            } else {
                scanner.next(); // Nuke invalid input
            }
            if (value < min || value > max) {
                System.out.println("-! Nope, try something between " + min + " and " + max + ".");
            }
        }
        return value;
    }

    private static String getSimulationMode(Scanner scanner) {
        String choice = "";
        while (!choice.equalsIgnoreCase("a") && !choice.equalsIgnoreCase("m")) {
            System.out.println("-> Choose mode: (A)utomatic or (M)anual (type A/M then Enter)");
            choice = scanner.next();
        }
        return choice;
    }

    private static void stepThroughSimulation(Simulation sim, Scanner scanner) throws Exception {
        scanner.nextLine(); // Clear buffer
        while (true) {
            System.out.println("-> Hit SPACE to move forward, Q to stop simulation:");
            String input = scanner.nextLine();
            if ("q".equalsIgnoreCase(input)) break;
            sim.step(); // Do a single step
        }
    }

    private static void runAutoSimulation(Simulation sim, Scanner scanner) throws Exception {
        int totalSteps = getUserNumber(scanner, "-> How many steps should we run? (each ~700ms): ", 1, 100);
        for (int i = 0; i < totalSteps; i++) {
            sim.step();
            try {
                Thread.sleep(700);  // Just a delay for better visibility
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static boolean askToRestart(Scanner scanner) {
        scanner.nextLine(); // Clean input
        System.out.println("-! Done! Want to run it again?");
        System.out.println("-> Press Enter to restart, Q to quit:");

        while (true) {
            String response = scanner.nextLine().trim().toLowerCase();
            if ("q".equals(response)) return false;
            if (response.isEmpty()) return true;

            System.out.println("-! Please repeat — try just hitting Enter to restart, or Q to quit.");
        }
    }

    private static boolean promptYesNo(Scanner scanner, String message) {
        String answer;
        do {
            System.out.print(message);
            answer = scanner.next();
        } while (!answer.equalsIgnoreCase("y") && !answer.equalsIgnoreCase("n"));
        return answer.equalsIgnoreCase("y");
    }

    public static List<Station> pickRandomStopsSubset(List<Station> list, int count) {
        // Shuffle a copy so we don't mess with the original
        List<Station> temp = new ArrayList<>(list);
        Collections.shuffle(temp);
        return temp.subList(0, count);  // Just take what we need
    }
}

package mpk.gui;

import mpk.model.Passenger;
import mpk.model.Station;
import mpk.model.Vehicle;

import java.util.List;

public class ConsoleUI {

    public static void showStepHeader(int step) {
        System.out.println("\n==================[ STEP " + step + " ]==================");
    }

    public static void showVehicleStatus(Vehicle v, int caught) {
        Station station = v.getCurrentStation();
        int stopNumber = v.getCurrentStationNumber();

        System.out.println("\n🚍 " + v.getName() + " is at 🛑 " + station.getName() + " (Stop " + stopNumber + ")");
        List<Passenger> passengers = v.getPassengers();
        if (passengers.isEmpty()) {
            System.out.println("   [No passengers on board]");
        } else {
            for (Passenger p : passengers) {
                String ticketIcon = p.hasTicket() ? "✅" : "❌";
                System.out.println("   - " + p.getName() + " → " + p.getDestination() + " " + ticketIcon);
            }
        }

        if (caught > 0) {
            System.out.println("   ⚠️ Controller appeared and caught " + caught + " passenger" + (caught > 1 ? "s" : "") + " without tickets!");
        }
    }

    public static void showVehicleAtDepot(Vehicle v) {
        System.out.println("\n🚍 " + v.getName() + " is now at 🅿 DEPOT (end of route)");
    }

    public static void showSummary(int earnings, int totalCaptures) {
        System.out.println("\n==================[ SIMULATION SUMMARY ]==================");
        System.out.println("💰 Total earnings from fines: " + earnings + " PLN");
        System.out.println("🚔 Total passengers caught without tickets: " + totalCaptures);
    }

    public static void showClearMessage() {
        System.out.println("\n🧹 Clearing vehicles and results for new simulation...");
    }

    public static void showWelcomeMessage() {
        System.out.println("\n=======================================");
        System.out.println("🚦 Welcome to the MPK Transport Simulator 🚦");
        System.out.println("=======================================\n");
    }

    public static void showModePrompt() {
        System.out.println("-> Choose simulation mode: (A)utomatic or (M)anual (type A/M then Enter)");
    }

    public static void showVehicleCreated(String name, int stopCount) {
        System.out.println("-! Created: " + name + " with " + stopCount + " stops.");
    }

    public static void showAutoVehicleCreated(String name, int stopCount) {
        System.out.println("-! Auto-created: " + name + ", stops: " + stopCount);
    }

    public static void showRestartPrompt() {
        System.out.println("\n-> Press Enter to restart, or type Q to quit:");
    }

    public static void showSimulationEnd() {
        System.out.println("-! Simulation ended. Goodbye!");
    }

    public static void showInvalidInputRange(int min, int max) {
        System.out.println("-! Invalid input. Please enter a number between " + min + " and " + max + ".");
    }

    public static void showManualStepPrompt() {
        System.out.println("-> Press ENTER to advance one step, or type Q to quit:");
    }

    public static void showAutoStepRunning(int step, int totalSteps) {
        System.out.println("[Auto] Running step " + step + "/" + totalSteps + "...");
    }
}

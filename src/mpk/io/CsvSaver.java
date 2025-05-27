package mpk.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class CsvSaver {
    public static void saveControlResults(Map<String, Map<String, Integer>> results, Map<String, Integer> boughtTicketsMap, int totalCaptures, int earnings, int totalBoughtTickets) throws IOException {
        String folderPath = "src/mpk/output";

        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Find the next available file name
        int fileIndex = 1;
        File file;
        do {
            file = new File(folderPath + File.separator + "inspection_" + fileIndex + ".csv");
            fileIndex++;
        } while (file.exists());

        // Use the previous index (last one before an unused file name)
        fileIndex--;
        File outputFile = new File(folderPath + File.separator + "inspection_" + fileIndex + ".csv");


        FileWriter writer = new FileWriter(outputFile);

        // Write bought tickets per vehicle (sorted alphabetically by vehicle name)
        for (Map.Entry<String, Integer> entry : new TreeMap<>(boughtTicketsMap).entrySet()) {
            writer.write(entry.getKey() + ": " + entry.getValue() + " bought tickets\n");
        }
        writer.write("\n");

        // Write detailed control (inspection) results
        writer.write("Control results:\n");
        for (Map.Entry<String, Map<String, Integer>> vehicleEntry : results.entrySet()) {
            String vehicleName = vehicleEntry.getKey();
            writer.write(vehicleName + ":\n");

            for (Map.Entry<String, Integer> stationEntry : vehicleEntry.getValue().entrySet()) {
                writer.write(stationEntry.getKey() + ", " + stationEntry.getValue() + " caught\n");
            }

            writer.write("\n");
        }

        // Write summary statistics at the end of the file
        writer.write("Total captures: " + totalCaptures + "\n");
        writer.write("Total of bought tickets: " + totalBoughtTickets + "\n" );
        writer.write("Total earnings: " + earnings + " PLN \n");

        writer.close();
        System.out.println("Zapisano kontrolę do pliku: " + outputFile.getPath());
    }
}

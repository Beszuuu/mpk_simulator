package mpk.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class CsvSaver {
    public static void saveControlResults(Map<String, Map<String, Integer>> results, int totalCaptures, int earnings) throws IOException {
        String folderPath = "src/mpk/output";

        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Najwyższy numer istniejącego pliku
        int fileIndex = 1;
        File file;
        do {
            file = new File(folderPath + File.separator + "inspection_" + fileIndex + ".csv");
            fileIndex++;
        } while (file.exists());

        fileIndex--;
        File outputFile = new File(folderPath + File.separator + "inspection_" + fileIndex + ".csv");

        // Zapis do pliku
        FileWriter writer = new FileWriter(outputFile);

        for (Map.Entry<String, Map<String, Integer>> vehicleEntry : results.entrySet()) {
            String vehicleName = vehicleEntry.getKey();
            writer.write(vehicleName + ":\n");

            for (Map.Entry<String, Integer> stationEntry : vehicleEntry.getValue().entrySet()) {
                writer.write(stationEntry.getKey() + ", " + stationEntry.getValue() + " caught\n");
            }

            writer.write("\n");
        }

        writer.write("Total captures: " + totalCaptures + "\n");
        writer.write("Total earnings: " + earnings + " PLN \n");

        writer.close();
        System.out.println("Zapisano kontrolę do pliku: " + outputFile.getPath());
    }
}

package filehandling;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    // Crucial Fix: Define all file fields here so DataLoader and GUI classes can resolve them
    public static final String PATIENTS_FILE = "data/patients.txt";
    public static final String DOCTORS_FILE = "data/doctors.txt";
    public static final String ADMINS_FILE = "data/admins.txt";
    public static final String APPOINTMENTS_FILE = "data/appointments.txt";
    public static final String BILLS_FILE = "data/bills.txt";

    private static final String DATA_FOLDER = "data/";

    public static void initialize() {
        File folder = new File(DATA_FOLDER);
        if (!folder.exists()) {
            folder.mkdir();
        }
        createIfNotExists(PATIENTS_FILE);
        createIfNotExists(DOCTORS_FILE);
        createIfNotExists(ADMINS_FILE);
        createIfNotExists(APPOINTMENTS_FILE);
        createIfNotExists(BILLS_FILE);
    }

    private static void createIfNotExists(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Could not create database file: " + filePath);
        }
    }

    public static List<String> readAll(String filePath) {
        List<String> lines = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return lines;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line.trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading: " + filePath);
        }
        return lines;
    }

    public static void writeAll(String filePath, List<String> lines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error overwriting: " + filePath);
        }
    }

    public static void appendLine(String filePath, String line) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error appending record: " + filePath);
        }
    }

    public static void deleteById(String filePath, String id) {
        List<String> lines = readAll(filePath);
        List<String> updatedLines = new ArrayList<>();
        for (String line : lines) {
            String[] tokens = line.split("\\|", -1);
            if (tokens.length > 0 && !tokens[0].equals(id)) {
                updatedLines.add(line);
            }
        }
        writeAll(filePath, updatedLines);
    }

    public static void updateById(String filePath, String id, String newLineData) {
        List<String> lines = readAll(filePath);
        List<String> updatedLines = new ArrayList<>();
        for (String line : lines) {
            String[] tokens = line.split("\\|", -1);
            if (tokens.length > 0 && tokens[0].equals(id)) {
                updatedLines.add(newLineData);
            } else {
                updatedLines.add(line);
            }
        }
        writeAll(filePath, updatedLines);
    }

    public static String findById(String filePath, String id) {
        List<String> lines = readAll(filePath);
        for (String line : lines) {
            String[] tokens = line.split("\\|", -1);
            if (tokens.length > 0 && tokens[0].equals(id)) {
                return line;
            }
        }
        return null;
    }
    /**
     * Checks if a specific record marker or value exists within a column/field in the file.
     * Useful for checking if an email or ID already exists before creating a duplicate.
     */
    public static boolean exists(String filePath, String targetValue) {
        List<String> lines = readAll(filePath);
        for (String line : lines) {
            // Split by pipe symbol to inspect fields (e.g., email or ID fields)
            String[] tokens = line.split("\\|", -1);
            for (String token : tokens) {
                if (token.equalsIgnoreCase(targetValue.trim())) {
                    return true;
                }
            }
        }
        return false;
    }
}

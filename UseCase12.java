/*
=========================================================
 Book My Stay App
 Use Case 12: Data Persistence & System Recovery
 Single File Version for VS Code
 File Name: UseCase12.java
=========================================================

 Goal:
 Save hotel room inventory into a text file
 and restore it when program restarts.

 Concepts:
 - File Handling
 - Persistence
 - Recovery
 - HashMap
 - OOP
=========================================================
*/

import java.io.*;
import java.util.*;

public class UseCase12 {

    // File to store inventory data
    static final String FILE_NAME = "inventory.txt";

    // Inventory storage
    static HashMap<String, Integer> inventory = new HashMap<>();

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println(" Use Case 12: Data Persistence");
        System.out.println(" & System Recovery");
        System.out.println("====================================");

        // Step 1: Load previous data
        loadInventory();

        // Step 2: If no file found, start fresh
        if (inventory.isEmpty()) {
            System.out.println("No saved inventory found.");
            System.out.println("Starting fresh...\n");

            inventory.put("Single", 5);
            inventory.put("Double", 3);
            inventory.put("Suite", 2);
        } else {
            System.out.println("Recovered Previous Inventory Successfully!\n");
        }

        // Step 3: Display inventory
        showInventory();

        // Step 4: Save current data
        saveInventory();

        System.out.println("\nProgram Finished Successfully.");
    }

    // =====================================================
    // Show Inventory
    // =====================================================
    public static void showInventory() {
        System.out.println("Current Room Inventory:");
        for (String room : inventory.keySet()) {
            System.out.println(room + " : " + inventory.get(room));
        }
    }

    // =====================================================
    // Save Inventory to File
    // =====================================================
    public static void saveInventory() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME));

            for (String room : inventory.keySet()) {
                writer.write(room + "," + inventory.get(room));
                writer.newLine();
            }

            writer.close();
            System.out.println("\nInventory saved successfully to file.");

        } catch (Exception e) {
            System.out.println("Error saving inventory.");
        }
    }

    // =====================================================
    // Load Inventory from File
    // =====================================================
    public static void loadInventory() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));

            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                inventory.put(parts[0], Integer.parseInt(parts[1]));
            }

            reader.close();

        } catch (Exception e) {
            // File may not exist first time
        }
    }
}                                                               
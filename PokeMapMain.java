package cs310c;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * PokeMapMain class contains the main method and commandline
 * interface for interacting with the Pokemap. It loads data from the CSV file
 * and handles the user commands.
 */
public class PokeMapMain {

    // Set filename 
    private static final String FILENAME = "src/cs310c/pokemon_pokedex_alt.csv";

    // Method to load Pokémon data from CSV file into Pokemap
    private static void loadFromCSV(Pokemap pokemap) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            br.readLine(); // Skip the header line
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");  // Split by comma

                // Ensure there are enough columns 
                if (columns.length < 11) {
                    System.out.println("Skipping incomplete row due to insufficient columns: " + line);
                    continue;
                }

                // Extract the Pokémon name from the 6th column (index 5)
                String name = columns[5].trim().toLowerCase();  // Pokémon name

                // Handle type1 and type2, with type2 going to an empty string if missing
                String type1 = columns[10].trim();
                String type2 = (columns.length > 11) ? columns[11].trim() : "";  // If no type2, set to empty string

                // Build the entry string using relevant columns
                String entry = String.format(
                    "Type: %s/%s, HP: %s, Attack: %s, Defense: %s, Speed: %s",
                    type1, type2.isEmpty() ? "None" : type2,  // Display "None" if type2 is empty
                    columns[3].trim(),           // hp
                    columns[0].trim(),           // attack
                    columns[2].trim(),           // defense
                    columns[9].trim()            // speed
                );

                // Debugging output to confirm each Pokémon added
                System.out.println("Adding: " + name + " -> " + entry);

                pokemap.add(name, entry);
            }
            System.out.println("Loaded Pokémon data from CSV successfully.");
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Check 
        if (!Files.exists(Paths.get(FILENAME))) {
            System.out.println("File not found: " + FILENAME);
            System.out.println("Please ensure the file path is correct.");
            return;  // Exit if the file is not found
        }

        Pokemap pokemap = new Pokemap();
        Scanner scanner = new Scanner(System.in);

        // Load data from CSV at startup
        System.out.println("Loading data from " + FILENAME + "...");
        loadFromCSV(pokemap);

        // Display available commands and start command loop
        System.out.println("Welcome to the Pokemap!");
        pokemap.help();  // Show available commands at the start

        while (true) {
            System.out.print("\nEnter command: ");
            String command = scanner.nextLine().trim();
            String[] parts = command.split(" ", 2);
            String action = parts[0].toLowerCase();

            switch (action) {
                case "add":
                    if (parts.length < 2) {
                        System.out.println("Usage: add <name> <entry>");
                    } else {
                        String[] commandArgs = parts[1].split(" ", 2);
                        if (commandArgs.length < 2) {
                            System.out.println("Usage: add <name> <entry>");
                        } else {
                            boolean success = pokemap.add(commandArgs[0], commandArgs[1]);
                            System.out.println(success ? "Added successfully." : "Duplicate entry.");
                        }
                    }
                    break;

                case "find":
                    if (parts.length < 2) {
                        System.out.println("Usage: find <name>");
                    } else {
                        String result = pokemap.find(parts[1]);
                        System.out.println(result != null ? result : "Pokémon not found.");
                    }
                    break;

                case "delete":
                    if (parts.length < 2) {
                        System.out.println("Usage: delete <name>");
                    } else {
                        boolean success = pokemap.delete(parts[1]);
                        System.out.println(success ? "Deleted successfully." : "Pokémon not found.");
                    }
                    break;

                case "printHT":
                    pokemap.printHT();
                    break;

                case "who":
                    pokemap.who();
                    break;

                case "help":
                case "?":
                    pokemap.help();
                    break;

                case "exit":
                case "q":
                case "x":
                    pokemap.exit();
                    scanner.close();
                    return;

                default:
                    System.out.println("Unknown command. Type 'help' for a list of commands.");
            }
        }
    }
}


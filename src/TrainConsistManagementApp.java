import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// Bogie class
class Bogie {
    String name;
    int capacity;

    // Constructor
    Bogie(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    // toString() for display
    @Override
    public String toString() {
        return name + " -> Capacity: " + capacity;
    }
}

public class TrainConsistManagementApp {

    public static void main(String[] args) {

        System.out.println("=== Train Consist Management App ===");

        // Create list of bogies (reused from UC7)
        List<Bogie> bogieList = new ArrayList<>();

        // Add passenger bogies
        bogieList.add(new Bogie("Sleeper", 72));
        bogieList.add(new Bogie("AC Chair", 56));
        bogieList.add(new Bogie("First Class", 24));

        // UC7: Sort bogies by capacity (ascending)
        bogieList.sort(Comparator.comparingInt(b -> b.capacity));

        // Display sorted bogies
        System.out.println("\nBogies sorted by capacity (ascending):");
        for (Bogie b : bogieList) {
            System.out.println(b);
        }

        // UC8: Filter passenger bogies using Stream API
        // Convert list to stream, apply filter, collect result into a new list
        List<Bogie> filteredBogies = bogieList.stream()
                .filter(b -> b.capacity > 60)
                .collect(Collectors.toList());

        // Display filtered bogies
        System.out.println("\nBogies with capacity greater than 60:");
        if (filteredBogies.isEmpty()) {
            System.out.println("No bogies match the filter condition.");
        } else {
            filteredBogies.forEach(System.out::println);
        }

        // Verify original list is unchanged
        System.out.println("\nOriginal bogie list (unchanged):");
        for (Bogie b : bogieList) {
            System.out.println(b);
        }

        // Program continues...
    }
}
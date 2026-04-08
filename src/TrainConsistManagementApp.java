import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

        // Create list of bogies
        List<Bogie> bogieList = new ArrayList<>();

        // Add passenger bogies
        bogieList.add(new Bogie("Sleeper", 72));
        bogieList.add(new Bogie("AC Chair", 56));
        bogieList.add(new Bogie("First Class", 24));

        // Sort bogies by capacity (ascending)
        bogieList.sort(Comparator.comparingInt(b -> b.capacity));

        // Display sorted bogies
        System.out.println("\nBogies sorted by capacity (ascending):");
        for (Bogie b : bogieList) {
            System.out.println(b);
        }

        // Program continues...
    }
}
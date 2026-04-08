import java.util.HashMap;
import java.util.Map;

public class TrainConsistManagementApp {

    public static void main(String[] args) {

        System.out.println("=== Train Consist Management App ===");

        // Create HashMap to store bogie and its capacity
        Map<String, Integer> bogieCapacityMap = new HashMap<>();

        // Insert bogie-capacity pairs
        bogieCapacityMap.put("Sleeper", 72);
        bogieCapacityMap.put("AC Chair", 78);
        bogieCapacityMap.put("First Class", 24);

        // Display bogie capacities using entrySet()
        System.out.println("\nBogie Capacity Details:");

        for (Map.Entry<String, Integer> entry : bogieCapacityMap.entrySet()) {
            String bogie = entry.getKey();
            int capacity = entry.getValue();

            System.out.println(bogie + " -> Capacity: " + capacity);
        }
    }
}
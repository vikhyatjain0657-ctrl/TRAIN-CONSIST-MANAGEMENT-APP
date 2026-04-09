import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

class Bogie {
    String name;
    int capacity;

    Bogie(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return name + " -> Capacity: " + capacity;
    }
}

public class TrainConsistManagementApp {

    public static void main(String[] args) {

        System.out.println("=== Train Consist Management App ===");

        List<Bogie> bogieList = new ArrayList<>();

        bogieList.add(new Bogie("Sleeper", 72));
        bogieList.add(new Bogie("AC Chair", 56));
        bogieList.add(new Bogie("First Class", 24));
        bogieList.add(new Bogie("Sleeper", 68));
        bogieList.add(new Bogie("AC Chair", 60));

        bogieList.sort(Comparator.comparingInt(b -> b.capacity));

        System.out.println("\nBogies sorted by capacity (ascending):");
        for (Bogie b : bogieList) {
            System.out.println(b);
        }

        List<Bogie> filteredBogies = bogieList.stream()
                .filter(b -> b.capacity > 60)
                .collect(Collectors.toList());

        System.out.println("\nBogies with capacity greater than 60:");
        if (filteredBogies.isEmpty()) {
            System.out.println("No bogies match the filter condition.");
        } else {
            filteredBogies.forEach(System.out::println);
        }

        Map<String, List<Bogie>> groupedBogies = bogieList.stream()
                .collect(Collectors.groupingBy(b -> b.name));

        System.out.println("\nBogies grouped by type:");
        groupedBogies.forEach((type, bogies) -> {
            System.out.println("\nType: " + type);
            bogies.forEach(b -> System.out.println("  " + b));
        });

        int totalSeats = bogieList.stream()
                .map(b -> b.capacity)
                .reduce(0, Integer::sum);

        System.out.println("\nTotal Seating Capacity of Train: " + totalSeats);

        Pattern trainIdPattern = Pattern.compile("TRN-\\d{4}");
        Pattern cargoCodePattern = Pattern.compile("PET-[A-Z]{2}");

        String trainId = "TRN-1234";
        String cargoCode = "PET-AB";

        Matcher trainIdMatcher = trainIdPattern.matcher(trainId);
        Matcher cargoCodeMatcher = cargoCodePattern.matcher(cargoCode);

        System.out.println("\n=== UC11: Train ID & Cargo Code Validation ===");

        if (trainIdMatcher.matches()) {
            System.out.println("Train ID: " + trainId + " -> Valid");
        } else {
            System.out.println("Train ID: " + trainId + " -> Invalid");
        }

        if (cargoCodeMatcher.matches()) {
            System.out.println("Cargo Code: " + cargoCode + " -> Valid");
        } else {
            System.out.println("Cargo Code: " + cargoCode + " -> Invalid");
        }
    }
}
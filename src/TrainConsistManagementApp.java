import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

class InvalidCapacityException extends Exception {
    InvalidCapacityException(String message) {
        super(message);
    }
}

class CargoSafetyException extends RuntimeException {
    CargoSafetyException(String message) {
        super(message);
    }
}

class Bogie {
    String name;
    int capacity;

    Bogie(String name, int capacity) throws InvalidCapacityException {
        if (capacity <= 0) {
            throw new InvalidCapacityException("Capacity must be greater than zero");
        }
        this.name = name;
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return name + " -> Capacity: " + capacity;
    }
}

class GoodsBogie {
    String type;
    String cargo;

    GoodsBogie(String type, String cargo) {
        this.type = type;
        this.cargo = cargo;
    }

    void assignCargo(String newCargo) {
        try {
            if (this.type.equals("Rectangular") && newCargo.equals("Petroleum")) {
                throw new CargoSafetyException(
                        "Unsafe assignment: Petroleum cannot be assigned to a Rectangular bogie");
            }
            this.cargo = newCargo;
            System.out.println("Cargo assigned successfully: " + newCargo + " -> " + this.type);
        } catch (CargoSafetyException e) {
            System.out.println("Cargo assignment failed: " + e.getMessage());
        } finally {
            System.out.println("Cargo assignment validation completed for bogie type: " + this.type);
        }
    }

    @Override
    public String toString() {
        return "Type: " + type + " | Cargo: " + cargo;
    }
}

public class TrainConsistManagementApp {

    public static String[] sortBogieNames(String[] names) {
        java.util.Arrays.sort(names);
        return names;
    }

    public static void main(String[] args) {

        System.out.println("=== Train Consist Management App ===");

        List<Bogie> bogieList = new ArrayList<>();

        try {
            bogieList.add(new Bogie("Sleeper", 72));
            bogieList.add(new Bogie("AC Chair", 56));
            bogieList.add(new Bogie("First Class", 24));
            bogieList.add(new Bogie("Sleeper", 68));
            bogieList.add(new Bogie("AC Chair", 60));
        } catch (InvalidCapacityException e) {
            System.out.println("Error: " + e.getMessage());
        }

        bogieList.sort(Comparator.comparingInt(b -> b.capacity));

        System.out.println("\nBogies sorted by capacity (ascending):");
        for (Bogie b : bogieList) {
            System.out.println(b);
        }

        System.out.println("\n=== UC17: Sorting Bogie Names Using Arrays.sort() ===");

        String[] bogieNames = new String[bogieList.size()];

        for (int i = 0; i < bogieList.size(); i++) {
            bogieNames[i] = bogieList.get(i).name;
        }

        java.util.Arrays.sort(bogieNames);

        System.out.println(java.util.Arrays.toString(bogieNames));

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

        System.out.println("Train ID: " + trainId + " -> " + (trainIdMatcher.matches() ? "Valid" : "Invalid"));
        System.out.println("Cargo Code: " + cargoCode + " -> " + (cargoCodeMatcher.matches() ? "Valid" : "Invalid"));

        System.out.println("\n=== UC12: Safety Compliance Check for Goods Bogies ===");

        List<GoodsBogie> goodsBogieList = new ArrayList<>();

        goodsBogieList.add(new GoodsBogie("Cylindrical", "Petroleum"));
        goodsBogieList.add(new GoodsBogie("Rectangular", "Coal"));
        goodsBogieList.add(new GoodsBogie("Cylindrical", "Petroleum"));

        boolean isSafetyCompliant = goodsBogieList.stream()
                .allMatch(b -> !b.type.equals("Cylindrical") || b.cargo.equals("Petroleum"));

        goodsBogieList.forEach(b -> System.out.println("  " + b));

        System.out.println(isSafetyCompliant ? "\nSafety Compliance Status: SAFE"
                : "\nSafety Compliance Status: UNSAFE");

        System.out.println("\n=== UC15: Safe Cargo Assignment ===");

        GoodsBogie cylindricalBogie = new GoodsBogie("Cylindrical", "Empty");
        GoodsBogie rectangularBogie = new GoodsBogie("Rectangular", "Empty");

        cylindricalBogie.assignCargo("Petroleum");
        rectangularBogie.assignCargo("Petroleum");
        rectangularBogie.assignCargo("Coal");

        System.out.println("  " + cylindricalBogie);
        System.out.println("  " + rectangularBogie);
    }
}
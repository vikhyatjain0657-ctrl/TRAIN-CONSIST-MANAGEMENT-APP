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

    @Override
    public String toString() {
        return "Type: " + type + " | Cargo: " + cargo;
    }
}

public class TrainConsistManagementApp {

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

        System.out.println("\n=== UC12: Safety Compliance Check for Goods Bogies ===");

        List<GoodsBogie> goodsBogieList = new ArrayList<>();

        goodsBogieList.add(new GoodsBogie("Cylindrical", "Petroleum"));
        goodsBogieList.add(new GoodsBogie("Rectangular", "Coal"));
        goodsBogieList.add(new GoodsBogie("Cylindrical", "Petroleum"));

        boolean isSafetyCompliant = goodsBogieList.stream()
                .allMatch(b -> !b.type.equals("Cylindrical") || b.cargo.equals("Petroleum"));

        System.out.println("\nGoods Bogies:");
        goodsBogieList.forEach(b -> System.out.println("  " + b));

        if (isSafetyCompliant) {
            System.out.println("\nSafety Compliance Status: SAFE");
        } else {
            System.out.println("\nSafety Compliance Status: UNSAFE - Rule Violation Detected");
        }

        System.out.println("\n=== UC13: Performance Comparison (Loops vs Streams) ===");

        List<Bogie> largeBogieList = new ArrayList<>();
        try {
            for (int i = 0; i < 100000; i++) {
                largeBogieList.add(new Bogie("Sleeper", 50 + (i % 50)));
            }
        } catch (InvalidCapacityException e) {
            System.out.println("Error: " + e.getMessage());
        }

        long loopStart = System.nanoTime();
        List<Bogie> loopResult = new ArrayList<>();
        for (Bogie b : largeBogieList) {
            if (b.capacity > 60) {
                loopResult.add(b);
            }
        }
        long loopEnd = System.nanoTime();
        long loopElapsed = loopEnd - loopStart;

        long streamStart = System.nanoTime();
        List<Bogie> streamResult = largeBogieList.stream()
                .filter(b -> b.capacity > 60)
                .collect(Collectors.toList());
        long streamEnd = System.nanoTime();
        long streamElapsed = streamEnd - streamStart;

        System.out.println("\nLoop-based filtering time  : " + loopElapsed + " ns");
        System.out.println("Stream-based filtering time: " + streamElapsed + " ns");
        System.out.println("Loop result count          : " + loopResult.size());
        System.out.println("Stream result count        : " + streamResult.size());

        System.out.println("\n=== UC14: Handle Invalid Bogie Capacity (Custom Exception) ===");

        try {
            Bogie validBogie = new Bogie("Sleeper", 72);
            System.out.println("Created bogie: " + validBogie);
        } catch (InvalidCapacityException e) {
            System.out.println("Error: " + e.getMessage());
        }

        try {
            Bogie invalidBogie = new Bogie("Sleeper", -10);
            System.out.println("Created bogie: " + invalidBogie);
        } catch (InvalidCapacityException e) {
            System.out.println("Caught Exception -> " + e.getMessage());
        }

        try {
            Bogie zeroBogie = new Bogie("AC Chair", 0);
            System.out.println("Created bogie: " + zeroBogie);
        } catch (InvalidCapacityException e) {
            System.out.println("Caught Exception -> " + e.getMessage());
        }
    }
}
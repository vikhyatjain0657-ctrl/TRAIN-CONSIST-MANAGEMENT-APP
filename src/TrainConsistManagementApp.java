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
                throw new CargoSafetyException("Unsafe assignment: Petroleum cannot be assigned to a Rectangular bogie");
            }
            this.cargo = newCargo;
            System.out.println("Cargo assigned successfully: " + newCargo + " -> " + this.type);
        } catch (CargoSafetyException e) {
            System.out.println("Cargo assignment failed: " + e.getMessage());
        } finally {
            System.out.println("Cargo assignment validation completed for bogie type: " + this.type);
        }
    }

    public String toString() {
        return "Type: " + type + " | Cargo: " + cargo;
    }
}

public class TrainConsistManagementApp {

    public static String[] sortBogieNames(String[] names) {
        java.util.Arrays.sort(names);
        return names;
    }

    public static boolean linearSearch(String[] ids, String key) {
        for (String id : ids) {
            if (id.equals(key)) {
                return true;
            }
        }
        return false;
    }

    public static boolean binarySearch(String[] ids, String key) {
        if (ids.length == 0) return false;

        java.util.Arrays.sort(ids);

        int low = 0;
        int high = ids.length - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            int cmp = ids[mid].compareTo(key);

            if (cmp == 0) return true;
            else if (cmp < 0) low = mid + 1;
            else high = mid - 1;
        }

        return false;
    }

    public static boolean validatedSearch(String[] ids, String key) {
        if (ids == null || ids.length == 0) {
            throw new IllegalStateException("Search operation failed: No bogies available in the train");
        }
        return binarySearch(ids, key);
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
        bogieList.forEach(System.out::println);

        String[] bogieNames = new String[bogieList.size()];
        for (int i = 0; i < bogieList.size(); i++) {
            bogieNames[i] = bogieList.get(i).name;
        }

        java.util.Arrays.sort(bogieNames);
        System.out.println(java.util.Arrays.toString(bogieNames));

        String[] bogieIds = {"BG101","BG205","BG309","BG412","BG550"};

        try {
            boolean result = validatedSearch(bogieIds, "BG309");
            System.out.println(result ? "Bogie Found" : "Bogie Not Found");
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        List<Bogie> filteredBogies = bogieList.stream()
                .filter(b -> b.capacity > 60)
                .collect(Collectors.toList());

        filteredBogies.forEach(System.out::println);

        Map<String, List<Bogie>> groupedBogies = bogieList.stream()
                .collect(Collectors.groupingBy(b -> b.name));

        groupedBogies.forEach((k,v) -> {
            System.out.println("\n" + k);
            v.forEach(System.out::println);
        });

        int totalSeats = bogieList.stream()
                .map(b -> b.capacity)
                .reduce(0, Integer::sum);

        System.out.println("\nTotal Seats: " + totalSeats);

        Pattern pattern = Pattern.compile("TRN-\\d{4}");
        Matcher matcher = pattern.matcher("TRN-1234");
        System.out.println("\nTrain ID Valid: " + matcher.matches());
    }
}
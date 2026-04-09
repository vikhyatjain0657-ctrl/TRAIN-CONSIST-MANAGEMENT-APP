import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;

class TrainConsistManagementAppTest {

    // Helper method to filter bogies by capacity threshold
    private List<Bogie> filterBogies(List<Bogie> bogieList, int threshold) {
        return bogieList.stream()
                .filter(b -> b.capacity > threshold)
                .collect(Collectors.toList());
    }

    // Test Case 1: Bogies with capacity greater than threshold should be returned
    @Test
    void testFilter_CapacityGreaterThanThreshold() {
        List<Bogie> bogieList = new ArrayList<>();
        bogieList.add(new Bogie("Sleeper", 72));
        bogieList.add(new Bogie("AC Chair", 56));
        bogieList.add(new Bogie("First Class", 24));

        List<Bogie> result = filterBogies(bogieList, 60);

        assertEquals(1, result.size());
        assertEquals("Sleeper", result.get(0).name);
        assertEquals(72, result.get(0).capacity);
    }

    // Test Case 2: Bogies with capacity equal to threshold should NOT be included
    @Test
    void testFilter_CapacityEqualToThreshold() {
        List<Bogie> bogieList = new ArrayList<>();
        bogieList.add(new Bogie("Sleeper", 72));
        bogieList.add(new Bogie("AC Chair", 70));  // exactly at threshold
        bogieList.add(new Bogie("First Class", 24));

        List<Bogie> result = filterBogies(bogieList, 70);

        assertEquals(1, result.size());
        assertEquals("Sleeper", result.get(0).name); // only 72 passes
    }

    // Test Case 3: Multiple bogies matching the condition should all be returned
    @Test
    void testFilter_MultipleBogiesMatching() {
        List<Bogie> bogieList = new ArrayList<>();
        bogieList.add(new Bogie("Sleeper", 72));
        bogieList.add(new Bogie("AC Chair", 80));
        bogieList.add(new Bogie("First Class", 24));

        List<Bogie> result = filterBogies(bogieList, 60);

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(b -> b.name.equals("Sleeper")));
        assertTrue(result.stream().anyMatch(b -> b.name.equals("AC Chair")));
    }

    // Test Case 4: No bogies matching should return an empty list
    @Test
    void testFilter_NoBogiesMatching() {
        List<Bogie> bogieList = new ArrayList<>();
        bogieList.add(new Bogie("Sleeper", 72));
        bogieList.add(new Bogie("AC Chair", 56));
        bogieList.add(new Bogie("First Class", 24));

        List<Bogie> result = filterBogies(bogieList, 100); // threshold higher than all

        assertTrue(result.isEmpty());
    }

    // Test Case 5: Original list must remain unchanged after stream filtering
    @Test
    void testFilter_OriginalListUnchanged() {
        List<Bogie> bogieList = new ArrayList<>();
        bogieList.add(new Bogie("Sleeper", 72));
        bogieList.add(new Bogie("AC Chair", 56));
        bogieList.add(new Bogie("First Class", 24));

        int originalSize = bogieList.size();
        filterBogies(bogieList, 60); // perform filtering

        assertEquals(originalSize, bogieList.size());         // size unchanged
        assertEquals("Sleeper",     bogieList.get(0).name);   // order unchanged (after sort)
        assertEquals("AC Chair",    bogieList.get(1).name);
        assertEquals("First Class", bogieList.get(2).name);
    }
}
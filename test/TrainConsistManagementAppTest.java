import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;

class TrainConsistManagementAppTest {

    private Map<String, List<Bogie>> groupBogies(List<Bogie> bogieList) {
        return bogieList.stream()
                .collect(Collectors.groupingBy(b -> b.name));
    }

    @Test
    void testGrouping_BogiesGroupedByType() {
        List<Bogie> bogieList = new ArrayList<>();
        bogieList.add(new Bogie("Sleeper", 72));
        bogieList.add(new Bogie("AC Chair", 56));
        bogieList.add(new Bogie("First Class", 24));

        Map<String, List<Bogie>> result = groupBogies(bogieList);

        assertTrue(result.containsKey("Sleeper"));
        assertTrue(result.containsKey("AC Chair"));
        assertTrue(result.containsKey("First Class"));
    }

    @Test
    void testGrouping_MultipleBogiesInSameGroup() {
        List<Bogie> bogieList = new ArrayList<>();
        bogieList.add(new Bogie("Sleeper", 72));
        bogieList.add(new Bogie("Sleeper", 68));
        bogieList.add(new Bogie("AC Chair", 56));

        Map<String, List<Bogie>> result = groupBogies(bogieList);

        assertEquals(2, result.get("Sleeper").size());
        assertEquals(1, result.get("AC Chair").size());
    }

    @Test
    void testGrouping_DifferentBogieTypes() {
        List<Bogie> bogieList = new ArrayList<>();
        bogieList.add(new Bogie("Sleeper", 72));
        bogieList.add(new Bogie("AC Chair", 56));
        bogieList.add(new Bogie("First Class", 24));

        Map<String, List<Bogie>> result = groupBogies(bogieList);

        assertEquals(3, result.size());
        assertNotEquals(result.get("Sleeper"), result.get("AC Chair"));
    }

    @Test
    void testGrouping_EmptyBogieList() {
        List<Bogie> bogieList = new ArrayList<>();

        Map<String, List<Bogie>> result = groupBogies(bogieList);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGrouping_MapContainsCorrectKeys() {
        List<Bogie> bogieList = new ArrayList<>();
        bogieList.add(new Bogie("Sleeper", 72));
        bogieList.add(new Bogie("AC Chair", 56));
        bogieList.add(new Bogie("First Class", 24));

        Map<String, List<Bogie>> result = groupBogies(bogieList);

        assertTrue(result.keySet().containsAll(
                List.of("Sleeper", "AC Chair", "First Class")));
    }

    @Test
    void testGrouping_GroupSizeValidation() {
        List<Bogie> bogieList = new ArrayList<>();
        bogieList.add(new Bogie("Sleeper", 72));
        bogieList.add(new Bogie("Sleeper", 68));
        bogieList.add(new Bogie("AC Chair", 56));
        bogieList.add(new Bogie("AC Chair", 60));
        bogieList.add(new Bogie("First Class", 24));

        Map<String, List<Bogie>> result = groupBogies(bogieList);

        assertEquals(2, result.get("Sleeper").size());
        assertEquals(2, result.get("AC Chair").size());
        assertEquals(1, result.get("First Class").size());
    }

    @Test
    void testGrouping_SingleBogieCategory() {
        List<Bogie> bogieList = new ArrayList<>();
        bogieList.add(new Bogie("Sleeper", 72));
        bogieList.add(new Bogie("Sleeper", 68));
        bogieList.add(new Bogie("Sleeper", 60));

        Map<String, List<Bogie>> result = groupBogies(bogieList);

        assertEquals(1, result.size());
        assertEquals(3, result.get("Sleeper").size());
    }

    @Test
    void testGrouping_OriginalListUnchanged() {
        List<Bogie> bogieList = new ArrayList<>();
        bogieList.add(new Bogie("Sleeper", 72));
        bogieList.add(new Bogie("AC Chair", 56));
        bogieList.add(new Bogie("First Class", 24));

        int originalSize = bogieList.size();
        groupBogies(bogieList);

        assertEquals(originalSize, bogieList.size());
        assertEquals("Sleeper",     bogieList.get(0).name);
        assertEquals("AC Chair",    bogieList.get(1).name);
        assertEquals("First Class", bogieList.get(2).name);
    }
}
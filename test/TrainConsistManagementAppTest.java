import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import static org.junit.jupiter.api.Assertions.*;

class TrainConsistManagementAppTest {

    private Map<String, List<Bogie>> groupBogies(List<Bogie> bogieList) {
        return bogieList.stream()
                .collect(Collectors.groupingBy(b -> b.name));
    }

    private int getTotalSeats(List<Bogie> bogieList) {
        return bogieList.stream()
                .map(b -> b.capacity)
                .reduce(0, Integer::sum);
    }

    private boolean validateTrainId(String input) {
        Pattern pattern = Pattern.compile("TRN-\\d{4}");
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    private boolean validateCargoCode(String input) {
        Pattern pattern = Pattern.compile("PET-[A-Z]{2}");
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    private boolean isSafetyCompliant(List<GoodsBogie> goodsBogieList) {
        return goodsBogieList.stream()
                .allMatch(b -> !b.type.equals("Cylindrical") || b.cargo.equals("Petroleum"));
    }

    private List<Bogie> filterByLoop(List<Bogie> bogieList, int threshold) {
        List<Bogie> result = new ArrayList<>();
        for (Bogie b : bogieList) {
            if (b.capacity > threshold) {
                result.add(b);
            }
        }
        return result;
    }

    private List<Bogie> filterByStream(List<Bogie> bogieList, int threshold) {
        return bogieList.stream()
                .filter(b -> b.capacity > threshold)
                .collect(Collectors.toList());
    }

    @Test
    void testGrouping_BogiesGroupedByType() throws InvalidCapacityException {
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
    void testGrouping_MultipleBogiesInSameGroup() throws InvalidCapacityException {
        List<Bogie> bogieList = new ArrayList<>();
        bogieList.add(new Bogie("Sleeper", 72));
        bogieList.add(new Bogie("Sleeper", 68));
        bogieList.add(new Bogie("AC Chair", 56));

        Map<String, List<Bogie>> result = groupBogies(bogieList);

        assertEquals(2, result.get("Sleeper").size());
        assertEquals(1, result.get("AC Chair").size());
    }

    @Test
    void testGrouping_DifferentBogieTypes() throws InvalidCapacityException {
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
    void testGrouping_MapContainsCorrectKeys() throws InvalidCapacityException {
        List<Bogie> bogieList = new ArrayList<>();
        bogieList.add(new Bogie("Sleeper", 72));
        bogieList.add(new Bogie("AC Chair", 56));
        bogieList.add(new Bogie("First Class", 24));

        Map<String, List<Bogie>> result = groupBogies(bogieList);

        assertTrue(result.keySet().containsAll(
                List.of("Sleeper", "AC Chair", "First Class")));
    }

    @Test
    void testGrouping_GroupSizeValidation() throws InvalidCapacityException {
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
    void testGrouping_SingleBogieCategory() throws InvalidCapacityException {
        List<Bogie> bogieList = new ArrayList<>();
        bogieList.add(new Bogie("Sleeper", 72));
        bogieList.add(new Bogie("Sleeper", 68));
        bogieList.add(new Bogie("Sleeper", 60));

        Map<String, List<Bogie>> result = groupBogies(bogieList);

        assertEquals(1, result.size());
        assertEquals(3, result.get("Sleeper").size());
    }

    @Test
    void testGrouping_OriginalListUnchanged() throws InvalidCapacityException {
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

    @Test
    void testReduce_TotalSeatCalculation() throws InvalidCapacityException {
        List<Bogie> bogieList = new ArrayList<>();
        bogieList.add(new Bogie("Sleeper", 72));
        bogieList.add(new Bogie("AC Chair", 56));
        bogieList.add(new Bogie("First Class", 24));

        assertEquals(152, getTotalSeats(bogieList));
    }

    @Test
    void testReduce_MultipleBogiesAggregation() throws InvalidCapacityException {
        List<Bogie> bogieList = new ArrayList<>();
        bogieList.add(new Bogie("Sleeper", 72));
        bogieList.add(new Bogie("Sleeper", 68));
        bogieList.add(new Bogie("AC Chair", 56));
        bogieList.add(new Bogie("AC Chair", 60));
        bogieList.add(new Bogie("First Class", 24));

        assertEquals(280, getTotalSeats(bogieList));
    }

    @Test
    void testReduce_SingleBogieCapacity() throws InvalidCapacityException {
        List<Bogie> bogieList = new ArrayList<>();
        bogieList.add(new Bogie("Sleeper", 72));

        assertEquals(72, getTotalSeats(bogieList));
    }

    @Test
    void testReduce_EmptyBogieList() {
        List<Bogie> bogieList = new ArrayList<>();

        assertEquals(0, getTotalSeats(bogieList));
    }

    @Test
    void testReduce_CorrectCapacityExtraction() throws InvalidCapacityException {
        List<Bogie> bogieList = new ArrayList<>();
        bogieList.add(new Bogie("Sleeper", 72));
        bogieList.add(new Bogie("AC Chair", 56));

        List<Integer> capacities = bogieList.stream()
                .map(b -> b.capacity)
                .collect(Collectors.toList());

        assertEquals(2, capacities.size());
        assertEquals(72, capacities.get(0));
        assertEquals(56, capacities.get(1));
    }

    @Test
    void testReduce_AllBogiesIncluded() throws InvalidCapacityException {
        List<Bogie> bogieList = new ArrayList<>();
        bogieList.add(new Bogie("Sleeper", 72));
        bogieList.add(new Bogie("AC Chair", 56));
        bogieList.add(new Bogie("First Class", 24));

        assertEquals(72 + 56 + 24, getTotalSeats(bogieList));
    }

    @Test
    void testReduce_OriginalListUnchanged() throws InvalidCapacityException {
        List<Bogie> bogieList = new ArrayList<>();
        bogieList.add(new Bogie("Sleeper", 72));
        bogieList.add(new Bogie("AC Chair", 56));
        bogieList.add(new Bogie("First Class", 24));

        int originalSize = bogieList.size();
        getTotalSeats(bogieList);

        assertEquals(originalSize, bogieList.size());
        assertEquals("Sleeper",     bogieList.get(0).name);
        assertEquals("AC Chair",    bogieList.get(1).name);
        assertEquals("First Class", bogieList.get(2).name);
    }

    @Test
    void testRegex_ValidTrainID() {
        assertTrue(validateTrainId("TRN-1234"));
    }

    @Test
    void testRegex_InvalidTrainIDFormat() {
        assertFalse(validateTrainId("TRAIN12"));
        assertFalse(validateTrainId("TRN12A"));
        assertFalse(validateTrainId("1234-TRN"));
    }

    @Test
    void testRegex_ValidCargoCode() {
        assertTrue(validateCargoCode("PET-AB"));
    }

    @Test
    void testRegex_InvalidCargoCodeFormat() {
        assertFalse(validateCargoCode("PET-ab"));
        assertFalse(validateCargoCode("PET123"));
        assertFalse(validateCargoCode("AB-PET"));
    }

    @Test
    void testRegex_TrainIDDigitLengthValidation() {
        assertFalse(validateTrainId("TRN-123"));
        assertFalse(validateTrainId("TRN-12345"));
    }

    @Test
    void testRegex_CargoCodeUppercaseValidation() {
        assertFalse(validateCargoCode("PET-ab"));
        assertFalse(validateCargoCode("PET-Ab"));
        assertFalse(validateCargoCode("PET-aB"));
    }

    @Test
    void testRegex_EmptyInputHandling() {
        assertFalse(validateTrainId(""));
        assertFalse(validateCargoCode(""));
    }

    @Test
    void testRegex_ExactPatternMatch() {
        assertFalse(validateTrainId("TRN-1234X"));
        assertFalse(validateTrainId("XTRN-1234"));
        assertFalse(validateCargoCode("PET-ABC"));
        assertFalse(validateCargoCode("XPET-AB"));
    }

    @Test
    void testSafety_AllBogiesValid() {
        List<GoodsBogie> goodsBogieList = new ArrayList<>();
        goodsBogieList.add(new GoodsBogie("Cylindrical", "Petroleum"));
        goodsBogieList.add(new GoodsBogie("Cylindrical", "Petroleum"));
        goodsBogieList.add(new GoodsBogie("Rectangular", "Coal"));

        assertTrue(isSafetyCompliant(goodsBogieList));
    }

    @Test
    void testSafety_CylindricalWithInvalidCargo() {
        List<GoodsBogie> goodsBogieList = new ArrayList<>();
        goodsBogieList.add(new GoodsBogie("Cylindrical", "Coal"));
        goodsBogieList.add(new GoodsBogie("Rectangular", "Grain"));

        assertFalse(isSafetyCompliant(goodsBogieList));
    }

    @Test
    void testSafety_NonCylindricalBogiesAllowed() {
        List<GoodsBogie> goodsBogieList = new ArrayList<>();
        goodsBogieList.add(new GoodsBogie("Rectangular", "Coal"));
        goodsBogieList.add(new GoodsBogie("Rectangular", "Grain"));
        goodsBogieList.add(new GoodsBogie("Rectangular", "Petroleum"));

        assertTrue(isSafetyCompliant(goodsBogieList));
    }

    @Test
    void testSafety_MixedBogiesWithViolation() {
        List<GoodsBogie> goodsBogieList = new ArrayList<>();
        goodsBogieList.add(new GoodsBogie("Cylindrical", "Petroleum"));
        goodsBogieList.add(new GoodsBogie("Cylindrical", "Coal"));
        goodsBogieList.add(new GoodsBogie("Rectangular", "Grain"));

        assertFalse(isSafetyCompliant(goodsBogieList));
    }

    @Test
    void testSafety_EmptyBogieList() {
        List<GoodsBogie> goodsBogieList = new ArrayList<>();

        assertTrue(isSafetyCompliant(goodsBogieList));
    }

    @Test
    void testLoopFilteringLogic() throws InvalidCapacityException {
        List<Bogie> bogieList = new ArrayList<>();
        bogieList.add(new Bogie("Sleeper", 72));
        bogieList.add(new Bogie("AC Chair", 56));
        bogieList.add(new Bogie("First Class", 24));
        bogieList.add(new Bogie("Sleeper", 68));

        List<Bogie> result = filterByLoop(bogieList, 60);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(b -> b.capacity > 60));
    }

    @Test
    void testStreamFilteringLogic() throws InvalidCapacityException {
        List<Bogie> bogieList = new ArrayList<>();
        bogieList.add(new Bogie("Sleeper", 72));
        bogieList.add(new Bogie("AC Chair", 56));
        bogieList.add(new Bogie("First Class", 24));
        bogieList.add(new Bogie("Sleeper", 68));

        List<Bogie> result = filterByStream(bogieList, 60);

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(b -> b.capacity > 60));
    }

    @Test
    void testLoopAndStreamResultsMatch() throws InvalidCapacityException {
        List<Bogie> bogieList = new ArrayList<>();
        bogieList.add(new Bogie("Sleeper", 72));
        bogieList.add(new Bogie("AC Chair", 56));
        bogieList.add(new Bogie("First Class", 24));
        bogieList.add(new Bogie("Sleeper", 68));
        bogieList.add(new Bogie("AC Chair", 60));

        List<Bogie> loopResult   = filterByLoop(bogieList, 60);
        List<Bogie> streamResult = filterByStream(bogieList, 60);

        assertEquals(loopResult.size(), streamResult.size());
    }

    @Test
    void testExecutionTimeMeasurement() throws InvalidCapacityException {
        List<Bogie> bogieList = new ArrayList<>();
        bogieList.add(new Bogie("Sleeper", 72));
        bogieList.add(new Bogie("AC Chair", 56));
        bogieList.add(new Bogie("First Class", 24));

        long start = System.nanoTime();
        filterByLoop(bogieList, 60);
        long end = System.nanoTime();

        assertTrue((end - start) > 0);
    }

    @Test
    void testLargeDatasetProcessing() throws InvalidCapacityException {
        List<Bogie> largeBogieList = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            largeBogieList.add(new Bogie("Sleeper", 50 + (i % 50)));
        }

        List<Bogie> loopResult   = filterByLoop(largeBogieList, 60);
        List<Bogie> streamResult = filterByStream(largeBogieList, 60);

        assertEquals(loopResult.size(), streamResult.size());
        assertTrue(loopResult.stream().allMatch(b -> b.capacity > 60));
        assertTrue(streamResult.stream().allMatch(b -> b.capacity > 60));
    }

    @Test
    void testException_ValidCapacityCreation() throws InvalidCapacityException {
        Bogie bogie = new Bogie("Sleeper", 72);

        assertNotNull(bogie);
        assertEquals("Sleeper", bogie.name);
        assertEquals(72, bogie.capacity);
    }

    @Test
    void testException_NegativeCapacityThrowsException() {
        assertThrows(InvalidCapacityException.class,
                () -> new Bogie("Sleeper", -10));
    }

    @Test
    void testException_ZeroCapacityThrowsException() {
        assertThrows(InvalidCapacityException.class,
                () -> new Bogie("AC Chair", 0));
    }

    @Test
    void testException_ExceptionMessageValidation() {
        InvalidCapacityException exception = assertThrows(
                InvalidCapacityException.class,
                () -> new Bogie("Sleeper", -5));

        assertEquals("Capacity must be greater than zero", exception.getMessage());
    }

    @Test
    void testException_ObjectIntegrityAfterCreation() throws InvalidCapacityException {
        Bogie bogie = new Bogie("First Class", 48);

        assertEquals("First Class", bogie.name);
        assertEquals(48, bogie.capacity);
    }

    @Test
    void testException_MultipleValidBogiesCreation() throws InvalidCapacityException {
        Bogie sleeper    = new Bogie("Sleeper", 72);
        Bogie acChair    = new Bogie("AC Chair", 56);
        Bogie firstClass = new Bogie("First Class", 24);

        assertNotNull(sleeper);
        assertNotNull(acChair);
        assertNotNull(firstClass);
        assertEquals(72, sleeper.capacity);
        assertEquals(56, acChair.capacity);
        assertEquals(24, firstClass.capacity);
    }

    @Test
    void testCargo_SafeAssignment() {
        GoodsBogie bogie = new GoodsBogie("Cylindrical", "Empty");

        bogie.assignCargo("Petroleum");

        assertEquals("Petroleum", bogie.cargo);
    }

    @Test
    void testCargo_UnsafeAssignmentHandled() {
        GoodsBogie bogie = new GoodsBogie("Rectangular", "Empty");

        assertDoesNotThrow(() -> bogie.assignCargo("Petroleum"));
    }

    @Test
    void testCargo_CargoNotAssignedAfterFailure() {
        GoodsBogie bogie = new GoodsBogie("Rectangular", "Empty");

        bogie.assignCargo("Petroleum");

        assertNotEquals("Petroleum", bogie.cargo);
        assertEquals("Empty", bogie.cargo);
    }

    @Test
    void testCargo_ProgramContinuesAfterException() {
        GoodsBogie rectangular = new GoodsBogie("Rectangular", "Empty");
        GoodsBogie cylindrical = new GoodsBogie("Cylindrical", "Empty");

        rectangular.assignCargo("Petroleum");
        cylindrical.assignCargo("Petroleum");

        assertEquals("Empty",     rectangular.cargo);
        assertEquals("Petroleum", cylindrical.cargo);
    }

    @Test
    void testCargo_FinallyBlockExecution() {
        GoodsBogie bogie = new GoodsBogie("Rectangular", "Empty");
        boolean[] finallyExecuted = {false};

        try {
            if (bogie.type.equals("Rectangular") && "Petroleum".equals("Petroleum")) {
                throw new CargoSafetyException(
                        "Unsafe assignment: Petroleum cannot be assigned to a Rectangular bogie");
            }
            bogie.cargo = "Petroleum";
        } catch (CargoSafetyException e) {
            assertNotNull(e.getMessage());
        } finally {
            finallyExecuted[0] = true;
        }

        assertTrue(finallyExecuted[0]);
    }
}
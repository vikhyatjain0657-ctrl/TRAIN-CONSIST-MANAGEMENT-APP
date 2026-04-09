import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TrainConsistManagementAppTest {

    @Test
    void testBinarySearch_BogieFound() {
        String[] ids = {"BG101","BG205","BG309","BG412","BG550"};
        assertTrue(TrainConsistManagementApp.binarySearch(ids, "BG309"));
    }

    @Test
    void testBinarySearch_BogieNotFound() {
        String[] ids = {"BG101","BG205","BG309","BG412","BG550"};
        assertFalse(TrainConsistManagementApp.binarySearch(ids, "BG999"));
    }

    @Test
    void testBinarySearch_FirstElementMatch() {
        String[] ids = {"BG101","BG205","BG309","BG412","BG550"};
        assertTrue(TrainConsistManagementApp.binarySearch(ids, "BG101"));
    }

    @Test
    void testBinarySearch_LastElementMatch() {
        String[] ids = {"BG101","BG205","BG309","BG412","BG550"};
        assertTrue(TrainConsistManagementApp.binarySearch(ids, "BG550"));
    }

    @Test
    void testBinarySearch_SingleElementArray() {
        String[] ids = {"BG101"};
        assertTrue(TrainConsistManagementApp.binarySearch(ids, "BG101"));
    }

    @Test
    void testBinarySearch_EmptyArray() {
        String[] ids = {};
        assertFalse(TrainConsistManagementApp.binarySearch(ids, "BG101"));
    }

    @Test
    void testBinarySearch_UnsortedInputHandled() {
        String[] ids = {"BG309","BG101","BG550","BG205","BG412"};
        assertTrue(TrainConsistManagementApp.binarySearch(ids, "BG205"));
    }
}
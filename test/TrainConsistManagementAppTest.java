import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TrainConsistManagementAppTest {

    @Test
    void testSearch_BogieFound() {
        String[] ids = {"BG101","BG205","BG309","BG412","BG550"};
        assertTrue(TrainConsistManagementApp.linearSearch(ids, "BG309"));
    }

    @Test
    void testSearch_BogieNotFound() {
        String[] ids = {"BG101","BG205","BG309","BG412","BG550"};
        assertFalse(TrainConsistManagementApp.linearSearch(ids, "BG999"));
    }

    @Test
    void testSearch_FirstElementMatch() {
        String[] ids = {"BG101","BG205","BG309","BG412","BG550"};
        assertTrue(TrainConsistManagementApp.linearSearch(ids, "BG101"));
    }

    @Test
    void testSearch_LastElementMatch() {
        String[] ids = {"BG101","BG205","BG309","BG412","BG550"};
        assertTrue(TrainConsistManagementApp.linearSearch(ids, "BG550"));
    }

    @Test
    void testSearch_SingleElementArray() {
        String[] ids = {"BG101"};
        assertTrue(TrainConsistManagementApp.linearSearch(ids, "BG101"));
    }
}
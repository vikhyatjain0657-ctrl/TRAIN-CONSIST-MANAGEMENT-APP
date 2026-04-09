import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TrainConsistManagementAppTest {

    @Test
    void testSearch_ThrowsExceptionWhenEmpty() {
        String[] ids = {};
        assertThrows(IllegalStateException.class,
                () -> TrainConsistManagementApp.validatedSearch(ids, "BG101"));
    }

    @Test
    void testSearch_AllowsSearchWhenDataExists() {
        String[] ids = {"BG101","BG205"};
        assertDoesNotThrow(() ->
                TrainConsistManagementApp.validatedSearch(ids, "BG101"));
    }

    @Test
    void testSearch_BogieFoundAfterValidation() {
        String[] ids = {"BG101","BG205","BG309"};
        assertTrue(TrainConsistManagementApp.validatedSearch(ids, "BG205"));
    }

    @Test
    void testSearch_BogieNotFoundAfterValidation() {
        String[] ids = {"BG101","BG205","BG309"};
        assertFalse(TrainConsistManagementApp.validatedSearch(ids, "BG999"));
    }

    @Test
    void testSearch_SingleElementValidCase() {
        String[] ids = {"BG101"};
        assertTrue(TrainConsistManagementApp.validatedSearch(ids, "BG101"));
    }
}
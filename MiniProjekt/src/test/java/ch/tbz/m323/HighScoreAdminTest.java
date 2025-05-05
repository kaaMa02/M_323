package ch.tbz.m323;

import m323.tbz.ch.HighScore;
import m323.tbz.ch.HighScoreAdmin;
import org.junit.jupiter.api.*;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

class HighScoreAdminTest {

    private HighScoreAdmin admin;
    private Path tempFile;

    @BeforeEach
    void setUp() throws Exception {
        admin = new HighScoreAdmin();
        tempFile = Files.createTempFile("highscores_test", ".csv");
    }

    @AfterEach
    void tearDown() throws Exception {
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testSaveAndReload() throws Exception {
        HighScore score = new HighScore("Carol", "2025-01-01", "schwer", 120);
        admin.insert(score);
        admin.saveToCsv(tempFile.toString());

        HighScoreAdmin reloaded = new HighScoreAdmin();
        reloaded.loadFromCsv(tempFile.toString());

        String reloadedOutput = reloaded.getHighScores("Schwer");

        assertTrue(reloadedOutput.contains("Carol"), "Carol should appear in reloaded output");
        assertTrue(reloadedOutput.contains("120"), "Time 120 should appear in reloaded output");

    }
}

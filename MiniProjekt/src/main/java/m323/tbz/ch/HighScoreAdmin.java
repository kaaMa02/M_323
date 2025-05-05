package m323.tbz.ch;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Verwaltet Highscore-Einträge: Hinzufügen, Speichern und Laden.
 */
public class HighScoreAdmin {
    private static final List<String> LEVELS = Arrays.asList("einfach", "mittel", "schwer", "genie");
    private static final int MAX_ENTRIES = 10;
    private static final String FILE_NAME = "highscores.csv";
    private Map<String, List<HighScore>> scores = new HashMap<>();

    public HighScoreAdmin() {
        for (String level : LEVELS) {
            scores.put(level, new ArrayList<>());
        }
        loadScores();
    }

    public String insert(HighScore score) {
        String level = score.getLevel();
        if (!LEVELS.contains(level)) return "Invalid level.";

        List<HighScore> list = scores.get(level);
        list.add(score);
        list.sort(Comparator.comparingInt(HighScore::getTime));

        if (list.size() > MAX_ENTRIES) {
            list.remove(list.size() - 1);
        }

        int rank = list.indexOf(score) + 1;
        if (rank > MAX_ENTRIES) {
            return "HighScore entries only better than " + list.get(MAX_ENTRIES - 1).getTime() + " seconds.";
        }

        return "Your rank: " + rank + "!";
    }

    public void saveToCsv(String path) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path))) {
            writer.write("name,date,level,time\n");
            for (String level : LEVELS) {
                for (HighScore score : scores.get(level)) {
                    writer.write(String.format("%s,%s,%s,%d\n",
                            score.getName(),
                            score.getDate(),
                            score.getLevel(),
                            score.getTime()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromCsv(String path) {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(path))) {
            scores.clear();
            for (String level : LEVELS) {
                scores.put(level, new ArrayList<>());
            }

            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 4) continue;
                HighScore score = new HighScore(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
                insert(score);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fügt einen neuen Highscore hinzu und gibt den Rang des Spielers zurück.
     * @param name Name des Spielers
     * @param level Schwierigkeitsgrad
     * @param time Benötigte Zeit in Sekunden
     * @return Rangnachricht oder Fehlermeldung
     */
    public String addScore(String name, String level, int time) {
        level = level.toLowerCase();
        if (!scores.containsKey(level)) return "Invalid level!";

        String date = new Date().toString();
        HighScore newScore = new HighScore(name, date, level, time);
        List<HighScore> levelScores = scores.get(level);
        levelScores.add(newScore);

        int rank = calculateRank(levelScores, newScore);

        List<HighScore> sortedLevelScores = levelScores.stream()
                .sorted(Comparator.comparingInt(s -> s.getTime()))
                .limit(MAX_ENTRIES)
                .toList();

        return rank <= MAX_ENTRIES ? "Your rank: " + rank + "!" : "m323.tbz.ch.HighScore entries only better than " + sortedLevelScores.get(MAX_ENTRIES - 1).getTime() + " seconds";
    }

    private int calculateRank(List<HighScore> scores, HighScore newScore) {
        List<HighScore> sorted = new ArrayList<>(scores);
        sorted.add(newScore);
        sorted.sort(Comparator.comparingInt(HighScore::getTime));
        return sorted.indexOf(newScore) + 1;
    }

    public String getHighScores(String level) {
        level = level.toLowerCase();
        if (!scores.containsKey(level)) return "Invalid level!";

        // Filter the scores first
        List<HighScore> filteredScores = scores.get(level).stream()
                .filter(score -> score.getTime() <= 300)
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        printScoresRecursively(filteredScores, 0, sb);
        return sb.toString();
    }

    private void printScoresRecursively(List<HighScore> scores, int index, StringBuilder sb) {
        if (index >= scores.size()) return;
        sb.append(scores.get(index).toString()).append("\n");
        printScoresRecursively(scores, index + 1, sb);
    }


    public void saveScores() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            writer.println("Name,Date,Level,Time");
            for (String level : LEVELS) {
                for (HighScore hs : scores.get(level)) {
                    writer.println(hs.getName() + "," + hs.getDate() + "," + hs.getLevel() + "," + hs.getTime());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadScores() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            reader.readLine(); // Skip header
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    scores.get(parts[2]).add(new HighScore(parts[0], parts[1], parts[2], Integer.parseInt(parts[3])));
                }
            }
            for (String level : LEVELS) {
                scores.get(level).sort(Comparator.comparingInt(s -> s.getTime()));
                if (scores.get(level).size() > MAX_ENTRIES) {
                    scores.get(level).subList(MAX_ENTRIES, scores.get(level).size()).clear();
                }
            }
        } catch (IOException e) {
            System.out.println("Keine gespeicherten Highscores gefunden.");
        }
    }
}
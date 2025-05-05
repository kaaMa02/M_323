package m323.tbz.ch;

public class HighScore {
    private final String name;
    private final String date;
    private final String level;
    private final int time;

    public HighScore(String name, String date, String level, int time) {
        this.name = name;
        this.date = date;
        this.level = level;
        this.time = time;
    }

    @Override
    public String toString() {
        return name + " (" + date + ") - " + level + ": " + time + " sec";
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getLevel() {
        return level;
    }

    public int getTime() {
        return time;
    }
}
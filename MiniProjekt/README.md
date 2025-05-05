# MiniProjekt – HighScoreList

This project implements a reusable Java library and simple CLI for maintaining a Sudoku high‑score list.

## Requirements
- Java 17+
- Maven

## Build
```bash
mvn clean package
```

## Run
```bash
java -jar target/highscore-cli.jar
```

## Features
- Add a new score (with rank feedback)
- List top 10 scores by level (Einfach, Mittel, Schwer, Genie)
- Persist to/load from CSV (highscores.csv)

## Testing
```bash
mvn test
```

## Project Structure
```
MiniProjekt/
├── pom.xml
├── src
│   ├── main/java/ch/tbz/m323
│   │   ├── HighScore.java
│   │   ├── HighScoreAdmin.java
│   │   └── HighScoreApp.java
│   └── test/java/ch/tbz/m323
│       └── HighScoreAdminTest.java
└── README.md
```
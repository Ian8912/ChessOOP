# Object-Oriented Programming GUI Chess Project

<strong>Developed by:</strong> Ian Lingo, Pedro Perez, and Shaz Momin.

## Project Overview

This project showcases our understanding of <strong>Object-Oriented Programming (OOP)</strong> by building a fully
functional chess game using Java with a <strong>Swing-based GUI</strong>. <br>

## Development Notes

Originally built as a collaborative class project, I (Ian Lingo) have since taken the lead on expanding the application far beyond the original requirements. My additions include:

- Creating JavaDocs documentation.
- Adding additional GUI features such as player turn indicator and side bar.
- Migrating the project to a multi-module Gradle build (client + server).
- Adding unit testing (JUnit 5).
- Implementing a Spring Boot backend for leaderboards and results.
- Designing REST APIs and integrating Elo rating updates.
- Adding PostgreSQL support with Docker Compose for persistent storage.
- Integrating the Swing client with the Spring Boot server to automatically POST game results upon completion of game.

## Modules

- client/ <br>
  - `Game`: Handles the game board, movement validation, input handling, and UI rendering. <br>
  - `Piece`: Contains all chess piece classes (`Pawn`, `Rook`, `Bishop`, etc.) with piece-specific logic.

- server/ <br>
  - Spring Boot REST API with H2 (dev) and PostgreSQL (prod) database profiles. Stores player profiles, Elo ratings, and game results.

The entry point is the `GameRun.java` file within the `Game` package, which initializes and launches the chess
game interface. <br>

## Features

### Client (Swing)

- Click and drag pieces using your mouse.
- Enforces turn order (White and Black alternate each move).
- Implements Pawn Promotion: a pawn that reaches the opponent's back rank is
  automatically promoted to a Queen.
- A player wins by capturing the opposing King; a win dialog is displayed and the result is automatically sent to the server.
- Prompts both players for their names before the game starts.
- Visual board and sprite rendering is done via Java's `Graphics2D` and `BufferedImage`.

### Server (Spring Boot)

- REST endpoints:
  - `POST /api/v1/results` -> record game results
  - `GET /api/v1/leaderboard?limit=N` -> fetch top N players
- Player "profiles" are automatically created or updated by name.
- Elo reating system with wins/losses tracked.
- In-memory H2 database for development console available at: [http//localhost:8080/h2-console](http//localhost:8080/h2-console)
- PostgreSQL for production via Docker Compose (`docker compose up -d`)
- Game results are automatically received from the Swing client upon game completion. 

## How to Run

### Prerequisites

- Java 24
- Gradle wrapper (included)
- Docker (for PostgreSQL database)

<ol>
    <li> Clone or download all source files from this GitHub repository.</li>
    <li> Ensure you have <strong>Java 24 or later</strong> installed</li>
    <li> Run the client then server from the root directory using Gradle</li>
</ol>

#### Run Client (GUI Chess):

<pre># On macOS/Linux
./gradlew :client:run

# On Windows
gradlew.bat :client:run </pre>

#### Run Server (REST API):

<pre># On macOS/Linux
./gradlew :server:bootRun

# On Windows
gradlew.bat :server:bootRun </pre>
Once running:

- Visit H2 console -> `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:chess`
  - User: `sa`
  - Password: _(blank)_

### Test API (optional)

<pre># record a result (White beats Black)
curl -X POST http://localhost:8080/api/v1/results \
  -H "Content-Type: application/json" \
  -d '{"whiteName":"Alice","blackName":"Bob","winner":"WHITE"}'

# fetch leaderboard
curl "http://localhost:8080/api/v1/leaderboard?limit=10" </pre>

#### Run the full backend with Docker (recommended)

This starts everything — PostgreSQL, the REST API, and a web database viewer —
with a single command. Requires Docker Desktop to be running.

<pre># Build and start Postgres + API + Adminer
docker compose up --build -d

# Stop everything (keeps the database data)
docker compose down

# Stop AND wipe the database (fresh start)
docker compose down -v</pre>

Once it is up, open these in your browser:

| What | URL |
| --- | --- |
| Leaderboard (live JSON) | `http://localhost:8080/api/v1/leaderboard` |
| **Adminer — browse the database** | `http://localhost:8081` |
| Health check | `http://localhost:8080/actuator/health` |

**Logging into Adminer** (to see the `players` and `game_results` tables):

| Field | Value |
| --- | --- |
| System | PostgreSQL |
| Server | `db` |
| Username | `chess` |
| Password | `secret` |
| Database | `chessdb` |

The data is created by playing games in the client. Point the client at the
server (and send the API key the server expects):

<pre># On Windows
gradlew.bat :client:run "-Dchess.server.url=http://localhost:8080" "-Dchess.api.key=dev-local-key"</pre>

> The Docker Postgres is published on host port **5434** (not 5432) to avoid
> clashing with any PostgreSQL you have installed locally. To connect a desktop
> tool like pgAdmin to the Docker database, use `localhost:5434`.

#### Run the API against Postgres without Docker (optional)

<pre># Start just the database container
docker compose up -d db

# On macOS/Linux
SPRING_PROFILES_ACTIVE=postgres SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5434/chessdb \
  SPRING_DATASOURCE_USERNAME=chess SPRING_DATASOURCE_PASSWORD=secret ./gradlew :server:bootRun

# On Windows (PowerShell)
$env:SPRING_PROFILES_ACTIVE="postgres"
$env:SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5434/chessdb"
$env:SPRING_DATASOURCE_USERNAME="chess"; $env:SPRING_DATASOURCE_PASSWORD="secret"
gradlew.bat :server:bootRun </pre>

## Project Significance

<ul>
    <li> Demonstrates <strong>Object-Oriented Principles and Design</strong> (abstraction, polymorphism, inheritance, encapsulation</li>
    <li> GUI development using <strong>Java Swing</strong></li>
    <li> Practical use of <strong>Gradle (Kotlin DSL)</strong> multi-module builds.</li>
    <li> Backend experience with <strong>Spring Boot, REST APIs, H2, and PostgreSQL</strong>.</li>
    <li> Implements an <strong>Elo</strong> rating system to persist player performance.</li>
    <li> Collaboration with <strong>Git</strong>, version control, and clean architecture.</li>
</ul>

## Documentation

View Full JavaDocs Online: [Click here to open the JavaDocs](https://ian8912.github.io/ChessOOP/)

Thank you for checking out this project!

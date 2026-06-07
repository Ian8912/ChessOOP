# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

This is a multi-module Gradle project (Kotlin DSL) targeting **Java 24**. Use the included Gradle wrapper.

```bash
# Build everything
gradlew.bat build

# Run client (Swing GUI chess game)
gradlew.bat :client:run

# Run server (Spring Boot REST API)
gradlew.bat :server:bootRun

# Run all tests
gradlew.bat test

# Run tests for a single module
gradlew.bat :client:test
gradlew.bat :server:test

# Run a single test class
gradlew.bat :server:test --tests "com.chessoop.server.SomeTestClass"

# Clean build
gradlew.bat clean build
```

## Architecture

Two independent modules sharing no code:

### `client/` — Swing Chess GUI
- **Entry point:** `Game.GameRun` — creates JFrame with Board panel
- **Game logic:** `Game.Board` — manages 8x8 grid, piece list, turn tracking, move validation, captures, and pawn promotion
- **Input:** `Game.Input` (MouseAdapter) — click-drag-release to move pieces
- **Piece hierarchy:** Abstract `Piece.Piece` base class with subclasses `King`, `Queen`, `Rook`, `Bishop`, `Knight`, `Pawn` — each implements `isValidMove(toCol, toRow, Board)` with piece-specific movement rules
- Rendering via `Graphics2D` and `BufferedImage` sprites

### `server/` — Spring Boot REST API
- **Entry point:** `com.chessoop.server.ServerApplication`
- **Layers:** Controller → Service → Repository (standard Spring architecture)
- **API endpoints** (`/api/v1`):
  - `POST /results` — record a game result (upserts players, applies Elo, persists GameResult)
  - `GET /leaderboard?limit=N` — top players sorted by Elo then wins
  - `GET /ping`, `POST /echo` — debug endpoints
- **Entities:** `Player` (UUID, name, elo, wins, losses) and `GameResult` (white/black Player refs, winner enum, PGN, timestamp)
- **DTOs** are Java `record` classes in `api/dto/`
- **Elo system:** K-factor=32, standard chess Elo formula in `ResultService`

### Database
- **PostgreSQL only.** Single config in `application.properties`; connection comes from `SPRING_DATASOURCE_URL`/`_USERNAME`/`_PASSWORD` env vars, defaulting to the local Docker Postgres (`jdbc:postgresql://localhost:5434/chessdb`, user `chess`, pass `secret`).
- **Docker:** `docker compose up -d` runs Postgres 16 (db `chessdb`) + the API + Adminer (DB viewer at `http://localhost:8081`). DB published on host port `5434`.
- **Tests:** start a throwaway Postgres via Testcontainers (`@ServiceConnection`), so running `:server:test` requires Docker.

## Conventions
- Client uses uppercase package names (`Game/`, `Piece/`) — match this when adding client code
- Server follows standard Spring Boot package conventions under `com.chessoop.server`
- JUnit 5 for all tests
- Git branches use prefixes: `feat/`, `fix/`, `chore/`, `ci/`

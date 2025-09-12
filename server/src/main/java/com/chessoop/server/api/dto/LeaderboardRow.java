package com.chessoop.server.api.dto;

public record LeaderboardRow(String name, int elo, int wins, int losses) {}

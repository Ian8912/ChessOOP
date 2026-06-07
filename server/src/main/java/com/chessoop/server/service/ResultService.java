package com.chessoop.server.service;

import com.chessoop.server.api.dto.LeaderboardRow;
import com.chessoop.server.api.dto.RecordResultRequest;
import com.chessoop.server.domain.*;
import com.chessoop.server.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
public class ResultService {
    private final PlayerRepo players;
    private final GameResultRepo results;

    public ResultService(PlayerRepo players, GameResultRepo results){
        this.players = players;
        this.results = results;
    }

    @Transactional
    public GameResult record(RecordResultRequest req){
        System.out.println("== RECORD REQUEST ==");
        System.out.println("white=" + req.whiteName() + " black=" + req.blackName() + " winner=" + req.winner());

        Player white = upsert(req.whiteName());
        Player black = upsert(req.blackName());

        applyOutcomeAndElo(white, black, req.winner());

        players.save(white);
        players.save(black);

        GameResult saved = results.save(new GameResult(white, black, req.winner(), req.pgn(), req.finishedAt()));


        return saved;
    }

    public List<LeaderboardRow> leaderboard(int limit){
        return players.findAll().stream()
                .sorted(
                        // Highest Elo first, ties broken by most wins. Building the
                        // ascending comparator and reversing once flips both keys.
                        Comparator.comparingInt(Player::getElo)
                                .thenComparingInt(Player::getWins)
                                .reversed()
                )
                .limit(Math.max(1, limit))
                .map(p -> new LeaderboardRow(p.getName(), p.getElo(), p.getWins(), p.getLosses()))
                .toList();
    }

    private Player upsert(String raw){
        String norm = raw.toLowerCase(Locale.ROOT).trim();

        return players.findByNameIgnoreCase(norm).orElseGet(() -> new Player(norm));
    }

    private void applyOutcomeAndElo(Player white, Player black, Winner w){
        int Ra = white.getElo();
        int Rb = black.getElo();

        double Ea = 1.0 / (1.0 + Math.pow(10, (Rb - Ra) / 400.0));
        double Eb = 1.0 - Ea;
        double Sa, Sb;

        if (w == Winner.WHITE){
            Sa = 1;
            Sb = 0;
            white.incWins();
            black.incLosses();
        }
        else if (w == Winner.BLACK){
            Sa = 0;
            Sb = 1;
            white.incLosses();
            black.incWins();
        }
        else{
            Sa = 0.5;
            Sb = 0.5;
        }

        int K = 32;

        white.setElo((int)Math.round(Ra + K*(Sa - Ea)));
        black.setElo((int)Math.round(Rb + K*(Sb - Eb)));
    }
    
}

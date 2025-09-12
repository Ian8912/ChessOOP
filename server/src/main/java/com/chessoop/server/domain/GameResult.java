package com.chessoop.server.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="game_results")
public class GameResult {
    @Id @GeneratedValue private UUID id;

    @ManyToOne(optional=false, fetch=FetchType.LAZY) private Player white;
    @ManyToOne(optional=false, fetch=FetchType.LAZY) private Player black;

    @Enumerated(EnumType.STRING) @Column(nullable=false, length=8)
    private Winner winner;

    @Lob private String pgn;
    @Column(nullable=false) private Instant finishedAt = Instant.now();

    protected GameResult() {}

    public GameResult(Player w, Player b, Winner win, String pgn, Instant finishedAt){
        this.white = w;
        this.black = b;
        this.winner = win;
        this.pgn = pgn;

        if (finishedAt != null){
            this.finishedAt = finishedAt;
        }
    }

    public UUID getId(){
        return id;
    }

}
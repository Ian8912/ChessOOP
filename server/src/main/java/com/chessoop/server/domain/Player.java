package com.chessoop.server.domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name="players", uniqueConstraints=@UniqueConstraint(name="uk_player_name", columnNames="name"))
public class Player {
    @Id @GeneratedValue private UUID id;

    @Column(nullable=false, length=64) private String name; // store lowercase
    @Column(nullable=false) private int elo = 1200;
    @Column(nullable=false) private int wins = 0;
    @Column(nullable=false) private int losses = 0;

    protected Player() {}

    public Player(String name){

        this.name = name.toLowerCase().trim();
    }

    public UUID getId(){

        return id;
    }

    public String getName(){

        return name;
    }

    public int getElo(){

        return elo;
    }

    public int getWins(){

        return wins;
    }

    public int getLosses(){

        return losses;
    }

    public void setElo(int elo){

        this.elo = elo;
    }

    public void incWins(){

        wins++;
    }

    public void incLosses(){

        losses++;
    }

}

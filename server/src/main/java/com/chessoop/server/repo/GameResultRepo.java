package com.chessoop.server.repo;

import com.chessoop.server.domain.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface GameResultRepo extends JpaRepository<GameResult, UUID> {}

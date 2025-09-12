package com.chessoop.server.repo;

import com.chessoop.server.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface PlayerRepo extends JpaRepository<Player, UUID> {

    Optional<Player> findByNameIgnoreCase(String name);

}

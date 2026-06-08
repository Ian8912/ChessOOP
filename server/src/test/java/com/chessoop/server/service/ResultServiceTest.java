package com.chessoop.server.service;

import com.chessoop.server.api.dto.LeaderboardRow;
import com.chessoop.server.api.dto.RecordResultRequest;
import com.chessoop.server.domain.GameResult;
import com.chessoop.server.domain.Player;
import com.chessoop.server.domain.Winner;
import com.chessoop.server.repo.GameResultRepo;
import com.chessoop.server.repo.PlayerRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ResultService}: Elo updates and leaderboard ordering.
 *
 * <p>The repositories are mocked, so these run without a database (no Docker /
 * Testcontainers required). The integration wiring against a real PostgreSQL is
 * covered separately by {@code ServerApplicationTests}.</p>
 */
@ExtendWith(MockitoExtension.class)
class ResultServiceTest {

    @Mock PlayerRepo players;
    @Mock GameResultRepo results;
    @InjectMocks ResultService service;

    private static RecordResultRequest result(String white, String black, Winner winner) {
        return new RecordResultRequest(white, black, winner, null, null);
    }

    private static Player playerWith(String name, int elo, int wins, int losses) {
        Player p = new Player(name);
        p.setElo(elo);
        for (int i = 0; i < wins; i++) p.incWins();
        for (int i = 0; i < losses; i++) p.incLosses();
        return p;
    }

    /** Runs record() for two brand-new players and returns the saved [white, black]. */
    private List<Player> recordWithNewPlayers(RecordResultRequest req) {
        when(players.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(players.save(any(Player.class))).thenAnswer(inv -> inv.getArgument(0));
        when(results.save(any(GameResult.class))).thenAnswer(inv -> inv.getArgument(0));

        service.record(req);

        ArgumentCaptor<Player> captor = ArgumentCaptor.forClass(Player.class);
        verify(players, times(2)).save(captor.capture());
        return captor.getAllValues(); // record() saves white first, then black
    }

    @Test
    void newPlayersStartAt1200AndWinnerGainsSixteenWhenEvenlyRated() {
        List<Player> saved = recordWithNewPlayers(result("Alice", "Bob", Winner.WHITE));
        // Both start at 1200, expected score 0.5 each; K=32 -> +/-16.
        assertEquals(1216, saved.get(0).getElo());
        assertEquals(1184, saved.get(1).getElo());
    }

    @Test
    void winnerGetsWinAndLoserGetsLoss() {
        List<Player> saved = recordWithNewPlayers(result("Alice", "Bob", Winner.WHITE));
        Player white = saved.get(0);
        Player black = saved.get(1);
        assertEquals(1, white.getWins());
        assertEquals(0, white.getLosses());
        assertEquals(0, black.getWins());
        assertEquals(1, black.getLosses());
    }

    @Test
    void blackWinMirrorsWhiteWin() {
        List<Player> saved = recordWithNewPlayers(result("Alice", "Bob", Winner.BLACK));
        assertEquals(1184, saved.get(0).getElo());
        assertEquals(1216, saved.get(1).getElo());
        assertEquals(1, saved.get(1).getWins());
        assertEquals(1, saved.get(0).getLosses());
    }

    @Test
    void eloExchangeIsZeroSum() {
        List<Player> saved = recordWithNewPlayers(result("Alice", "Bob", Winner.WHITE));
        int whiteDelta = saved.get(0).getElo() - 1200;
        int blackDelta = saved.get(1).getElo() - 1200;
        assertEquals(0, whiteDelta + blackDelta,
            "points gained by the winner should equal points lost by the loser");
    }

    @Test
    void higherRatedWinnerGainsFewerPointsThanEvenMatch() {
        when(players.findByNameIgnoreCase("alice"))
            .thenReturn(Optional.of(playerWith("alice", 1400, 0, 0)));
        when(players.findByNameIgnoreCase("bob"))
            .thenReturn(Optional.of(playerWith("bob", 1200, 0, 0)));
        when(players.save(any(Player.class))).thenAnswer(inv -> inv.getArgument(0));
        when(results.save(any(GameResult.class))).thenAnswer(inv -> inv.getArgument(0));

        service.record(result("Alice", "Bob", Winner.WHITE));

        ArgumentCaptor<Player> captor = ArgumentCaptor.forClass(Player.class);
        verify(players, times(2)).save(captor.capture());
        int gain = captor.getAllValues().get(0).getElo() - 1400;
        assertTrue(gain > 0 && gain < 16,
            "favorite beating an underdog should gain a small amount, got " + gain);
    }

    @Test
    void leaderboardSortsByEloDescending() {
        when(players.findAll()).thenReturn(List.of(
            playerWith("low", 1100, 0, 3),
            playerWith("high", 1500, 9, 1),
            playerWith("mid", 1300, 4, 4)));

        List<LeaderboardRow> rows = service.leaderboard(10);

        assertEquals(List.of("high", "mid", "low"),
            rows.stream().map(LeaderboardRow::name).toList());
    }

    @Test
    void leaderboardBreaksEloTiesByWins() {
        when(players.findAll()).thenReturn(List.of(
            playerWith("few", 1300, 2, 0),
            playerWith("more", 1300, 7, 0)));

        List<LeaderboardRow> rows = service.leaderboard(10);

        assertEquals("more", rows.get(0).name());
        assertEquals("few", rows.get(1).name());
    }

    @Test
    void leaderboardRespectsLimitAndAlwaysReturnsAtLeastOne() {
        when(players.findAll()).thenReturn(List.of(
            playerWith("a", 1400, 0, 0),
            playerWith("b", 1300, 0, 0),
            playerWith("c", 1200, 0, 0)));

        assertEquals(2, service.leaderboard(2).size());
        assertEquals(1, service.leaderboard(0).size(), "limit should floor at 1");
    }
}

package Game;

import Piece.PieceColor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Search} negamax engine (alpha-beta + quiescence).
 *
 * <p>From the starting position there are no tactics, so a material-only search
 * should report a level score regardless of depth.</p>
 */
class SearchTest {

    private static Board startingBoard() {
        Board board = new Board();
        board.addPieces();
        return board;
    }

    @Test
    void horizonScoreIsMaterialBalanceAtStart() {
        Board board = startingBoard();
        // Depth 0 falls straight into quiescence; with no captures available the
        // score is the (balanced) static evaluation.
        int score = Search.negamax(board, 0, 0, -Search.INF, Search.INF, PieceColor.WHITE);
        assertEquals(0, score);
    }

    @Test
    void searchStaysLevelFromStartingPosition() {
        Board board = startingBoard();
        // With material-only evaluation and no tactics in the first two plies,
        // best play neither wins nor loses material.
        int score = Search.negamax(board, 2, 0, -Search.INF, Search.INF, PieceColor.WHITE);
        assertEquals(0, score);
    }
}

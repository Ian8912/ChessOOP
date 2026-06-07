package Game;

import Piece.PieceColor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Regression tests for check/checkmate/stalemate detection on {@link Board}.
 *
 * <p>The key guarantee is that detection is <em>turn-independent</em>: the helpers
 * must report the truth for either color regardless of the {@code whiteTurn} flag.
 * The previous implementation routed detection through {@code validMove}, whose
 * turn guard rejected every move for the side not to move — so the side-not-to-move
 * always appeared to have zero legal moves, which is exactly what produced false
 * checkmate/stalemate calls.</p>
 */
class BoardCheckmateTest {

    /** A freshly set-up board in the standard starting position; White to move. */
    private static Board startingBoard() {
        Board board = new Board();
        board.addPieces();
        return board;
    }

    @Test
    void startingPositionIsNotCheckOrMateForEitherSide() {
        Board board = startingBoard();
        for (PieceColor c : PieceColor.values()) {
            assertFalse(board.isKingInCheck(c), c + " should not be in check at the start");
            assertFalse(board.isCheckmate(c), c + " should not be checkmated at the start");
            assertFalse(board.isStalemate(c), c + " should not be stalemated at the start");
            assertTrue(board.hasLegalMoves(c), c + " should have legal moves at the start");
        }
    }

    /**
     * The core regression assertion: detection is independent of whose turn it is.
     * At the start it is White's turn, yet Black must still report its full set of
     * 20 opening moves (16 pawn pushes + 4 knight moves). Under the old turn-guarded
     * detection this returned 0, the root cause of the false checkmate.
     */
    @Test
    void legalMoveCountIsTurnIndependentAtStart() {
        Board board = startingBoard();
        assertEquals(20, board.countLegalMoves(PieceColor.WHITE), "White opening moves");
        assertEquals(20, board.countLegalMoves(PieceColor.BLACK),
            "Black must report its 20 opening moves even when it is White's turn");
    }

    @Test
    void everyKingHasAnAttackerListThatIsEmptyWhenSafe() {
        Board board = startingBoard();
        assertTrue(board.attackersOf(PieceColor.WHITE).isEmpty());
        assertTrue(board.attackersOf(PieceColor.BLACK).isEmpty());
        assertNotNull(board.findKing(PieceColor.WHITE));
        assertNotNull(board.findKing(PieceColor.BLACK));
    }
}

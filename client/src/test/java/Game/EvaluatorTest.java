package Game;

import Piece.PieceColor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Evaluator}, the material-based position scorer that
 * underpins both the computer player and the move-quality grading.
 */
class EvaluatorTest {

    private static Board startingBoard() {
        Board board = new Board();
        board.addPieces();
        return board;
    }

    @Test
    void startingPositionIsBalancedForBothColors() {
        Board board = startingBoard();
        assertEquals(0, Evaluator.evaluate(board, PieceColor.WHITE),
            "equal material should score 0 from White's perspective");
        assertEquals(0, Evaluator.evaluate(board, PieceColor.BLACK),
            "equal material should score 0 from Black's perspective");
    }

    @Test
    void startingMaterialExcludesKingAndTotalsExpectedValue() {
        // 8*100 (pawns) + 2*320 (knights) + 2*330 (bishops) + 2*500 (rooks)
        // + 900 (queen) = 4000 centipawns; the king is intentionally excluded.
        Board board = startingBoard();
        assertEquals(4000, Evaluator.material(board, PieceColor.WHITE));
        assertEquals(4000, Evaluator.material(board, PieceColor.BLACK));
    }
}

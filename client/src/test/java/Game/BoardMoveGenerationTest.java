package Game;

import Piece.PieceColor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Board} move generation from the standard opening
 * position (the well-known 20 legal moves per side, with no captures yet).
 */
class BoardMoveGenerationTest {

    private static Board startingBoard() {
        Board board = new Board();
        board.addPieces();
        return board;
    }

    @Test
    void eachSideHasTwentyLegalMovesAtStart() {
        Board board = startingBoard();
        // 16 pawn pushes (8 pawns x 1 or 2 squares) + 4 knight moves.
        assertEquals(20, board.generateLegalMoves(PieceColor.WHITE).size());
        assertEquals(20, board.generateLegalMoves(PieceColor.BLACK).size());
    }

    @Test
    void noCaptureMovesAreAvailableAtStart() {
        Board board = startingBoard();
        assertTrue(board.generateCaptureMoves(PieceColor.WHITE).isEmpty());
        assertTrue(board.generateCaptureMoves(PieceColor.BLACK).isEmpty());
    }
}

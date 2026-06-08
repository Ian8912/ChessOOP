package Game;

import Piece.PieceColor;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ComputerPlayer}, which selects a move via the
 * negamax search and must always return a legal move for its own color.
 */
class ComputerPlayerTest {

    private static Board startingBoard() {
        Board board = new Board();
        board.addPieces();
        return board;
    }

    @Test
    void returnsAMoveForItsOwnColorFromStartingPosition() {
        Board board = startingBoard();
        ComputerPlayer cpu = new ComputerPlayer(PieceColor.WHITE);

        Move move = cpu.getBestMove(board);

        assertNotNull(move);
        assertEquals(PieceColor.WHITE, move.piece.getColor());
    }

    @Test
    void chosenMoveIsAmongTheLegalMoves() {
        Board board = startingBoard();
        ComputerPlayer cpu = new ComputerPlayer(PieceColor.WHITE);

        Move move = cpu.getBestMove(board);

        // Move has no equals(), so match on coordinates against a freshly
        // generated legal move list.
        List<Move> legal = board.generateLegalMoves(PieceColor.WHITE);
        boolean legalChoice = legal.stream().anyMatch(m ->
                m.oldCol == move.oldCol && m.oldRow == move.oldRow &&
                m.newCol == move.newCol && m.newRow == move.newRow);
        assertTrue(legalChoice, "computer should pick one of the legal moves");
    }
}

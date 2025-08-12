package Game;
import Piece.*;

/**
 * Represents a chess move, including the piece being moved, its
 * original and new positions, and any captured piece.
 *
 * <p>Stores all information needed to validate or execute a move on
 * the {@link Board}, including old and new coordinates and any piece
 * that might be captured at the destination.</p>
 *
 * @see Board
 * @see Piece
 */
public class Move {

    /** The original column of the piece before the move. */
     public int oldCol;
    /** The original row of the piece before the move. */
     public int oldRow;
     /** The new column of the piece after the move. */
     public int newCol;
    /** The new row of the piece after the move. */
     public int newRow;

    /** The piece being moved. */
     public Piece piece;
    /** The piece being captured at the destination, if any */
     public Piece Capture;

    /**
     * Constructs the move object that stores the piece's original position, it's destination,
     * and any piece present at the destination square for potential capture.
     *
     * <p>This parameterized constructor creates a {@link Move} instance by capturing the
     * state of a piece before and after a player action on the {@link Board}. It records the
     * piece's current column and row, the intended target location, and checks if a piece exists
     * at the destination to handle captures.</p>
     *
     * @param board the chess {@link Board} containing the piece positions
     * @param piece the {@link Piece} being moved
     * @param newCol the destination column
     * @param newRow the destination row
     */
     public Move(Board board, Piece piece, int newCol, int newRow){

        this.oldCol = piece.col;
        this.oldRow = piece.row;
        this.newCol = newCol;
        this.newRow = newRow;

        this.piece = piece;
        this.Capture = board.getPiece(newCol, newRow);
     }

}
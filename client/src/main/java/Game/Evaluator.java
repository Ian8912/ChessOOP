package Game;

import Piece.Bishop;
import Piece.King;
import Piece.Knight;
import Piece.Pawn;
import Piece.Piece;
import Piece.PieceColor;
import Piece.Queen;
import Piece.Rook;

/**
 * Provides static evaluation of a chess position in centipawns.
 *
 * <p>The score is based purely on material balance: each piece is assigned a
 * standard centipawn value, and the board is scored from a given color's
 * perspective (positive means that color is ahead). This single function is the
 * foundation for both the computer player's move selection and the move-quality
 * feedback shown to the human player.</p>
 *
 * <p>This evaluation counts material only; it is the leaf evaluation for the
 * negamax + quiescence search in {@link Search}, which supplies the look-ahead.
 * Because the search resolves captures, hanging a piece is now detected even
 * though this function itself does not look ahead. The natural next accuracy
 * lever is positional terms here (piece-square tables, king safety, mobility).</p>
 *
 * @see Board
 * @see ComputerPlayer
 * @see Search
 */
public final class Evaluator {

    /** Standard centipawn value of a pawn. */
    private static final int PAWN_VALUE = 100;
    /** Standard centipawn value of a knight. */
    private static final int KNIGHT_VALUE = 320;
    /** Standard centipawn value of a bishop. */
    private static final int BISHOP_VALUE = 330;
    /** Standard centipawn value of a rook. */
    private static final int ROOK_VALUE = 500;
    /** Standard centipawn value of a queen. */
    private static final int QUEEN_VALUE = 900;
    /** Centipawn value of a king (large so its loss dominates the score). */
    private static final int KING_VALUE = 20000;

    /** Non-instantiable utility class. */
    private Evaluator() {}

    /**
     * Returns the centipawn value of a single piece.
     *
     * @param p the piece to value
     * @return the piece's centipawn value, or {@code 0} if unrecognized
     */
    private static int value(Piece p) {
        if (p instanceof Pawn)   return PAWN_VALUE;
        if (p instanceof Knight) return KNIGHT_VALUE;
        if (p instanceof Bishop) return BISHOP_VALUE;
        if (p instanceof Rook)   return ROOK_VALUE;
        if (p instanceof Queen)  return QUEEN_VALUE;
        if (p instanceof King)   return KING_VALUE;
        return 0;
    }

    /**
     * Evaluates the given board from {@code color}'s perspective.
     *
     * @param board the board state to evaluate
     * @param color the color whose perspective to score from
     * @return the material balance in centipawns; positive means {@code color}
     *         is ahead, negative means it is behind
     */
    public static int evaluate(Board board, PieceColor color) {
        int score = 0;
        for (Piece p : board.getPieceList()) {
            score += (p.getColor() == color) ? value(p) : -value(p);
        }
        return score;
    }

    /**
     * Returns {@code color}'s total material in centipawns, excluding the king.
     *
     * <p>The king is omitted because both sides always have exactly one, so it
     * carries no information for a captured-material readout and its large value
     * would dwarf the rest. This is intended for the on-screen score display, not
     * for search (which uses {@link #evaluate(Board, PieceColor)}).</p>
     *
     * @param board the board state to score
     * @param color the color whose material to total
     * @return the side's material in centipawns (divide by 100 for pawn units)
     */
    public static int material(Board board, PieceColor color) {
        int total = 0;
        for (Piece p : board.getPieceList()) {
            if (p.getColor() == color && !(p instanceof King)) {
                total += value(p);
            }
        }
        return total;
    }
}

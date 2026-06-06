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
 * <p>This evaluation is intentionally shallow — it counts material only and does
 * not look ahead at the opponent's reply, so a move that wins material now but
 * hangs a piece next turn will still score well. Adding a small look-ahead
 * (minimax) is the natural next step for stronger play and more accurate grading.</p>
 *
 * @see Board
 * @see ComputerPlayer
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
}

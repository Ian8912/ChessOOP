package Game;

import Piece.Piece;
import Piece.PieceColor;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a computer-controlled chess player that selects random legal moves.
 *
 * <p>On each turn, {@link #getBestMove(Board)} enumerates every legal move available
 * to the computer's color and returns one chosen at random. This serves as the
 * foundation for a stronger AI (e.g. minimax) in a future phase.</p>
 *
 * @see Board
 * @see Move
 */
public class ComputerPlayer {

    /** The color this computer player controls. */
    private final PieceColor color;

    /**
     * Constructs a ComputerPlayer for the given color.
     *
     * @param color the piece color this player controls
     */
    public ComputerPlayer(PieceColor color) {
        this.color = color;
    }

    /**
     * Returns the color this computer player controls.
     *
     * @return the {@link PieceColor} of this player
     */
    public PieceColor getColor() {
        return color;
    }

    /**
     * Selects the highest-scoring legal move for the computer's color.
     *
     * <p>Each legal move is scored via {@link Board#scoreMove(Move)} using the
     * material-based {@link Evaluator}, and the best-scoring move is returned. Ties
     * are broken randomly so the computer does not always play the same line. This
     * is a one-ply (greedy) search — it does not consider the opponent's reply.</p>
     *
     * @param board the current board state
     * @return the best-scoring legal {@link Move}, or {@code null} if none exist
     */
    public Move getBestMove(Board board) {
        List<Move> legal = getAllLegalMoves(board);
        if (legal.isEmpty()) return null;

        List<Move> best = new ArrayList<>();
        int bestScore = Integer.MIN_VALUE;
        for (Move m : legal) {
            int score = board.scoreMove(m);
            if (score > bestScore) {
                bestScore = score;
                best.clear();
                best.add(m);
            } else if (score == bestScore) {
                best.add(m);
            }
        }
        return best.get((int) (Math.random() * best.size()));
    }

    /**
     * Enumerates all legal moves available to the computer's color.
     *
     * <p>Iterates over a snapshot of the piece list to avoid
     * {@link java.util.ConcurrentModificationException} when {@link Board#validMove(Move)}
     * temporarily mutates the board during check simulation.</p>
     *
     * @param board the current board state
     * @return list of all legal moves for this player's color
     */
    private List<Move> getAllLegalMoves(Board board) {
        List<Move> moves = new ArrayList<>();
        // Snapshot to avoid ConcurrentModificationException during validMove simulation
        List<Piece> pieces = new ArrayList<>(board.getPieceList());
        for (Piece p : pieces) {
            if (p.getColor() != color) continue;
            for (int col = 0; col < 8; col++) {
                for (int row = 0; row < 8; row++) {
                    Move m = new Move(board, p, col, row);
                    if (board.validMove(m)) moves.add(m);
                }
            }
        }
        return moves;
    }
}

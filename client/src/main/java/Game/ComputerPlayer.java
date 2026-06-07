package Game;

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

    /** Full-width search depth (plies) the computer looks ahead when choosing a move. */
    private static final int SEARCH_DEPTH = 2;

    /**
     * Selects the strongest legal move for the computer's color.
     *
     * <p>Each legal move is applied and the resulting position is searched with
     * {@link Search#negamax} (alpha-beta + quiescence) to {@link #SEARCH_DEPTH}
     * plies, scored from the opponent's perspective and negated back to this
     * player's. The highest-scoring move is returned, with ties broken randomly so
     * the computer does not always play the same line. Unlike the previous greedy
     * one-ply scoring, this accounts for the opponent's reply, so it no longer hangs
     * material to grab a defended piece.</p>
     *
     * @param board the current board state
     * @return the best legal {@link Move}, or {@code null} if none exist
     */
    public Move getBestMove(Board board) {
        List<Move> legal = board.generateLegalMoves(color);
        if (legal.isEmpty()) return null;

        PieceColor opponent = (color == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
        List<Move> best = new ArrayList<>();
        int bestScore = Integer.MIN_VALUE;
        for (Move m : legal) {
            Board.Undo u = board.makeSearchMove(m);
            int score = -Search.negamax(board, SEARCH_DEPTH - 1, 1, -Search.INF, Search.INF, opponent);
            board.unmakeSearchMove(u);

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
}

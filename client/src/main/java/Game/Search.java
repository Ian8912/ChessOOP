package Game;

import Piece.PieceColor;
import java.util.List;

/**
 * Fixed-depth negamax search with alpha-beta pruning and a capture-only
 * quiescence extension, scored in centipawns from the side-to-move's perspective.
 *
 * <p>This replaces the old one-ply material grading. Negamax explores the game
 * tree to a fixed {@code depth}; at the horizon it does not evaluate statically
 * but instead runs {@link #quiesce} to resolve outstanding captures, so a move
 * is never judged in the middle of an exchange. Together this lets grading and
 * the computer player see hung pieces <em>and</em> recaptures: hanging the queen
 * is punished, while a fair trade that is recaptured is not.</p>
 *
 * <p>Turn ownership is handled entirely by {@link Board#makeSearchMove(Move)} /
 * {@link Board#unmakeSearchMove(Board.Undo)}, which toggle {@code whiteTurn} as
 * moves are made and unmade. Callers must invoke the search with {@code color}
 * equal to the board's current side to move.</p>
 *
 * <p>Known limitations: quiescence does not handle being in check (it always
 * allows a stand-pat), and promotions/checks are not searched as quiescent
 * moves. Castling and en passant are not generated. These are acceptable for the
 * current material+search grading.</p>
 *
 * @see Board#makeSearchMove(Move)
 * @see Evaluator
 */
public final class Search {

    /** Score assigned to being checkmated (adjusted by ply so faster mates rank higher). */
    public static final int MATE = 1_000_000;
    /** Search window bound; larger than any reachable material/mate score. */
    public static final int INF = 1_000_000_000;

    /** Non-instantiable utility class. */
    private Search() {}

    /**
     * Returns the best achievable score for {@code color} from the current
     * position, searching {@code depth} plies before falling into quiescence.
     *
     * @param b     the board to search (mutated and fully restored during search)
     * @param depth remaining full-width plies to search
     * @param ply   distance from the search root, used for mate-distance scoring
     * @param alpha lower bound of the search window
     * @param beta  upper bound of the search window
     * @param color the side to move (must match the board's current turn)
     * @return the negamax score in centipawns from {@code color}'s perspective
     */
    public static int negamax(Board b, int depth, int ply, int alpha, int beta, PieceColor color) {
        if (depth == 0) return quiesce(b, alpha, beta, color);

        PieceColor opp = opponent(color);
        List<Move> moves = b.generateLegalMoves(color);

        if (moves.isEmpty()) {
            // No legal move: checkmate (bad, nearer mates worse) or stalemate (draw).
            return b.isKingInCheck(color) ? -(MATE - ply) : 0;
        }

        int best = -INF;
        for (Move m : moves) {
            Board.Undo u = b.makeSearchMove(m);
            int score = -negamax(b, depth - 1, ply + 1, -beta, -alpha, opp);
            b.unmakeSearchMove(u);

            if (score > best) best = score;
            if (best > alpha) alpha = best;
            if (alpha >= beta) break; // beta cutoff
        }
        return best;
    }

    /**
     * Resolves outstanding captures at the search horizon so a position is never
     * scored mid-exchange. Uses the standard stand-pat model: the side to move
     * may decline to capture (scored statically), or play any capture and
     * recurse.
     *
     * @param b     the board to search (mutated and fully restored)
     * @param alpha lower bound of the search window
     * @param beta  upper bound of the search window
     * @param color the side to move (must match the board's current turn)
     * @return the quiescent score in centipawns from {@code color}'s perspective
     */
    private static int quiesce(Board b, int alpha, int beta, PieceColor color) {
        int standPat = Evaluator.evaluate(b, color);
        if (standPat >= beta) return beta;
        if (standPat > alpha) alpha = standPat;

        PieceColor opp = opponent(color);
        for (Move m : b.generateCaptureMoves(color)) {
            Board.Undo u = b.makeSearchMove(m);
            int score = -quiesce(b, -beta, -alpha, opp);
            b.unmakeSearchMove(u);

            if (score >= beta) return beta;
            if (score > alpha) alpha = score;
        }
        return alpha;
    }

    /**
     * Returns the opposing color.
     *
     * @param c a piece color
     * @return the other color
     */
    private static PieceColor opponent(PieceColor c) {
        return (c == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
    }
}

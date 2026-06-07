package Game;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import Piece.Bishop;
import Piece.King;
import Piece.Knight;
import Piece.Pawn;
import Piece.Piece;
import Piece.PieceColor;
import Piece.Queen;
import Piece.Rook;

/**
 * Manages the chess board, including piece placement, captures, movement
 * validation, and repainting of piece images.
 * 
 * <p>This class manages all 64 squares of the chess game board. It initializes
 * pieces in their correct starting positions, handles captures of opposing
 * pieces, validates movement logic, and repaints piece images after every
 * valid move.</p>
 * 
 * @see Piece
 * @see Move
 * @see Input
 */
public class Board extends JPanel {

    /** The size (in pixels) of each tile on the board.*/
    public int ts = 85;

    /** The number of columns on the board (standard: 8). */
    private final int col = 8;
    /** The number of rows on the board (standard: 8). */
    private final int row = 8;
    
    /** The list of all active chess pieces on the board. */
    private ArrayList<Piece> pieceList = new ArrayList<>();
    
    /** The piece currently selected by the player. */
    public Piece selPiece;

    /** Tracks whether it is white's turn to play. */
    private boolean whiteTurn = true;

    /** Full-width search depth (plies) used to grade each move's quality. */
    private static final int GRADING_DEPTH = 2;

    /** The JTextArea component used to display information associated with the board.*/
    private JTextArea infoArea;

    /** Names of the two players, set before the game starts. */
    private String whiteName = "White";
    private String blackName = "Black";

    /** Quality label of each side's most recent move, kept so it persists across status updates. */
    private String whiteGrade = "";
    private String blackGrade = "";

    /** Handles mouse input events and piece interaction. */
    public Input in = new Input(this);

    /** The computer player, or {@code null} when playing human vs human. */
    private ComputerPlayer computerPlayer = null;

    /**
     * Constructs the chess board and prepares it for user interaction.
     *
     * <p>Sets the board's preferred size based on the number of rows and columns,
     * then attaches mouse input listeners to handle click and drag events from the user.</p>
     *
     * @see Input for mouse handling logic
     */
    public Board(){

        this.setPreferredSize(new Dimension(col * ts , row * ts));
        this.addMouseListener(in);
        this.addMouseMotionListener(in);
    }

    /**
     * Adds all chess pieces to their standard starting positions on the board.
     * 
     * <p>This method initializes and places all chess {@link Piece} subclasses into the internal
     * piece list. It defines their initial row, column, and color. This 
     * prepares the board for the start of the game.</p>
     *
     * @see Knight
     * @see Pawn
     * @see Rook
     * @see Bishop
     * @see Queen
     * @see King
     */
    public void addPieces(){

        pieceList.add(new Knight(this, 1, 0, PieceColor.BLACK));
        pieceList.add(new Knight(this, 6, 0, PieceColor.BLACK));
        pieceList.add(new Knight(this, 1, 7, PieceColor.WHITE));
        pieceList.add(new Knight(this, 6, 7, PieceColor.WHITE));
        pieceList.add(new Pawn(this, 0, 6, PieceColor.WHITE));
        pieceList.add(new Pawn(this, 1, 6, PieceColor.WHITE));
        pieceList.add(new Pawn(this, 2, 6, PieceColor.WHITE));
        pieceList.add(new Pawn(this, 3, 6, PieceColor.WHITE));
        pieceList.add(new Pawn(this, 4, 6, PieceColor.WHITE));
        pieceList.add(new Pawn(this, 5, 6, PieceColor.WHITE));
        pieceList.add(new Pawn(this, 6, 6, PieceColor.WHITE));
        pieceList.add(new Pawn(this, 7, 6, PieceColor.WHITE));
        pieceList.add(new Pawn(this, 0, 1, PieceColor.BLACK));
        pieceList.add(new Pawn(this, 1, 1, PieceColor.BLACK));
        pieceList.add(new Pawn(this, 2, 1, PieceColor.BLACK));
        pieceList.add(new Pawn(this, 3, 1, PieceColor.BLACK));
        pieceList.add(new Pawn(this, 4, 1, PieceColor.BLACK));
        pieceList.add(new Pawn(this, 5, 1, PieceColor.BLACK));
        pieceList.add(new Pawn(this, 6, 1, PieceColor.BLACK));
        pieceList.add(new Pawn(this, 7, 1, PieceColor.BLACK));
        pieceList.add(new Rook(this, 0, 0, PieceColor.BLACK));
        pieceList.add(new Rook(this, 7, 0, PieceColor.BLACK));
        pieceList.add(new Rook(this, 0, 7, PieceColor.WHITE));
        pieceList.add(new Rook(this, 7, 7, PieceColor.WHITE));
        pieceList.add(new Bishop(this, 2, 0, PieceColor.BLACK));
        pieceList.add(new Bishop(this, 5, 0, PieceColor.BLACK));
        pieceList.add(new Bishop(this, 2, 7, PieceColor.WHITE));
        pieceList.add(new Bishop(this, 5, 7, PieceColor.WHITE));
        pieceList.add(new Queen(this, 3, 0, PieceColor.BLACK));
        pieceList.add(new Queen(this, 3, 7, PieceColor.WHITE));
        pieceList.add(new King(this, 4, 7, PieceColor.WHITE));
        pieceList.add(new King(this, 4, 0, PieceColor.BLACK));
    }

    /**
     * Connects the chess board with the specified JTextArea.
     *
     * <p>This method associates the given {@link JTextArea} to this {@link Board}, so
     * it can be used to display game information or updates.</p>
     *
     * @param infoArea the {@link JTextArea} info area
     */
    public void setInfoArea(JTextArea infoArea){

        this.infoArea = infoArea;
    }

    /**
     * Sets the display names for both players.
     *
     * @param whiteName name of the white player
     * @param blackName name of the black player
     */
    public void setPlayerNames(String whiteName, String blackName) {
        this.whiteName = whiteName;
        this.blackName = blackName;
    }

    /**
     * Returns an unmodifiable view of the active piece list.
     *
     * @return read-only list of all pieces currently on the board
     */
    public List<Piece> getPieceList() {
        return Collections.unmodifiableList(pieceList);
    }

    /**
     * Sets the computer player. Pass {@code null} to disable computer play.
     *
     * @param cp the {@link ComputerPlayer} to use, or {@code null} for human vs human
     */
    public void setComputerPlayer(ComputerPlayer cp) {
        this.computerPlayer = cp;
    }

    /**
     * Returns the computer player, or {@code null} if not set.
     *
     * @return the active {@link ComputerPlayer}, or {@code null}
     */
    public ComputerPlayer getComputerPlayer() {
        return computerPlayer;
    }

    /**
     * Renders the chess board and all active pieces.
     * 
     * <p>This method paints the checkered background of the 8x8 board
     * and calls the {@code paint()} method of each active piece
     * to draw them in their current positions.</p>
     * 
     * @param g the Graphics context used for drawing
     */
    @Override
    public void paintComponent(Graphics g){

        Graphics2D g2 = (Graphics2D) g;

        for (int r = 0; r < row; r++){
            for (int c = 0; c < col; c++){
                g2.setColor((c+r) % 2 == 0 ? new Color(255, 255, 255) : new Color(122, 173, 107) );
                g2.fillRect(r* ts, c*ts, ts, ts);
            }
        }

        for (Piece piece : pieceList){
            piece.paint(g2);
        }
    }

    /**
     * Retrieve the chess piece located at the specified column and row.
     * 
     * <p>This method searches through the {@code pieceList} to find a piece
     * that matches the given board coordinates. If no piece is found at the
     * specified location, the method returns {@code null}.</p>
     * 
     * @param col the column of the desired piece (0-7)
     * @param row the row of the desired piece (0-7)
     * @return the chess {@link Piece} at the specified location, or {@code null} if none exists
     */
    public Piece getPiece(int col, int row){

        for (Piece piece : pieceList){
            if (piece.col == col && piece.row == row){
                return piece;
            }
        }
        return null;
    }

    /**
     * Executes a player's move if it is valid; otherwise resets the piece's position.
     * 
     * <p>This method receives a {@link Move} object containing the piece's
     * current position ("from") and target position ("to"). If the move is
     * valid, the piece's board and screen coordinates are updated, a capture
     * is performed if necessary, the turn is switched, and the {@code infoArea} is updated. If the move is
     * invalid, the piece is reset to its original location.</p>
     *
     * @param move the {@link Move} object containing coordinates for current piece location 
     *             and destination location.
     */
    public void makeMove(Move move){

        if(validMove(move)){
            if((move.piece.col != move.newCol || move.piece.row != move.newRow)){

                // Grade the move's quality before applying it (both sides are graded).
                // Both the baseline and the chosen move are searched with negamax +
                // quiescence to the same horizon, so hanging a piece is punished while a
                // recaptured fair trade is not.
                PieceColor mover = move.piece.getColor();
                PieceColor moverOpp = (mover == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;

                // Best the mover could have done from this position.
                int best = Search.negamax(this, GRADING_DEPTH, 0, -Search.INF, Search.INF, mover);
                // Value of the move actually played: apply it, then search the opponent's
                // reply to the same horizon and negate back to the mover's perspective.
                Undo graded = makeSearchMove(move);
                int chosen = -Search.negamax(this, GRADING_DEPTH - 1, 1, -Search.INF, Search.INF, moverOpp);
                unmakeSearchMove(graded);

                move.piece.col = move.newCol;
                move.piece.row = move.newRow;

                move.piece.xpos = move.newCol * ts;
                move.piece.ypos = move.newRow * ts;

                // Mark the piece as moved (used by the pawn two-square rule).
                // Done here, on the real move only — never during validation/grading scans.
                move.piece.switchMadeMove();

                capture(move);

                // Record this move's grade so it persists across later status updates.
                String grade = gradeLabel(best - chosen);
                if(mover == PieceColor.WHITE) whiteGrade = grade;
                else blackGrade = grade;

                whiteTurn = !whiteTurn;

                // The move is complete and the turn has flipped, so `nextColor` is the
                // player about to move — the side whose king we test for check/mate.
                // After an AI move this resolves to the human, and vice versa, so we
                // always evaluate the side to move's king, never the mover's own.
                PieceColor nextColor = whiteTurn ? PieceColor.WHITE : PieceColor.BLACK;
                String nextName = whiteTurn ? whiteName : blackName;

                // Diagnostic snapshot of the side-to-move's king for tracing mate calls.
                debugKingStatus(nextColor);

                if(isCheckmate(nextColor)){
                    // Checkmate: the side to move is mated, so the mover (other color) wins.
                    String winner = (nextColor == PieceColor.WHITE) ? "BLACK" : "WHITE";
                    String winnerName = (nextColor == PieceColor.WHITE) ? blackName : whiteName;
                    ServerClient.postResult(whiteName, blackName, winner);
                    JOptionPane.showMessageDialog(this,
                        winnerName + " wins by checkmate!", "Checkmate",
                        JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
                else if(isStalemate(nextColor)){
                    JOptionPane.showMessageDialog(this,
                        "Stalemate — it's a draw!", "Stalemate",
                        JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
                else if(isKingInCheck(nextColor)){
                    updateInfo(" " + nextName + " is in CHECK!\n\n It is " + nextName + "'s turn");
                }
                else{
                    updateInfo(" It is " + nextName + "'s turn");
                }

                // Trigger AI move if it is now the computer's turn
                if (computerPlayer != null) {
                    boolean aiTurn = (computerPlayer.getColor() == PieceColor.WHITE) == whiteTurn;
                    if (aiTurn) {
                        SwingUtilities.invokeLater(() -> {
                            Move aiMove = computerPlayer.getBestMove(this);
                            if (aiMove != null) {
                                makeMove(aiMove);
                                repaint();
                            }
                        });
                    }
                }
            }
        }
        else{
            move.piece.xpos = move.piece.col * ts;
            move.piece.ypos = move.piece.row * ts;
        }
    }

    /**
     * Validates whether a player's move is legal based on turn order and piece rules.
     * 
     * <p>This method retrieves the selected piece and the target square, checking if the move
     * is allowed given the current player's turn. It returns {@code false} if the player attempts
     * to move out of turn or capture their own piece. If those conditions pass, it defers to the
     * piece’s own {@link Piece#isValidMove(int, int, Board)} method to validate subclass-specific movement logic.</p>
     * 
     * @param move the {@link Move} object containing coordinates for current piece location 
     *             and destination location.
     * @return {@code true} if the move is valid; {@code false} otherwise
     */
    public boolean validMove(Move move){

        Piece piece = getPiece(move.oldCol, move.oldRow);
        Piece toPiece = getPiece(move.newCol, move.newRow);

        if((piece.getColor() == PieceColor.WHITE) && (!whiteTurn)){
            return false;
        }

        if((piece.getColor() == PieceColor.BLACK) && (whiteTurn)){
            return false;
        }

        if((toPiece != null) && (piece.getColor() == toPiece.getColor())){
            return false;
        }

        if(!piece.isValidMove(move.newCol, move.newRow, this)){
            return false;
        }

        // Simulate the move to verify it doesn't leave the king in check
        piece.col = move.newCol;
        piece.row = move.newRow;
        pieceList.remove(move.Capture);

        boolean leavesKingInCheck = isKingInCheck(piece.getColor());

        // Undo simulation
        piece.col = move.oldCol;
        piece.row = move.oldRow;
        if(move.Capture != null) pieceList.add(move.Capture);

        return !leavesKingInCheck;
    }

    /**
     * Captures an opposing piece and performs special chess logic such as promotion or game end.
     * 
     * <p>This method checks if the move results in a capture, pawn promotion, or game-ending
     * condition. If a {@link Pawn} reaches the opposite end of the board, it is promoted to a
     * {@link Queen}. If a {@link King} is captured, the game ends and the winning team is printed
     * to the terminal before exiting the program.</p>
     * 
     * @param move the {@link Move} object containing coordinates for current piece location 
     *             and destination location.
     */
    public void capture(Move move){

        if(move.piece instanceof Pawn && (move.newRow == 0 || move.newRow == 7)){
            pieceList.remove(move.Capture);
            pieceList.remove(move.piece);
            pieceList.add(new Queen(this, move.newCol, move.newRow, (move.piece.getColor())));
        }
        else{
            pieceList.remove(move.Capture);
        }
    }

    /**
     * Finds the King of the given color.
     *
     * @param color the color of the King to locate
     * @return the {@link King} piece, or {@code null} if it is not on the board
     */
    public Piece findKing(PieceColor color){
        for(Piece p : pieceList){
            if(p instanceof King && p.getColor() == color){
                return p;
            }
        }
        return null;
    }

    /**
     * Determines whether the King of the given color is currently in check, i.e.
     * attacked by at least one opposing piece on its current square.
     *
     * @param color the color of the King to test
     * @return {@code true} if the King is attacked by any opponent piece
     */
    public boolean isKingInCheck(PieceColor color){
        Piece king = findKing(color);
        if(king == null) return false;

        for(Piece p : pieceList){
            if(p.getColor() != color && p.isValidMove(king.col, king.row, this)){
                return true;
            }
        }
        return false;
    }

    /**
     * Lists the opposing pieces currently attacking {@code color}'s king, described
     * as {@code Name(col,row)}. Used only for diagnostic logging.
     *
     * @param color the color whose king's attackers to report
     * @return the attacking pieces, or an empty list if the king is safe/absent
     */
    public List<String> attackersOf(PieceColor color){
        List<String> attackers = new ArrayList<>();
        Piece king = findKing(color);
        if(king == null) return attackers;
        for(Piece p : pieceList){
            if(p.getColor() != color && p.isValidMove(king.col, king.row, this)){
                attackers.add(p.name + "(" + p.col + "," + p.row + ")");
            }
        }
        return attackers;
    }

    /**
     * Tests whether {@code move} is geometrically legal for its piece and does not
     * leave that piece's own king in check — <em>ignoring whose turn it is</em>.
     *
     * <p>This is the turn-independent core of legality used by checkmate/stalemate
     * detection and by search move generation. Keeping it independent of the
     * {@code whiteTurn} flag is deliberate: the previous detection routed through
     * {@link #validMove(Move)}, whose turn guard rejected every move whenever the
     * flag did not match the color under test, producing false checkmates. Callers
     * that must also enforce turn order (real user input) use {@code validMove}.</p>
     *
     * @param move the move to test (its piece's color is taken as the mover)
     * @return {@code true} if the move is pseudo-legal and leaves the mover's king safe
     */
    public boolean isLegalIgnoringTurn(Move move){
        Piece piece = move.piece;
        Piece toPiece = getPiece(move.newCol, move.newRow);

        // Cannot capture your own piece (also rejects a no-op move onto own square).
        if(toPiece != null && toPiece.getColor() == piece.getColor()) return false;
        if(!piece.isValidMove(move.newCol, move.newRow, this)) return false;

        // Simulate the move and confirm it does not leave the mover's king in check.
        int oldCol = piece.col;
        int oldRow = piece.row;
        if(toPiece != null) pieceList.remove(toPiece);
        piece.col = move.newCol;
        piece.row = move.newRow;

        boolean kingSafe = !isKingInCheck(piece.getColor());

        piece.col = oldCol;
        piece.row = oldRow;
        if(toPiece != null) pieceList.add(toPiece);

        return kingSafe;
    }

    /**
     * Returns whether {@code color} has at least one legal move that leaves its own
     * king safe. Turn-independent, so it is correct regardless of the
     * {@code whiteTurn} flag. Used to detect checkmate and stalemate.
     *
     * @param color the color to test
     * @return {@code true} if any king-safe move exists; {@code false} otherwise
     */
    public boolean hasLegalMoves(PieceColor color){
        for(Piece p : new ArrayList<>(pieceList)){
            if(p.getColor() != color) continue;
            for(int c = 0; c < 8; c++){
                for(int r = 0; r < 8; r++){
                    if(isLegalIgnoringTurn(new Move(this, p, c, r))) return true;
                }
            }
        }
        return false;
    }

    /**
     * Counts every legal king-safe move available to {@code color}. Used for
     * diagnostic logging; {@link #hasLegalMoves(PieceColor)} is cheaper when only
     * existence matters.
     *
     * @param color the color to count moves for
     * @return the number of legal moves
     */
    public int countLegalMoves(PieceColor color){
        int count = 0;
        for(Piece p : new ArrayList<>(pieceList)){
            if(p.getColor() != color) continue;
            for(int c = 0; c < 8; c++){
                for(int r = 0; r < 8; r++){
                    if(isLegalIgnoringTurn(new Move(this, p, c, r))) count++;
                }
            }
        }
        return count;
    }

    /**
     * Checkmate for {@code color}: its king is in check and it has no legal move
     * that leaves the king safe.
     *
     * @param color the side to test (the side to move)
     * @return {@code true} if {@code color} is checkmated
     */
    public boolean isCheckmate(PieceColor color){
        return isKingInCheck(color) && !hasLegalMoves(color);
    }

    /**
     * Stalemate for {@code color}: its king is <em>not</em> in check yet it has no
     * legal move — a draw, not a loss.
     *
     * @param color the side to test (the side to move)
     * @return {@code true} if {@code color} is stalemated
     */
    public boolean isStalemate(PieceColor color){
        return !isKingInCheck(color) && !hasLegalMoves(color);
    }

    /**
     * Prints a diagnostic snapshot of {@code color}'s king status: current player,
     * king position, whether the king is in check, the number of legal moves, and
     * any attacking pieces. Logged on every turn transition so false checkmate
     * calls can be traced to the exact board state that produced them.
     *
     * @param color the side to move whose status to report
     */
    private void debugKingStatus(PieceColor color){
        Piece king = findKing(color);
        String pos = (king == null) ? "none" : "(" + king.col + "," + king.row + ")";
        System.out.println("[CHECKMATE DEBUG] player=" + color
            + " king=" + pos
            + " inCheck=" + isKingInCheck(color)
            + " legalMoves=" + countLegalMoves(color)
            + " attackers=" + attackersOf(color));
    }

    /**
     * Records the state needed to undo a move applied during search by
     * {@link Board#makeSearchMove(Move)}. Returned by make, consumed by
     * {@link Board#unmakeSearchMove(Undo)}.
     */
    public static final class Undo {
        /** The piece that moved (the pawn, on a promotion). */
        private final Piece piece;
        /** The piece's column before the move. */
        private final int fromCol;
        /** The piece's row before the move. */
        private final int fromRow;
        /** The captured piece removed from the board, or {@code null}. */
        private final Piece captured;
        /** The queen substituted in on a promotion, or {@code null}. */
        private final Piece promoted;

        private Undo(Piece piece, int fromCol, int fromRow, Piece captured, Piece promoted) {
            this.piece = piece;
            this.fromCol = fromCol;
            this.fromRow = fromRow;
            this.captured = captured;
            this.promoted = promoted;
        }
    }

    /**
     * Applies {@code move} to the live board for search, returning an {@link Undo}
     * token that {@link #unmakeSearchMove(Undo)} uses to restore the previous state.
     *
     * <p>Unlike {@link #makeMove(Move)} this touches only the logical board state —
     * piece coordinates, the piece list, promotion, and the {@code whiteTurn} flag —
     * never screen coordinates, the {@code madeMove} flag, the UI, or game-end
     * dialogs. The captured piece is recomputed from the destination square rather
     * than read from {@link Move#Capture}, so the token is always consistent with
     * the board at make time. Promotion substitutes a {@link Queen} for a pawn that
     * reaches the back rank, mirroring {@link #capture(Move)}.</p>
     *
     * @param move the move to apply
     * @return an undo token; pass it to {@link #unmakeSearchMove(Undo)} to revert
     */
    public Undo makeSearchMove(Move move){
        Piece piece = move.piece;
        int fromCol = piece.col;
        int fromRow = piece.row;
        Piece captured = getPiece(move.newCol, move.newRow);
        if(captured != null) pieceList.remove(captured);

        piece.col = move.newCol;
        piece.row = move.newRow;

        Piece promoted = null;
        if(piece instanceof Pawn && (move.newRow == 0 || move.newRow == 7)){
            promoted = new Queen(this, move.newCol, move.newRow, piece.getColor());
            pieceList.remove(piece);
            pieceList.add(promoted);
        }

        whiteTurn = !whiteTurn;
        return new Undo(piece, fromCol, fromRow, captured, promoted);
    }

    /**
     * Reverts a move previously applied by {@link #makeSearchMove(Move)}, exactly
     * undoing the turn flip, any promotion, the piece's position, and any capture.
     *
     * @param u the undo token returned by the matching {@code makeSearchMove} call
     */
    public void unmakeSearchMove(Undo u){
        whiteTurn = !whiteTurn;
        if(u.promoted != null){
            pieceList.remove(u.promoted);
            pieceList.add(u.piece);
        }
        u.piece.col = u.fromCol;
        u.piece.row = u.fromRow;
        if(u.captured != null) pieceList.add(u.captured);
    }

    /**
     * Generates every legal move for {@code color} in the current position.
     *
     * <p>Iterates a snapshot of the piece list (so the temporary mutation during
     * check simulation is safe) and tests every destination square via
     * {@link #isLegalIgnoringTurn(Move)}, so generation is correct for either color
     * regardless of the {@code whiteTurn} flag — required because the search makes
     * moves for both sides.</p>
     *
     * @param color the side to move
     * @return all legal moves for {@code color}
     */
    public List<Move> generateLegalMoves(PieceColor color){
        List<Move> moves = new ArrayList<>();
        for(Piece p : new ArrayList<>(pieceList)){
            if(p.getColor() != color) continue;
            for(int c = 0; c < 8; c++){
                for(int r = 0; r < 8; r++){
                    Move m = new Move(this, p, c, r);
                    if(isLegalIgnoringTurn(m)) moves.add(m);
                }
            }
        }
        return moves;
    }

    /**
     * Generates the legal capturing moves for {@code color} — those landing on an
     * occupied square. Used by the quiescence search to resolve exchanges.
     *
     * @param color the side to move
     * @return all legal capture moves for {@code color}
     */
    public List<Move> generateCaptureMoves(PieceColor color){
        List<Move> moves = new ArrayList<>();
        for(Piece p : new ArrayList<>(pieceList)){
            if(p.getColor() != color) continue;
            for(int c = 0; c < 8; c++){
                for(int r = 0; r < 8; r++){
                    if(getPiece(c, r) == null) continue; // captures only
                    Move m = new Move(this, p, c, r);
                    if(isLegalIgnoringTurn(m)) moves.add(m);
                }
            }
        }
        return moves;
    }

    /**
     * Sets the info area to the given status line, then re-appends each side's
     * most recent move grade so the grades persist across turn-status updates.
     *
     * <p>Without this, a fresh {@code setText} for the next player's turn would
     * erase the grade just shown — in computer mode the AI's immediate reply
     * would wipe the human's grade before it could be read.</p>
     *
     * @param status the status line to show above the move grades
     */
    private void updateInfo(String status){
        StringBuilder sb = new StringBuilder(status);
        if(!whiteGrade.isEmpty()) sb.append("\n " + whiteName + "'s move: " + whiteGrade);
        if(!blackGrade.isEmpty()) sb.append("\n " + blackName + "'s move: " + blackGrade);
        infoArea.setText(sb.toString());
    }

    /**
     * Maps a move's centipawn loss to a human-readable quality label.
     *
     * @param loss the centipawn loss (best achievable score minus the chosen move's score)
     * @return a short label describing the move's quality
     */
    private String gradeLabel(int loss){
        if(loss <= 0)   return "Best move ⭐";
        if(loss <= 30)  return "Excellent";
        if(loss <= 90)  return "Good";
        if(loss <= 200) return "Inaccuracy ?!";
        if(loss <= 400) return "Mistake ?";
        return "Blunder ??";
    }

}
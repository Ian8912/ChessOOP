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

    /** The JTextArea component used to display information associated with the board.*/
    private JTextArea infoArea;

    /** Names of the two players, set before the game starts. */
    private String whiteName = "White";
    private String blackName = "Black";

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
                move.piece.col = move.newCol;
                move.piece.row = move.newRow;

                move.piece.xpos = move.newCol * ts;
                move.piece.ypos = move.newRow * ts;

                capture(move);

                whiteTurn = !whiteTurn;

                PieceColor nextColor = whiteTurn ? PieceColor.WHITE : PieceColor.BLACK;
                String nextName = whiteTurn ? whiteName : blackName;

                if(!hasLegalMoves(nextColor)){
                    if(isKingInCheck(nextColor)){
                        String winner = whiteTurn ? "BLACK" : "WHITE";
                        String winnerName = whiteTurn ? blackName : whiteName;
                        ServerClient.postResult(whiteName, blackName, winner);
                        JOptionPane.showMessageDialog(this,
                            winnerName + " wins by checkmate!", "Checkmate",
                            JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
                    }
                    else{
                        JOptionPane.showMessageDialog(this,
                            "Stalemate — it's a draw!", "Stalemate",
                            JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
                    }
                }
                else if(isKingInCheck(nextColor)){
                    infoArea.setText(" " + nextName + " is in CHECK!\n\n It is " + nextName + "'s turn");
                }
                else{
                    infoArea.setText(" It is " + nextName + "'s turn");
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
     * Determines whether the King of the given color is currently in check.
     *
     * @param color the color of the King to test
     * @return {@code true} if the King is attacked by any opponent piece
     */
    public boolean isKingInCheck(PieceColor color){
        Piece king = null;
        for(Piece p : pieceList){
            if(p instanceof King && p.getColor() == color){
                king = p;
                break;
            }
        }
        if(king == null) return false;

        for(Piece p : pieceList){
            if(p.getColor() != color && p.isValidMove(king.col, king.row, this)){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether the given color has at least one legal move available.
     * Used to detect checkmate and stalemate.
     *
     * @param color the color to test
     * @return {@code true} if any legal move exists; {@code false} if it's checkmate or stalemate
     */
    public boolean hasLegalMoves(PieceColor color){
        for(Piece p : new ArrayList<>(pieceList)){
            if(p.getColor() == color){
                for(int c = 0; c < 8; c++){
                    for(int r = 0; r < 8; r++){
                        Move m = new Move(this, p, c, r);
                        if(validMove(m)) return true;
                    }
                }
            }
        }
        return false;
    }

}
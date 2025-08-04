package Game;
import Piece.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

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

    private JTextArea infoArea;

    /** Handles mouse input events and piece interaction. */
    public Input in = new Input(this);

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

    public void setInfoArea(JTextArea infoArea){
        this.infoArea = infoArea;
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

        for (int r = 0; r < row; r++)
            for (int c = 0; c < col; c++)
            {
                g2.setColor((c+r) % 2 == 0 ? new Color(255, 255, 255) : new Color(122, 173, 107) );
                g2.fillRect(r* ts, c*ts, ts, ts);
            }
        
        for (Piece piece : pieceList)
        {
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
        for (Piece piece : pieceList)
        {
            if (piece.col == col && piece.row == row)
                return piece;
        }
        return null;
    }

    /**
     * Executes a player's move if it is valid; otherwise resets the piece's position.
     * 
     * <p>This method receives a {@link Move} object containing the piece's
     * current position ("from") and target position ("to"). If the move is
     * valid, the piece's board and screen coordinates are updated, a capture
     * is performed if necessary, and the turn is switched. If the move is
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

                if(whiteTurn){
                    infoArea.setText("It is White's Turn");
                }
                else{
                    infoArea.setText("It is Black's Turn");
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

        return piece.isValidMove(move.newCol, move.newRow, this);
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
        } else{
            pieceList.remove(move.Capture);
        }
        if(move.Capture instanceof King){
            System.out.println();
            if(move.piece.getColor() == PieceColor.WHITE){
                System.out.println("White team wins!\n");
            }
            else{
                System.out.println("Black team wins!\n");
            }
            System.exit(0);
        }
    }

}

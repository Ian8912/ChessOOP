package Piece;
import Game.Board;
import java.awt.image.BufferedImage;

/**
 * Represents a Bishop chess piece with movement logic restricted to diagonals.
 *
 * <p>This class handles the initialization, rendering, and valid move logic
 * for a bishop. The bishop moves diagonally across the board and can travel
 * any number of tiles as long as its path is unobstructed.</p>
 *
 * @see Piece
 * @see Board
 */
public class Bishop extends Piece {

    /**
     * Constructs a Bishop piece at the specified board position and color.
     *
     * <p>Initializes the bishop's coordinates, screen position, name, and sprite
     * based on its color and location on the {@link Board}.</p>
     *
     * @param board the board the bishop belongs to
     * @param col the starting column of the bishop
     * @param row the starting row of the bishop
     * @param color the {@link PieceColor} of the bishop (white or black)
     */
    public Bishop(Board board, int col, int row, PieceColor color){

        /** Call superclass constructor to set base piece properties. */
        super(board);

        /** Sets the board grid column position. */
        this.col = col;
        /** Sets the board grid row position. */
        this.row = row;

        /** Sets on-screen pixel column position. */
        this.xpos = col * board.ts;
        /** Sets on-screen pixel row position. */
        this.ypos = row * board.ts;

        /** Assigns the piece color. */
        this.color = color;

        /** Assigns the piece name. */
        this.name = "Bishop";

        /** Extract and scale the bishop sprite from the sprite sheet. Uses different
         *  row in the sprite sheet depending on piece color. */
        this.sprite = sT.getSubimage( 2 * sTScale, this.color == PieceColor.WHITE ? 0 : sTScale, sTScale, sTScale).getScaledInstance(board.ts, board.ts, BufferedImage.SCALE_SMOOTH);
    }

    /**
     * Determines whether a move to the specified coordinates is valid for a bishop.
     *
     * <p>A bishop can move any number of tiles diagonally, provided no pieces
     * obstruct the path. This method checks for all four diagonal directions:
     * up-right, up-left, down-right, and down-left.</p>
     *
     * @param toCol the destination column
     * @param toRow the destination row
     * @param board the current game {@link Board} to evaluate piece positions
     * @return {@code true} if the move is valid; {@code false} otherwise
     */
    @Override
    public boolean isValidMove(int toCol, int toRow, Board board){
        // Checks for diagonal moves, up and to the right
        for (int i = 1; this.row - i > -1 && this.col + i < 8; i++) {
            if(board.getPiece(this.col+i, this.row-i) != null && 
            this.row - i == toRow && this.col + i == toCol) {
                return true;
            }
            else if(board.getPiece(this.col+i, this.row-i) != null && 
            this.row - i > toRow && this.col + i < toCol) {
                return false;
            }
            else if(this.row - i == toRow && this.col + i == toCol) {
                return true;
            }
        }
        // Checks for diagonal moves, up and to the left
        for (int i = 1; this.row - i > -1 && this.col - i > -1; i++) {
            if(board.getPiece(this.col-i, this.row-i) != null && 
            this.row - i == toRow && this.col - i == toCol) {
                return true;
            }
            else if(board.getPiece(this.col-i, this.row-i) != null && 
            this.row - i > toRow && this.col - i > toCol) {
                return false;
            }
            else if(this.row - i == toRow && this.col - i == toCol) {
                return true;
            }
        }
        // Checks for diagonal moves, down and to the right
        for (int i = 1; this.row + i < 8 && this.col + i < 8; i++) {
            if(board.getPiece(this.col+i, this.row+i) != null && 
            this.row + i == toRow && this.col + i == toCol) {
                return true;
            }
            else if(board.getPiece(this.col+i, this.row+i) != null && 
            this.row + i < toRow && this.col + i < toCol) {
                return false;
            }
            else if(this.row + i == toRow && this.col + i == toCol) {
                return true;
            }
        }
        // Checks for diagonal moves, down and to the left
        for (int i = 1; this.row + i < 8 && this.col - i < 8; i++) {
            if(board.getPiece(this.col-i, this.row+i) != null && 
            this.row + i == toRow && this.col - i == toCol) {
                return true;
            }
            else if(board.getPiece(this.col-i, this.row+i) != null && 
            this.row + i < toRow && this.col - i > toCol) {
                return false;
            }
            else if(this.row + i == toRow && this.col - i == toCol) {
                return true;
            }
        }
        return false;
    }
}

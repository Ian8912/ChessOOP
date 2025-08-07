package Piece;
import Game.Board;
import java.awt.image.BufferedImage;

/**
 * Represents a Queen chess piece with movement logic that can travel any number
 * of tiles omnidirectional on chess board.
 *
 * <p>This class handles the initialization, rendering, and valid move logic
 * for a queen. The queen can move as many tiles omnidirectional across the board
 * as long as its path is unobstructed.</p>
 *
 * @see Piece
 * @see Board
 */
public class Queen extends Piece {

    /**
     * Constructs a Queen piece at the specified board position and color.
     *
     * <p>Initializes the queen's coordinates, screen position, name, and sprite
     * based on its color and location on the {@link Board}.</p>
     *
     * @param board the board the pawn belongs to
     * @param col the starting column of the queen
     * @param row the starting row of the queen
     * @param color the {@link PieceColor} of the queen (white or black)
     */
    public Queen(Board board, int col, int row, PieceColor color){

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
        this.name = "Queen";

        /** Extract and scale the queen sprite from the sprite sheet. Uses different
         *  row in the sprite sheet depending on piece color. */
        this.sprite = sT.getSubimage(1 * sTScale, this.color == PieceColor.WHITE ? 0 : sTScale, sTScale, sTScale).getScaledInstance(board.ts, board.ts, BufferedImage.SCALE_SMOOTH);
    }

    /**
     * Determines whether a move to the specified coordinates is valid for a Queen.
     *
     * <p>A queen can move as many tiles omnidirectional provided no pieces
     * obstruct the path. This method checks for logic.</p>
     *
     * @param toCol the destination column
     * @param toRow the destination row
     * @param board the current game {@link Board} to evaluate piece positions
     * @return {@code true} if the move is valid; {@code false} otherwise
     */
    @Override
    public boolean isValidMove(int toCol, int toRow, Board board){
        if((this.row != toRow && this.col == toCol) || 
        (this.row == toRow && this.col != toCol)){

            // Checks for Upward Moves
            for (int i = 1; this.row - i > -1; i++) {
                if(board.getPiece(this.col, this.row-i) != null && 
                this.row - i == toRow) {
                    return true;
                }
                else if(board.getPiece(this.col, this.row-i) != null && 
                this.row - i > toRow) {
                    return false;
                }
                else if(this.row - i == toRow) {
                    return true;
                }
            }
            // Checks for Downward Moves
            for (int i = 1; this.row + i < 8; i++) {
                if(board.getPiece(this.col, this.row+i) != null && 
                this.row + i == toRow) {
                    return true;
                }
                else if(board.getPiece(this.col, this.row+i) != null && 
                this.row + i < toRow) {
                    return false;
                }
                else if(this.row + i == toRow) {
                    return true;
                }
            }
            // Checks for Rightward Moves
            for (int i = 1; this.col + i < 8; i++) {
                if(board.getPiece(this.col+i, this.row) != null && 
                this.col + i == toCol) {
                    return true;
                }
                else if(board.getPiece(this.col+i, this.row) != null && 
                this.col + i < toCol) {
                    return false;
                }
                else if(this.col + i == toCol) {
                    return true;
                }
            }
            // Checks for Leftward Moves
            for (int i = 1; this.col - i > -1; i++) {
                if(board.getPiece(this.col-i, this.row) != null && 
                this.col - i == toCol) {
                    return true;
                }
                else if(board.getPiece(this.col-i, this.row) != null && 
                this.col - i > toCol) {
                    return false;
                }
                else if(this.col - i == toCol) {
                    return true;
                }
            }
        }
        else{
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
        }   
        return false;
    }
}

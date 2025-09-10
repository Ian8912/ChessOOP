package Piece;
import Game.Board;
import java.awt.image.BufferedImage;

/**
 * Represents a Rook chess piece with movement logic restricted to move vertically
 * or horizontally.
 *
 * <p>This class handles the initialization, rendering, and valid move logic
 * for a rook. The rook moves vertically or horizontally across the board and can travel
 * any number of tiles as long as its path is unobstructed.</p>
 *
 * @see Piece
 * @see Board
 */
public class Rook extends Piece {

    /**
     * Constructs a Rook piece at the specified board position and color.
     *
     * <p>Initializes the rook's coordinates, screen position, name, and sprite
     * based on its color and location on the {@link Board}.</p>
     *
     * @param board the board the rook belongs to
     * @param col the starting column of the rook
     * @param row the starting row of the rook
     * @param color the {@link PieceColor} of the rook (white or black)
     */
    public Rook(Board board, int col, int row, PieceColor color){

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
        this.name = "Rook";

        /** Extract and scale the rook sprite from the sprite sheet. Uses different
         *  row in the sprite sheet depending on piece color. */
        this.sprite = sT.getSubimage(4 * sTScale, this.color == PieceColor.WHITE ? 0 : sTScale, sTScale, sTScale).getScaledInstance(board.ts, board.ts, BufferedImage.SCALE_SMOOTH);
    }

    /**
     * Determines whether a move to the specified coordinates is valid for a Rook.
     *
     * <p>A rook can move as many tiles vertically or horizontally provided no pieces
     * obstruct the path. This method checks for logic.</p>
     *
     * @param toCol the destination column
     * @param toRow the destination row
     * @param board the current game {@link Board} to evaluate piece positions
     * @return {@code true} if the move is valid; {@code false} otherwise
     */
    @Override
    public boolean isValidMove(int toCol, int toRow, Board board){

        boolean openPath = true;
        // Checks for Rightward Moves
        if(toRow == this.row && this.col < toCol){
            for(int i = this.col + 1; i < toCol; i++){
                if(board.getPiece(i, this.row ) != null){
                    openPath = false;
                }
            }
        }

        // Checks for Leftward Moves
        if(toRow == this.row  && this.col > toCol){
            for(int i = this.col - 1; i > toCol; i--){
                if(board.getPiece(i, this.row ) != null){
                    openPath = false;
                }
            }
        }

        // Checks for Upward Moves
        if(toRow < this.row  && this.col == toCol){
            for(int i = this.row  - 1; i > toRow; i--){
                if(board.getPiece(this.col, i) != null){
                    openPath = false;
                }
            }
        }

        // Checks for Downward Moves
        if(toRow > this.row  && this.col == toCol){
            for(int i = this.row  + 1; i < toRow; i++){
                if(board.getPiece(this.col, i) != null){
                    openPath = false;
                }
            }
        }

        // Checks for unobstructed path, then moves
        if((((this.row  == toRow) && (this.col != toCol)) || ((this.row  != toRow) && (this.col == toCol)))  && (openPath)){
            return true;
        }
        return false;
    }

}
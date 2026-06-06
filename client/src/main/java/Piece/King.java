package Piece;
import java.awt.image.BufferedImage;

import Game.Board;

/**
 * Represents a King chess piece with movement logic restricted to 1 chess board
 * tile omnidirectional.
 *
 * <p>This class handles the initialization, rendering, and valid move logic
 * for a king. The king moves 1 tile omnidirectional across the board
 * as long as its path is unobstructed.</p>
 *
 * @see Piece
 * @see Board
 */
public class King extends Piece {

    /**
     * Constructs a King piece at the specified board position and color.
     *
     * <p>Initializes the king's coordinates, screen position, name, and sprite
     * based on its color and location on the {@link Board}.</p>
     *
     * @param board the board the king belongs to
     * @param col the starting column of the king
     * @param row the starting row of the king
     * @param color the {@link PieceColor} of the king (white or black)
     */
    public King(Board board, int col, int row, PieceColor color){

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
        this.name = "King";

        /**
         * Extract and scale the king sprite from the sprite sheet. Uses different
         * row in the sprite sheet depending on piece color.
         */
        this.sprite = sT.getSubimage(0 * sTScale, this.color == PieceColor.WHITE ? 0 : sTScale, sTScale, sTScale).getScaledInstance(board.ts, board.ts, BufferedImage.SCALE_SMOOTH);
    }

    /**
     * Determines whether a move to the specified coordinates is valid for a King.
     *
     * <p>A king can move 1 tile omnidirectional, provided no pieces
     * obstruct the path. This method checks for all directions.</p>
     *
     * @param toCol the destination column
     * @param toRow the destination row
     * @param board the current game {@link Board} to evaluate piece positions
     * @return {@code true} if the move is valid; {@code false} otherwise
     */
    @Override
    public boolean isValidMove(int toCol, int toRow, Board board){

        // Checks for the possible moves
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
            
                if (i == 0 && j == 0) {
                    continue;
                }
    
                int newRow = this.row + i;
                int newCol = this.col + j;
            
                if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                    if(newRow == toRow && newCol == toCol){
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
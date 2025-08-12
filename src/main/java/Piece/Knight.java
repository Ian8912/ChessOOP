package Piece;
import Game.Board;
import java.awt.image.BufferedImage;

/**
 * Represents a Knight chess piece with movement logic in an "L" shape directed on the
 * chess board.
 *
 * <p>This class handles the initialization, rendering, and valid move logic
 * for a knight. The knight moves in an "L" shape restricted to 2 squares in
 * one direction (either vertically or horizontally) and then 1 square perpendicularly
 * to that direction on the chess board.</p>
 *
 * @see Piece
 * @see Board
 */
public class Knight extends Piece {

    /**
     * Constructs a Knight piece at the specified board position and color.
     *
     * <p>Initializes the knight's coordinates, screen position, name, and sprite
     * based on its color and location on the {@link Board}.</p>
     *
     * @param board the board the knight belongs to
     * @param col the starting column of the knight
     * @param row the starting row of the knight
     * @param color the {@link PieceColor} of the knight (white or black)
     */
    public Knight(Board board, int col, int row, PieceColor color){

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
        this.name = "Knight";

        /**
         * Extract and scale the knight sprite from the sprite sheet. Uses different
         * row in the sprite sheet depending on piece color.
         */
        this.sprite = sT.getSubimage(3 * sTScale, this.color == PieceColor.WHITE ? 0 : sTScale, sTScale, sTScale).getScaledInstance(board.ts, board.ts, BufferedImage.SCALE_SMOOTH);
    }

    /**
     * Determines whether a move to the specified coordinates is valid for a Knight.
     *
     * <p>A knight can move in an "L" shape restricted to 2 squares in
     * tne direction (either vertically or horizontally) and then 1 square perpendicularly
     * to that direction on the chess board. This method checks for logic.</p>
     *
     * @param toCol the destination column
     * @param toRow the destination row
     * @param board the current game {@link Board} to evaluate piece positions
     * @return {@code true} if the move is valid; {@code false} otherwise
     */
    @Override
    public boolean isValidMove(int toCol, int toRow, Board board){

        int[] rowOffsets = {-2, -1, 1, 2, 2, 1, -1, -2};
        int[] colOffsets = {1, 2, 2, 1, -1, -2, -2, -1};

        for(int i = 0; i < 8; i++){
            int newRow = this.row + rowOffsets[i];
            int newCol = this.col + colOffsets[i];

            if(newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8){
                if(newRow == toRow && newCol == toCol){
                    return true;
                }
            }
        }
        return false;
    }

}
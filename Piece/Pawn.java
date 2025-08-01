package Piece;
import Game.Board;
import java.awt.image.BufferedImage;

/**
 * Represents a Pawn chess piece with movement logic with 2 squares (on initial move)
 * or 1 square forward on the chess board.
 *
 * <p>This class handles the initialization, rendering, and valid move logic
 * for a pawn. The pawn moves 2 squares (on initial move) or 1 square forward
 * on the chess board. A capture is possible 1 square diagonally left or right
 * forward facing.</p>
 *
 * @see Piece
 * @see Board
 */
public class Pawn extends Piece {

    /**
     * Constructs a Pawn piece at the specified board position and color.
     *
     * <p>Initializes the pawn's coordinates, screen position, name, and sprite
     * based on its color and location on the {@link Board}.</p>
     *
     * @param board the board the pawn belongs to
     * @param col the starting column of the pawn
     * @param row the starting row of the pawn
     * @param color the {@link PieceColor} of the pawn (white or black)
     */
    public Pawn(Board board, int col, int row, PieceColor color){

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
        this.name = "Pawn";

        /**
         * Extract and scale the pawn sprite from the sprite sheet. Uses different
         * row in the sprite sheet depending on piece color.
         */
        this.sprite = sT.getSubimage(5 * sTScale, this.color == PieceColor.WHITE ? 0 : sTScale, sTScale, sTScale).getScaledInstance(board.ts, board.ts, BufferedImage.SCALE_SMOOTH);
    }

    /**
     * Determines whether a move to the specified coordinates is valid for a Pawn.
     *
     * <p>A pawn can move 2 squares (on initial move tracked by {@link Piece#readMadeMove()})
     * or 1 square forward on the chess board. A capture is possible 1 square diagonally
     * left or right forward facing. This method checks for logic.</p>
     *
     * @param toCol the destination column
     * @param toRow the destination row
     * @param board the current game {@link Board} to evaluate piece positions
     * @return {@code true} if the move is valid; {@code false} otherwise
     */
    @Override
    public boolean isValidMove(int toCol, int toRow, Board board) {
        Piece toPiece = board.getPiece(toCol, toRow);

        // Checks white pawn piece, then moves upwards
        if(this.getColor() == PieceColor.WHITE){
            if(!this.readMadeMove()){
                if((this.row - 1 == toRow) && (this.col + 1 == toCol || this.col - 1 == toCol) && (toPiece != null)){
                    this.switchMadeMove();
                    return true;
                }
                else if((this.row - toRow < 3) && (this.row - toRow > -1) && (toCol == this.col) && (toPiece == null)){
                    if(board.getPiece(this.col, this.row - 1) != null){
                        return false;
                    }
                    this.switchMadeMove();
                    return true;
                }
            }
            else{
                if((this.row - 1 == toRow) && (this.col + 1 == toCol || this.col - 1 == toCol) && (toPiece != null)){
                    return true;
                }
                else if((this.row - toRow == 1) && (toCol == this.col) && (toPiece == null)){
                    return true;
                }
            }
        }
        // Checks black pawn piece, then moves downwards
        else{
            if(!this.readMadeMove()){
                if((this.row + 1 == toRow) && (this.col + 1 == toCol || this.col - 1 == toCol) && (toPiece != null)){
                    return true;
                }
                else if((this.row - toRow > -3) && (this.row - toRow < 1) && (toCol == this.col) && (toPiece == null)){
                    this.switchMadeMove();
                    return true;
                }
            }
            else{
                if((this.row + 1 == toRow) && (this.col + 1 == toCol || this.col - 1 == toCol) && (toPiece != null)){
                    return true;
                }
                else if((this.row - toRow > -2) && (this.row - toRow < 1) && (toCol == this.col) && (toPiece == null)){
                    return true;
                }
        }
    }
        return false;
    }
}

package Piece;
import Game.Board;
import java.awt.image.BufferedImage;


public class Bishop extends Piece{
    public Bishop(Board board, int col, int row, boolean isWhite)
    {
        super(board);
        this.col = col;
        this.row = row;
        this.xpos = col * board.ts;
        this.ypos = row * board.ts;

        this.isWhite = isWhite;
        this.name = "Bishop";

        
        this.sprite = sT.getSubimage( 2 * sTScale, isWhite ? 0 : sTScale, sTScale, sTScale).getScaledInstance(board.ts, board.ts, BufferedImage.SCALE_SMOOTH);
    }

}
package Piece;
import Game.Board;
import java.awt.image.BufferedImage;



public class Knight extends Piece{
    public Knight(Board board, int col, int row, boolean isWhite)
    {
        super(board);
        this.col = col;
        this.row = row;
        this.xpos = col * board.ts;
        this.ypos = row * board.ts;

        this.isWhite = isWhite;
        this.name = "Knight";
        
        this.sprite = sT.getSubimage(3 * sTScale, isWhite ? 0 : sTScale, sTScale, sTScale).getScaledInstance(board.ts, board.ts, BufferedImage.SCALE_SMOOTH);
    }

}

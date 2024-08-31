package Piece;
import Game.Board;
import java.awt.image.BufferedImage;


public class Rook extends Piece{
    public Rook(Board board, int col, int row, boolean isWhite)
    {
        super(board);
        this.col = col;
        this.row = row;
        this.xpos = col * board.ts;
        this.ypos = row * board.ts;

        this.isWhite = isWhite;
        this.name = "Rook";

        
        this.sprite = sT.getSubimage(4 * sTScale, isWhite ? 0 : sTScale, sTScale, sTScale).getScaledInstance(board.ts, board.ts, BufferedImage.SCALE_SMOOTH);
    }

}

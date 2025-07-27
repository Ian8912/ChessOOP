package Piece;
import Game.Board;
import java.awt.image.BufferedImage;

public class Pawn extends Piece {
    
    public Pawn(Board board, int col, int row, PieceColor color)
    {
        super(board);
        this.col = col;
        this.row = row;
        this.xpos = col * board.ts;
        this.ypos = row * board.ts;

        this.color = color;
        this.name = "Pawn";
        

        this.sprite = sT.getSubimage(5 * sTScale, this.color == PieceColor.WHITE ? 0 : sTScale, sTScale, sTScale).getScaledInstance(board.ts, board.ts, BufferedImage.SCALE_SMOOTH);

        
    }
    
    
}

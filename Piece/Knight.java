package Piece;
import Game.Board;
import java.awt.image.BufferedImage;



public class Knight extends Piece{
    public Knight(Board board, int col, int row, PieceColor color)
    {
        super(board);
        this.col = col;
        this.row = row;
        this.xpos = col * board.ts;
        this.ypos = row * board.ts;

        this.color = color;
        this.name = "Knight";
        
        this.sprite = sT.getSubimage(3 * sTScale, this.color == PieceColor.WHITE ? 0 : sTScale, sTScale, sTScale).getScaledInstance(board.ts, board.ts, BufferedImage.SCALE_SMOOTH);
    }

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

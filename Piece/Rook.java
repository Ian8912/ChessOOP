package Piece;
import Game.Board;
import java.awt.image.BufferedImage;


public class Rook extends Piece{
    public Rook(Board board, int col, int row, PieceColor color)
    {
        super(board);
        this.col = col;
        this.row = row;
        this.xpos = col * board.ts;
        this.ypos = row * board.ts;

        this.color = color;
        this.name = "Rook";

        
        this.sprite = sT.getSubimage(4 * sTScale, this.color == PieceColor.WHITE ? 0 : sTScale, sTScale, sTScale).getScaledInstance(board.ts, board.ts, BufferedImage.SCALE_SMOOTH);
    }

    @Override
    public boolean isValidMove(int toCol, int toRow, Board board){
        boolean openPath = true;
        if(toRow == this.row && this.col < toCol){
            for(int i = this.col + 1; i < toCol; i++){
                if(board.getPiece(i, this.row ) != null){
                    openPath = false;
                }
            }
        }
        if(toRow == this.row  && this.col > toCol){
            for(int i = this.col - 1; i > toCol; i--){
                if(board.getPiece(i, this.row ) != null){
                    openPath = false;
                }
            }
        }
        if(toRow < this.row  && this.col == toCol){
            for(int i = this.row  - 1; i > toRow; i--){
                if(board.getPiece(this.col, i) != null){
                    openPath = false;
                }
            }
        }
        if(toRow > this.row  && this.col == toCol){
            for(int i = this.row  + 1; i < toRow; i++){
                if(board.getPiece(this.col, i) != null){
                    openPath = false;
                }
            }
        }
        if((((this.row  == toRow) && (this.col != toCol)) || ((this.row  != toRow) && (this.col == toCol)))  && (openPath)){
            return true;
        }
        return false;
    }

}

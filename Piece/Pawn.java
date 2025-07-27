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

    @Override
    public boolean isValidMove(int toCol, int toRow, Board board) {
        Piece toPiece = board.getPiece(toCol, toRow);
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

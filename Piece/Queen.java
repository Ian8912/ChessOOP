package Piece;
import Game.Board;
import java.awt.image.BufferedImage;

public class Queen extends Piece{
    public Queen(Board board, int col, int row, PieceColor color)
    {
        super(board);
        this.col = col;
        this.row = row;
        this.xpos = col * board.ts;
        this.ypos = row * board.ts;

        this.color = color;
        this.name = "Queen";

        
        this.sprite = sT.getSubimage(1 * sTScale, this.color == PieceColor.WHITE ? 0 : sTScale, sTScale, sTScale).getScaledInstance(board.ts, board.ts, BufferedImage.SCALE_SMOOTH);
    }

    @Override
    public boolean isValidMove(int toCol, int toRow, Board board){
        if((this.row != toRow && this.col == toCol) || 
        (this.row == toRow && this.col != toCol)){

            // checks for Upward Moves
            for (int i = 1; this.row - i > -1; i++) {
                if(board.getPiece(this.col, this.row-i) != null && 
                this.row - i == toRow) {
                    return true;
                }
                else if(board.getPiece(this.col, this.row-i) != null && 
                this.row - i > toRow) {
                    return false;
                }
                else if(this.row - i == toRow) {
                    return true;
                }
            }
            // checks for Downward Moves
            for (int i = 1; this.row + i < 8; i++) {
                if(board.getPiece(this.col, this.row+i) != null && 
                this.row + i == toRow) {
                    return true;
                }
                else if(board.getPiece(this.col, this.row+i) != null && 
                this.row + i < toRow) {
                    return false;
                }
                else if(this.row + i == toRow) {
                    return true;
                }
            }
            // checks for Rightward Moves
            for (int i = 1; this.col + i < 8; i++) {
                if(board.getPiece(this.col+i, this.row) != null && 
                this.col + i == toCol) {
                    return true;
                }
                else if(board.getPiece(this.col+i, this.row) != null && 
                this.col + i < toCol) {
                    return false;
                }
                else if(this.col + i == toCol) {
                    return true;
                }
            }
            // checks for Leftward Moves
            for (int i = 1; this.col - i > -1; i++) {
                if(board.getPiece(this.col-i, this.row) != null && 
                this.col - i == toCol) {
                    return true;
                }
                else if(board.getPiece(this.col-i, this.row) != null && 
                this.col - i > toCol) {
                    return false;
                }
                else if(this.col - i == toCol) {
                    return true;
                }
            }
        }
        else{
            // checks for diagonal moves, up and to the right
            for (int i = 1; this.row - i > -1 && this.col + i < 8; i++) {
                if(board.getPiece(this.col+i, this.row-i) != null && 
                this.row - i == toRow && this.col + i == toCol) {
                    return true;
                }
                else if(board.getPiece(this.col+i, this.row-i) != null && 
                this.row - i > toRow && this.col + i < toCol) {
                    return false;
                }
                else if(this.row - i == toRow && this.col + i == toCol) {
                    return true;
                }
            }
            // checks for diagonal moves, up and to the left
            for (int i = 1; this.row - i > -1 && this.col - i > -1; i++) {
                if(board.getPiece(this.col-i, this.row-i) != null && 
                this.row - i == toRow && this.col - i == toCol) {
                    return true;
                }
                else if(board.getPiece(this.col-i, this.row-i) != null && 
                this.row - i > toRow && this.col - i > toCol) {
                    return false;
                }
                else if(this.row - i == toRow && this.col - i == toCol) {
                    return true;
                }
            }
            // checks for diagonal moves, down and to the right
            for (int i = 1; this.row + i < 8 && this.col + i < 8; i++) {
                if(board.getPiece(this.col+i, this.row+i) != null && 
                this.row + i == toRow && this.col + i == toCol) {
                    return true;
                }
                else if(board.getPiece(this.col+i, this.row+i) != null && 
                this.row + i < toRow && this.col + i < toCol) {
                    return false;
                }
                else if(this.row + i == toRow && this.col + i == toCol) {
                    return true;
                }
            }
            // checks for diagonal moves, down and to the left
            for (int i = 1; this.row + i < 8 && this.col - i < 8; i++) {
                if(board.getPiece(this.col-i, this.row+i) != null && 
                this.row + i == toRow && this.col - i == toCol) {
                    return true;
                }
                else if(board.getPiece(this.col-i, this.row+i) != null && 
                this.row + i < toRow && this.col - i > toCol) {
                    return false;
                }
                else if(this.row + i == toRow && this.col - i == toCol) {
                    return true;
                }
            }
        }   
        return false;
    }

}

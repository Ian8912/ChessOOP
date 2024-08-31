package Game;
import Piece.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class Board extends JPanel{

    public int ts = 85;

    private  final int col = 8;
    private final int row = 8;
    
    private ArrayList<Piece> pieceList = new ArrayList<>();
    
    public Piece selPiece;

    private boolean whiteTurn = true;

    public Input in = new Input(this);

    public Board(){
        this.setPreferredSize(new Dimension(col * ts , row * ts));

        this.addMouseListener(in);
        this.addMouseMotionListener(in);

    }

    public void addPieces()
    {
        pieceList.add(new Knight(this, 1, 0, false));
        pieceList.add(new Knight(this, 6, 0, false));
        pieceList.add(new Knight(this, 1, 7, true));
        pieceList.add(new Knight(this, 6, 7, true));
        pieceList.add(new Pawn(this, 0, 6, true));
        pieceList.add(new Pawn(this, 1, 6, true));
        pieceList.add(new Pawn(this, 2, 6, true));
        pieceList.add(new Pawn(this, 3, 6, true));
        pieceList.add(new Pawn(this, 4, 6, true));
        pieceList.add(new Pawn(this, 5, 6, true));
        pieceList.add(new Pawn(this, 6, 6, true));
        pieceList.add(new Pawn(this, 7, 6, true));
        pieceList.add(new Pawn(this, 0, 1, false));
        pieceList.add(new Pawn(this, 1, 1, false));
        pieceList.add(new Pawn(this, 2, 1, false));
        pieceList.add(new Pawn(this, 3, 1, false));
        pieceList.add(new Pawn(this, 4, 1, false));
        pieceList.add(new Pawn(this, 5, 1, false));
        pieceList.add(new Pawn(this, 6, 1, false));
        pieceList.add(new Pawn(this, 7, 1, false));
        pieceList.add(new Rook(this, 0, 0, false));
        pieceList.add(new Rook(this, 7, 0, false));
        pieceList.add(new Rook(this, 0, 7, true));
        pieceList.add(new Rook(this, 7, 7, true));
        pieceList.add(new Bishop(this, 2, 0, false));
        pieceList.add(new Bishop(this, 5, 0, false));
        pieceList.add(new Bishop(this, 2, 7, true));
        pieceList.add(new Bishop(this, 5, 7, true));
        pieceList.add(new Queen(this, 3, 0, false));
        pieceList.add(new Queen(this, 3, 7, true));
        pieceList.add(new King(this, 4, 7, true));
        pieceList.add(new King(this, 4, 0, false));
    }

    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;

        for (int r = 0; r < row; r++)
            for (int c = 0; c < col; c++)
            {
                g2.setColor((c+r) % 2 == 0 ? new Color(255, 255, 255) : new Color(122, 173, 107) );
                g2.fillRect(r* ts, c*ts, ts, ts);
            }
        
        for (Piece piece : pieceList)
        {
            piece.paint(g2);
        }
    }

    public Piece getPiece(int col, int row)
    {
        for (Piece piece : pieceList)
        {
            if (piece.col == col && piece.row == row)
                return piece;
        }
        return null;
    }

    public void makeMove(Move move)
    {
       if(validMove(move)){
       if((move.piece.col != move.newCol || move.piece.row != move.newRow)){
       move.piece.col = move.newCol;
       move.piece.row = move.newRow;
       
       move.piece.xpos = move.newCol * ts;
       move.piece.ypos = move.newRow * ts;
        
        capture(move);
        whiteTurn = whiteTurn ? false : true;

       }}
       else{
       move.piece.xpos = move.piece.col * ts;
       move.piece.ypos = move.piece.row * ts;
       }
       
    }

    public boolean validMove(Move move){
        int toCol = move.newCol, toRow = move.newRow, fromCol = move.piece.col, fromRow = move.piece.row;
        Piece piece = getPiece(fromCol, fromRow);
        Piece toPiece = getPiece(toCol, toRow);

        if(piece.isWhite() != whiteTurn){
            return false;
        }

        if(piece != null && toPiece != null){
            if(piece.isWhite() == toPiece.isWhite()){
                return false;
            }
        }

        if(piece instanceof Pawn ){
            if(piece.isWhite()){
                if(!piece.readMadeMove()){
                    if((fromRow - 1 == toRow) && (fromCol + 1 == toCol || fromCol - 1 == toCol) && (toPiece != null)){
                        piece.switchMadeMove();
                        return true;
                    }
                    else if((fromRow - toRow < 3) && (fromRow - toRow > -1) && (toCol == fromCol) && (toPiece == null)){
                        if(getPiece(fromCol, fromRow - 1) != null){
                            return false;
                        }
                        piece.switchMadeMove();
                        return true;
                    }
                }
                else{
                    if((fromRow - 1 == toRow) && (fromCol + 1 == toCol || fromCol - 1 == toCol) && (toPiece != null)){
                        return true;
                    }
                    else if((fromRow - toRow == 1) && (toCol == fromCol) && (toPiece == null)){
                        return true;
                    }
                }
            }
            else{
                if(!piece.readMadeMove()){
                    if((fromRow + 1 == toRow) && (fromCol + 1 == toCol || fromCol - 1 == toCol) && (toPiece != null)){
                        return true;
                    }
                    else if((fromRow - toRow > -3) && (fromRow - toRow < 1) && (toCol == fromCol) && (toPiece == null)){
                        piece.switchMadeMove();
                        return true;
                    }
                }
                else{
                    if((fromRow + 1 == toRow) && (fromCol + 1 == toCol || fromCol - 1 == toCol) && (toPiece != null)){
                        return true;
                    }
                    else if((fromRow - toRow > -2) && (fromRow - toRow < 1) && (toCol == fromCol) && (toPiece == null)){
                        return true;
                    }
            }
        }
            return false;
        }

        else if(piece instanceof Rook){
            boolean openPath = true;
            if(toRow == fromRow && fromCol < toCol){
                for(int i = fromCol + 1; i < toCol; i++){
                    if(getPiece(i, fromRow) != null){
                        openPath = false;
                    }
                }
            }
            if(toRow == fromRow && fromCol > toCol){
                for(int i = fromCol - 1; i > toCol; i--){
                    if(getPiece(i, fromRow) != null){
                        openPath = false;
                    }
                }
            }
            if(toRow < fromRow && fromCol == toCol){
                for(int i = fromRow - 1; i > toRow; i--){
                    if(getPiece(fromCol, i) != null){
                        openPath = false;
                    }
                }
            }
            if(toRow > fromRow && fromCol == toCol){
                for(int i = fromRow + 1; i < toRow; i++){
                    if(getPiece(fromCol, i) != null){
                        openPath = false;
                    }
                }
            }
            if((((fromRow == toRow) && (fromCol != toCol)) || ((fromRow != toRow) && (fromCol == toCol)))  && (openPath)){
                return true;
            }
            return false;
        }

        else if(piece instanceof Knight){
            int[] rowOffsets = {-2, -1, 1, 2, 2, 1, -1, -2};
            int[] colOffsets = {1, 2, 2, 1, -1, -2, -2, -1};

            for(int i = 0; i < 8; i++){
                int newRow = fromRow + rowOffsets[i];
                int newCol = fromCol + colOffsets[i];
    
                if(newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8){
                    if(newRow == toRow && newCol == toCol){
                        return true;
                    }
                }
            }
            return false;
        }

        else if(piece instanceof Bishop){
            // checks for diagonal moves, up and to the right
            for (int i = 1; fromRow - i > -1 && fromCol + i < 8; i++) {
                if(getPiece(fromCol+i, fromRow-i) != null && 
                fromRow - i == toRow && fromCol + i == toCol) {
                    return true;
                }
                else if(getPiece(fromCol+i, fromRow-i) != null && 
                fromRow - i > toRow && fromCol + i < toCol) {
                    return false;
                }
                else if(fromRow - i == toRow && fromCol + i == toCol) {
                    return true;
                }
              }
            // checks for diagonal moves, up and to the left
            for (int i = 1; fromRow - i > -1 && fromCol - i > -1; i++) {
                if(getPiece(fromCol-i, fromRow-i) != null && 
                fromRow - i == toRow && fromCol - i == toCol) {
                    return true;
                }
                else if(getPiece(fromCol-i, fromRow-i) != null && 
                fromRow - i > toRow && fromCol - i > toCol) {
                    return false;
                }
                else if(fromRow - i == toRow && fromCol - i == toCol) {
                    return true;
                }
            }
            // checks for diagonal moves, down and to the right
            for (int i = 1; fromRow + i < 8 && fromCol + i < 8; i++) {
                if(getPiece(fromCol+i, fromRow+i) != null && 
                fromRow + i == toRow && fromCol + i == toCol) {
                    return true;
                }
                else if(getPiece(fromCol+i, fromRow+i) != null && 
                fromRow + i < toRow && fromCol + i < toCol) {
                    return false;
                }
                else if(fromRow + i == toRow && fromCol + i == toCol) {
                    return true;
                }
            }
            // checks for diagonal moves, down and to the left
            for (int i = 1; fromRow + i < 8 && fromCol - i < 8; i++) {
                if(getPiece(fromCol-i, fromRow+i) != null && 
                fromRow + i == toRow && fromCol - i == toCol) {
                    return true;
                }
                else if(getPiece(fromCol-i, fromRow+i) != null && 
                fromRow + i < toRow && fromCol - i > toCol) {
                    return false;
                }
                else if(fromRow + i == toRow && fromCol - i == toCol) {
                    return true;
                }
            }
              return false;
        }

        else if (piece instanceof Queen) {
            if((fromRow != toRow && fromCol == toCol) || 
            (fromRow == toRow && fromCol != toCol)){

            // checks for Upward Moves
            for (int i = 1; fromRow - i > -1; i++) {
                if(getPiece(fromCol, fromRow-i) != null && 
                fromRow - i == toRow) {
                    return true;
                }
                else if(getPiece(fromCol, fromRow-i) != null && 
                fromRow - i > toRow) {
                    return false;
                }
                else if(fromRow - i == toRow) {
                    return true;
                }
            }
            // checks for Downward Moves
            for (int i = 1; fromRow + i < 8; i++) {
                if(getPiece(fromCol, fromRow+i) != null && 
                fromRow + i == toRow) {
                    return true;
                }
                else if(getPiece(fromCol, fromRow+i) != null && 
                fromRow + i < toRow) {
                    return false;
                }
                else if(fromRow + i == toRow) {
                    return true;
                }
            }
            // checks for Rightward Moves
            for (int i = 1; fromCol + i < 8; i++) {
                if(getPiece(fromCol+i, fromRow) != null && 
                fromCol + i == toCol) {
                    return true;
                }
                else if(getPiece(fromCol+i, fromRow) != null && 
                fromCol + i < toCol) {
                    return false;
                }
                else if(fromCol + i == toCol) {
                    return true;
                }
            }
            // checks for Leftward Moves
            for (int i = 1; fromCol - i > -1; i++) {
                if(getPiece(fromCol-i, fromRow) != null && 
                fromCol - i == toCol) {
                    return true;
                }
                else if(getPiece(fromCol-i, fromRow) != null && 
                fromCol - i > toCol) {
                    return false;
                }
                else if(fromCol - i == toCol) {
                    return true;
                }
            }
            }
            else{
            // checks for diagonal moves, up and to the right
            for (int i = 1; fromRow - i > -1 && fromCol + i < 8; i++) {
                if(getPiece(fromCol+i, fromRow-i) != null && 
                fromRow - i == toRow && fromCol + i == toCol) {
                    return true;
                }
                else if(getPiece(fromCol+i, fromRow-i) != null && 
                fromRow - i > toRow && fromCol + i < toCol) {
                    return false;
                }
                else if(fromRow - i == toRow && fromCol + i == toCol) {
                    return true;
                }
            }
            // checks for diagonal moves, up and to the left
            for (int i = 1; fromRow - i > -1 && fromCol - i > -1; i++) {
                if(getPiece(fromCol-i, fromRow-i) != null && 
                fromRow - i == toRow && fromCol - i == toCol) {
                    return true;
                }
                else if(getPiece(fromCol-i, fromRow-i) != null && 
                fromRow - i > toRow && fromCol - i > toCol) {
                    return false;
                }
                else if(fromRow - i == toRow && fromCol - i == toCol) {
                    return true;
                }
            }
            // checks for diagonal moves, down and to the right
            for (int i = 1; fromRow + i < 8 && fromCol + i < 8; i++) {
                if(getPiece(fromCol+i, fromRow+i) != null && 
                fromRow + i == toRow && fromCol + i == toCol) {
                    return true;
                }
                else if(getPiece(fromCol+i, fromRow+i) != null && 
                fromRow + i < toRow && fromCol + i < toCol) {
                    return false;
                }
                else if(fromRow + i == toRow && fromCol + i == toCol) {
                    return true;
                }
            }
            // checks for diagonal moves, down and to the left
            for (int i = 1; fromRow + i < 8 && fromCol - i < 8; i++) {
                if(getPiece(fromCol-i, fromRow+i) != null && 
                fromRow + i == toRow && fromCol - i == toCol) {
                    return true;
                }
                else if(getPiece(fromCol-i, fromRow+i) != null && 
                fromRow + i < toRow && fromCol - i > toCol) {
                    return false;
                }
                else if(fromRow + i == toRow && fromCol - i == toCol) {
                    return true;
                }
            }
            }   
            return false;
        }

        else if (piece instanceof King) {
            // Checks for the possible moves
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                
                    if (i == 0 && j == 0) {
                        continue;
                    }
        
                    int newRow = fromRow + i;
                    int newCol = fromCol + j;
                
                    if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                        if(newRow == toRow && newCol == toCol){
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        return true;
    }

    public void capture(Move move){
        if(move.piece instanceof Pawn && move.newRow == 0 || move.piece 
        instanceof Pawn && move.newRow == 7){
            pieceList.remove(move.Capture);
            pieceList.remove(move.piece);
            pieceList.add(new Queen(this, move.newCol, move.newRow, (move.piece.isWhite() ? true : false)));
        } else{
            pieceList.remove(move.Capture);
        }
        if(move.Capture instanceof King){
            System.out.println();
            if(move.piece.isWhite()){
                System.out.println("White team wins!\n");
            }
            else{
                System.out.println("Black team wins!\n");
            }
            System.exit(0);
        }
    }

}

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
        pieceList.add(new Knight(this, 1, 0, PieceColor.BLACK));
        pieceList.add(new Knight(this, 6, 0, PieceColor.BLACK));
        pieceList.add(new Knight(this, 1, 7, PieceColor.WHITE));
        pieceList.add(new Knight(this, 6, 7, PieceColor.WHITE));
        pieceList.add(new Pawn(this, 0, 6, PieceColor.WHITE));
        pieceList.add(new Pawn(this, 1, 6, PieceColor.WHITE));
        pieceList.add(new Pawn(this, 2, 6, PieceColor.WHITE));
        pieceList.add(new Pawn(this, 3, 6, PieceColor.WHITE));
        pieceList.add(new Pawn(this, 4, 6, PieceColor.WHITE));
        pieceList.add(new Pawn(this, 5, 6, PieceColor.WHITE));
        pieceList.add(new Pawn(this, 6, 6, PieceColor.WHITE));
        pieceList.add(new Pawn(this, 7, 6, PieceColor.WHITE));
        pieceList.add(new Pawn(this, 0, 1, PieceColor.BLACK));
        pieceList.add(new Pawn(this, 1, 1, PieceColor.BLACK));
        pieceList.add(new Pawn(this, 2, 1, PieceColor.BLACK));
        pieceList.add(new Pawn(this, 3, 1, PieceColor.BLACK));
        pieceList.add(new Pawn(this, 4, 1, PieceColor.BLACK));
        pieceList.add(new Pawn(this, 5, 1, PieceColor.BLACK));
        pieceList.add(new Pawn(this, 6, 1, PieceColor.BLACK));
        pieceList.add(new Pawn(this, 7, 1, PieceColor.BLACK));
        pieceList.add(new Rook(this, 0, 0, PieceColor.BLACK));
        pieceList.add(new Rook(this, 7, 0, PieceColor.BLACK));
        pieceList.add(new Rook(this, 0, 7, PieceColor.WHITE));
        pieceList.add(new Rook(this, 7, 7, PieceColor.WHITE));
        pieceList.add(new Bishop(this, 2, 0, PieceColor.BLACK));
        pieceList.add(new Bishop(this, 5, 0, PieceColor.BLACK));
        pieceList.add(new Bishop(this, 2, 7, PieceColor.WHITE));
        pieceList.add(new Bishop(this, 5, 7, PieceColor.WHITE));
        pieceList.add(new Queen(this, 3, 0, PieceColor.BLACK));
        pieceList.add(new Queen(this, 3, 7, PieceColor.WHITE));
        pieceList.add(new King(this, 4, 7, PieceColor.WHITE));
        pieceList.add(new King(this, 4, 0, PieceColor.BLACK));
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

        if((piece.getColor() == PieceColor.WHITE) && (!whiteTurn)){
            return false;
        }

        if((piece.getColor() == PieceColor.BLACK) && (whiteTurn)){
            return false;
        }

        if((toPiece != null) && (piece.getColor() == toPiece.getColor())){
            return false;
        }

        return piece.isValidMove(toCol, toRow, this);
    }

    public void capture(Move move){
        if(move.piece instanceof Pawn && move.newRow == 0 || move.piece 
        instanceof Pawn && move.newRow == 7){
            pieceList.remove(move.Capture);
            pieceList.remove(move.piece);
            pieceList.add(new Queen(this, move.newCol, move.newRow, (move.piece.getColor())));
        } else{
            pieceList.remove(move.Capture);
        }
        if(move.Capture instanceof King){
            System.out.println();
            if(move.piece.getColor() == PieceColor.WHITE){
                System.out.println("White team wins!\n");
            }
            else{
                System.out.println("Black team wins!\n");
            }
            System.exit(0);
        }
    }

}

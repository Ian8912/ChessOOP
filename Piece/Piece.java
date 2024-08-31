package Piece;
import Game.Board;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;



public class Piece {

    public int col, row;
    public int xpos, ypos;

    public boolean isWhite;
    public String name;
    
    private boolean madeMove = false;

    public void switchMadeMove(){
        madeMove = true;
    }
    
    public boolean readMadeMove(){
        return madeMove;
    }

    Image sprite;
    Board board;

    BufferedImage sT;
    {
        try {
            sT = ImageIO.read(ClassLoader.getSystemResourceAsStream("Pieces.png"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public int sTScale = sT.getWidth()/6;

    public Piece(Board board)
    {
        this.board = board;
    }    

    public void paint(Graphics2D g2)
    {
        g2.drawImage(sprite, xpos, ypos, null);
    }

    public boolean isWhite(){
        return isWhite;
    }

}
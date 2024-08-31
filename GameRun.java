import Game.Board;
import java.awt.*;
import javax.swing.*;
public class GameRun {
    
    public static void main(String[] args){
        JFrame frame = new JFrame("Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        frame.setSize(new Dimension(696,719));
        frame.setLocationRelativeTo(null);
        
        Board board = new Board();
        board.addPieces();
        frame.add(board);
        frame.setVisible(true);
    }
}

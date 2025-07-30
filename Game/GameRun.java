package Game;
import java.awt.*;
import javax.swing.*;

/**
 * Entry point for launching the Chess game application.
 * 
 * <p>This class creates the main application window using Swing,
 * initializes the chess board, and displays the GUI.</p>
 */
public class GameRun {
    
    /**
     * Main method to launch the chess game.
     * 
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args){
        System.out.println("GameRun launched");

        JFrame frame = new JFrame("Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        frame.setSize(new Dimension(696, 719));
        frame.setLocationRelativeTo(null);
        
        Board board = new Board();
        board.addPieces();
        frame.add(board);
        frame.setVisible(true);
    }
}

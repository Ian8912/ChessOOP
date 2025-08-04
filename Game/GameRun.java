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
     * @see Board
     * @see Board#addPieces() 
     */
    public static void main(String[] args){
        System.out.println("GameRun launched");

        JFrame frame = new JFrame("Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        
        Board board = new Board();
        board.addPieces();
        frame.add(board);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

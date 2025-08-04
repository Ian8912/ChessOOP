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
        frame.setLayout(new BorderLayout());
        
        Board board = new Board();
        board.addPieces();

        JTextArea infoArea = new JTextArea(5, 23);
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        infoArea.setText(" Welcome to Java Chess!\n White's turn");
        JScrollPane scrollPane = new JScrollPane(infoArea);

        frame.add(board, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.EAST);

        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

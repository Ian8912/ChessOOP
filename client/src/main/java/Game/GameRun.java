package Game;
import java.awt.*;
import javax.swing.*;
import Piece.PieceColor;

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

        // Mode selection
        String[] modes = { "Player vs Player", "Player vs Computer (you play White)", "Player vs Computer (you play Black)" };
        int mode = JOptionPane.showOptionDialog(null, "Select game mode:", "Game Mode",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, modes, modes[0]);
        if (mode == JOptionPane.CLOSED_OPTION) mode = 0;

        boolean vsComputer = (mode == 1 || mode == 2);
        boolean humanIsWhite = (mode != 2); // mode 2 = human plays Black

        String whiteName;
        String blackName;

        if (vsComputer) {
            String humanName = JOptionPane.showInputDialog(null, "Enter your name:", "Player Setup", JOptionPane.PLAIN_MESSAGE);
            if (humanName == null || humanName.isBlank()) humanName = "Player";
            whiteName = humanIsWhite ? humanName : "Computer";
            blackName = humanIsWhite ? "Computer" : humanName;
        } else {
            whiteName = JOptionPane.showInputDialog(null, "Enter White player's name:", "Player Setup", JOptionPane.PLAIN_MESSAGE);
            if (whiteName == null || whiteName.isBlank()) whiteName = "White";
            blackName = JOptionPane.showInputDialog(null, "Enter Black player's name:", "Player Setup", JOptionPane.PLAIN_MESSAGE);
            if (blackName == null || blackName.isBlank()) blackName = "Black";
        }

        JFrame frame = new JFrame("Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        Board board = new Board();
        board.addPieces();
        board.setPlayerNames(whiteName, blackName);

        if (vsComputer) {
            PieceColor computerColor = humanIsWhite ? PieceColor.BLACK : PieceColor.WHITE;
            board.setComputerPlayer(new ComputerPlayer(computerColor));
        }

        JTextArea infoArea = new JTextArea(5, 23);
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Monospaced", Font.BOLD, 14));
        infoArea.setText(" Welcome to Java Chess\n\n It is White's turn");
        JScrollPane scrollPane = new JScrollPane(infoArea);

        board.setInfoArea(infoArea);

        frame.add(board, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.EAST);

        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // If computer plays White it moves first
        if (vsComputer && !humanIsWhite) {
            SwingUtilities.invokeLater(() -> {
                Move firstMove = board.getComputerPlayer().getBestMove(board);
                if (firstMove != null) {
                    board.makeMove(firstMove);
                    board.repaint();
                }
            });
        }
    }

}

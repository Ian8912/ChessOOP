package Game;
import Piece.Piece;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Handles player mouse input, including all mouse event actions
 * such as pressing, releasing, and dragging pieces on the chess board.
 *
 * <p>This class allows players to interact with the chess board by selecting
 * (pressing), moving (dragging), and placing (releasing) pieces. It updates
 * the board state based on valid moves or resets the piece if the move is invalid.</p>
 *
 * @see Board
 */
public class Input extends MouseAdapter {

    /** The board to be updated based on player interaction.*/
    Board board;

    /**
     * Constructs the input handler for mouse events and assigns the target board.
     *
     * <p>Sets the {@link Board} that this input handler will interact with.</p>
     * @param board the chess board this input will handle
     */
    public Input(Board board){

        this.board = board;
    }

    /**
     * Handles mouse press events from the player.
     *
     * <p>Determines the column and row based on the {@link MouseEvent}
     * coordinates, then attempts to retrieve a piece from the corresponding
     * location on the {@link Board}. If a non-null piece exists at that square,
     * it is selected as the current piece.</p>
     *
     * @param e the mouse event triggered by the user click action
     */
    @Override
    public void mousePressed(MouseEvent e){

        int col = e.getX() / board.ts;
        int row = e.getY() / board.ts;

        Piece pXY = board.getPiece(col, row);

        if (pXY != null){
            board.selPiece = pXY;
        }
    }

    /** Handles mouse release events from the player.
     *
     *  <p>Determines the column and row based on the {@link MouseEvent}
     *  coordinates. If {@link Board#selPiece} is non-null, the method constructs
     *  * a {@link Move} and checks whether it is valid. If the move is valid,
     *  * the board is updated accordingly. Otherwise, the selected piece
     *  * returns to its original position.</p>
     *
     * @param e the mouse event triggered by the user release action
     */
    @Override
    public void mouseReleased(MouseEvent e){

        int col = e.getX() / board.ts;
        int row = e.getY() / board.ts;

        if (board.selPiece != null){
            Move m = new Move(board, board.selPiece, col, row);
                if (board.isValid()){
                    board.makeMove(m);
                }
                else{
                board.selPiece.xpos = board.selPiece.col * board.ts;
                board.selPiece.ypos = board.selPiece.row * board.ts;
                }
                board.repaint();
        }
        board.selPiece = null;
    }

    /** Handles mouse drag events from the player.
     *
     * <p>If {@link Board#selPiece} is non-null, updates its screen position
     * based on the current {@link MouseEvent} coordinates to allow the player
     * to visually drag the piece. Triggers a {@code repaint()} of the board to reflect
     * the piece's new position.</p>
     *
     * @param e the mouse event triggered by the user drag action
     */
    @Override
    public void mouseDragged(MouseEvent e){

        if (board.selPiece != null){
            board.selPiece.xpos = e.getX() - board.ts/2;
            board.selPiece.ypos = e.getY() - board.ts/2;

            board.repaint();
        }
    }

}
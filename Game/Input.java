package Game;
import Piece.Piece;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Input extends MouseAdapter{

    Board board;

    public Input(Board board)
    {
        this.board = board;
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        int col = e.getX() / board.ts;
        int row = e.getY() / board.ts;

        Piece pXY = board.getPiece(col, row);

        if (pXY != null)
        {
            board.selPiece = pXY;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        int col = e.getX() / board.ts;
        int row = e.getY() / board.ts;

        if (board.selPiece != null)
        {
            Move m = new Move(board, board.selPiece, col, row);
                if (board.isValid())
                {
                    board.makeMove(m);
                }
                else
                {
                board.selPiece.xpos = board.selPiece.col * board.ts;
                board.selPiece.ypos = board.selPiece.row * board.ts;
                }
                board.repaint();
        }
        board.selPiece = null;
        
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {

        if (board.selPiece != null)
        {
            board.selPiece.xpos = e.getX() - board.ts/2;
            board.selPiece.ypos = e.getY() - board.ts/2;

            board.repaint();
        }
    }



    
}

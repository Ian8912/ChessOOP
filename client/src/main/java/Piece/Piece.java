package Piece;
import Game.Board;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.InputStream;
import java.io.FileNotFoundException;

/**
 * Represents an abstract chess piece, providing shared functionality for all specific piece types.
 *
 * <p>This class defines common fields and methods used by all chess pieces, such as
 * position, sprite rendering, movement status, and access to the associated {@link Board}.
 * Subclasses are expected to implement their own movement validation logic via
 * {@link Piece#isValidMove(int, int, Board)}.</p>
 *
 * @see Board
 * @see PieceColor
 */
public abstract class Piece {

    /** The column and row position of the piece on the board. */
    public int col, row;

    /** The on-screen pixel coordinates used for rendering. */
    public int xpos, ypos;

    /** The color of the piece (white or black). */
    protected PieceColor color;

    /** The name of the piece (e.g., "Bishop", "Knight"). */
    public String name;

    /** Indicates whether the piece has moved at least once. */
    private boolean madeMove = false;

    /** The image sprite used to render this piece. */
    Image sprite;

    /** Reference to the board this piece belongs to. */
    Board board;

    /** The full sprite sheet image containing all chess piece graphics. */
    BufferedImage sT;

    /** The single piece graphic within the sprite sheet. */
    public int sTScale;

    /** Initializes the sprite sheet when the piece is constructed. */
    {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("Pieces.png")) {
            if (is == null) {
                throw new FileNotFoundException("Pieces.png not found on classpath!");
            }
            sT = ImageIO.read(is);
            /** The width and height of a single piece graphic within the sprite sheet. */
            sTScale = sT.getWidth() / 6;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructs a chess piece and associates it with the specified board.
     *
     * @param board the chess board this piece belongs to
     */
    public Piece(Board board){

        this.board = board;
    }

    /**
     * Draws the piece sprite on the screen at its current pixel position.
     *
     * @param g2 the graphics context used to render the piece
     */
    public void paint(Graphics2D g2){

        g2.drawImage(sprite, xpos, ypos, null);
    }

    /**
     * Returns the color of the piece (white or black).
     *
     * @return the color of this piece
     */
    public PieceColor getColor(){

        return color;
    }

    /**
     * Sets the madeMove flag to true, indicating the piece has moved at least once.
     */
    public void switchMadeMove(){

        madeMove = true;
    }

    /**
     * Returns whether the piece has previously moved.
     *
     * @return {@code true} if the piece has moved; {@code false} otherwise
     */
    public boolean readMadeMove(){

        return madeMove;
    }

    /**
     * Determines whether a move to the specified destination is valid for this piece.
     *
     * <p>This method must be implemented by subclasses such as {@code Bishop}, {@code Rook}, etc.,
     * and defines the {@link Piece} subclass movement logic.</p>
     *
     * @param toCol the destination column
     * @param toRow the destination row
     * @param board the board on which the move is evaluated
     * @return {@code true} if the move is valid; {@code false} otherwise
     */
    public abstract boolean isValidMove(int toCol, int toRow, Board board);

}
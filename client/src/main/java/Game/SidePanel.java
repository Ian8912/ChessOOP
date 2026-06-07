package Game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Styled information panel shown beside the board: game status, a live material
 * score with an advantage bar, captured pieces, and each side's last move grade.
 *
 * <p>The {@link Board} pushes a fresh snapshot through {@link #update} on every
 * turn transition. All values are supplied by the board so this class stays a
 * pure view with no game logic.</p>
 *
 * @see Board#updateInfo(String)
 */
public class SidePanel extends JPanel {

    private static final Color BG       = new Color(0x2B2B2B);
    private static final Color FG       = new Color(0xECECEC);
    private static final Color MUTED    = new Color(0x9AA0A6);
    private static final Color WHITE_BAR = new Color(0xEEEED2);
    private static final Color BLACK_BAR = new Color(0x4B4B4B);
    private static final Color ACCENT   = new Color(0x82C77F);

    private final JLabel statusLabel   = new JLabel();
    private final JLabel whiteLabel    = new JLabel();
    private final JLabel whiteCaptured = new JLabel();
    private final JLabel blackLabel    = new JLabel();
    private final JLabel blackCaptured = new JLabel();
    private final AdvantageBar bar     = new AdvantageBar();

    /** Builds the panel layout and styling. */
    public SidePanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        setPreferredSize(new Dimension(280, 0));

        Font titleFont   = new Font("SansSerif", Font.BOLD, 16);
        Font nameFont    = new Font("SansSerif", Font.BOLD, 15);
        Font glyphFont   = new Font("SansSerif", Font.PLAIN, 22);

        style(statusLabel, FG, titleFont);
        style(whiteLabel, FG, nameFont);
        style(blackLabel, FG, nameFont);
        style(whiteCaptured, MUTED, glyphFont);
        style(blackCaptured, MUTED, glyphFont);

        add(statusLabel);
        add(Box.createVerticalStrut(18));
        add(whiteLabel);
        add(whiteCaptured);
        add(Box.createVerticalStrut(10));
        add(bar);
        add(Box.createVerticalStrut(10));
        add(blackLabel);
        add(blackCaptured);
        add(Box.createVerticalGlue());
    }

    /** Applies a consistent foreground/font and left alignment to a label. */
    private void style(JLabel label, Color fg, Font font) {
        label.setForeground(fg);
        label.setFont(font);
        label.setAlignmentX(LEFT_ALIGNMENT);
    }

    /**
     * Refreshes the panel with the current game state.
     *
     * @param status         the status line(s) (may contain {@code \n})
     * @param whiteName      white player's display name
     * @param blackName      black player's display name
     * @param whiteScore     white's material in pawn units
     * @param blackScore     black's material in pawn units
     * @param whiteGrade     white's last move grade (may be empty)
     * @param blackGrade     black's last move grade (may be empty)
     * @param whiteCapturedGlyphs glyphs for pieces white has captured
     * @param blackCapturedGlyphs glyphs for pieces black has captured
     * @param lead           formatted material-lead indicator, e.g. {@code "+3.0"} or {@code "even"}
     */
    public void update(String status, String whiteName, String blackName,
                       double whiteScore, double blackScore,
                       String whiteGrade, String blackGrade,
                       String whiteCapturedGlyphs, String blackCapturedGlyphs,
                       String lead) {
        statusLabel.setText("<html>" + status.trim().replace("\n", "<br>") + "</html>");
        whiteLabel.setText(row(whiteName, whiteScore, whiteGrade));
        blackLabel.setText(row(blackName, blackScore, blackGrade));
        whiteCaptured.setText(whiteCapturedGlyphs.isEmpty() ? " " : whiteCapturedGlyphs);
        blackCaptured.setText(blackCapturedGlyphs.isEmpty() ? " " : blackCapturedGlyphs);
        bar.setScores(whiteScore, blackScore, lead);
        revalidate();
        repaint();
    }

    /** Formats a player's name + score, with the move grade on a muted second line. */
    private String row(String name, double score, String grade) {
        String gradeLine = grade.isEmpty() ? ""
            : "<br><span style='color:#9aa0a6;font-size:11px'>" + grade + "</span>";
        return String.format(
            "<html><b>%s</b>&nbsp;&nbsp;<span style='color:#82c77f'>%.1f</span>%s</html>",
            name, score, gradeLine);
    }

    /**
     * A horizontal bar whose split reflects the material ratio between the two
     * sides, with the lead amount drawn in the centre.
     */
    private static final class AdvantageBar extends JComponent {
        private double white = 1, black = 1;
        private String lead = "";

        AdvantageBar() {
            setAlignmentX(LEFT_ALIGNMENT);
            Dimension d = new Dimension(240, 22);
            setPreferredSize(d);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        }

        void setScores(double white, double black, String lead) {
            this.white = white;
            this.black = black;
            this.lead = lead;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth();
            int h = getHeight();
            double total = white + black;
            int split = (total <= 0) ? w / 2 : (int) Math.round(w * (white / total));

            int arc = h;
            g2.setColor(BLACK_BAR);
            g2.fillRoundRect(0, 0, w, h, arc, arc);
            g2.setColor(WHITE_BAR);
            g2.fillRoundRect(0, 0, split, h, arc, arc);
            // square off the right edge of the white portion so it meets the black cleanly
            if (split > arc) g2.fillRect(split - arc, 0, arc, h);

            g2.setColor(ACCENT);
            g2.fillRect(split - 1, 0, 2, h);

            if (!lead.isEmpty()) {
                g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                int tw = g2.getFontMetrics().stringWidth(lead);
                // draw on whichever half has room; default to centre
                int x = Math.max(4, Math.min(w - tw - 4, (w - tw) / 2));
                g2.setColor(new Color(0x202020));
                g2.drawString(lead, x, h - 6);
            }
            g2.dispose();
        }
    }
}

package game;

import javax.swing.*;
import java.awt.*;

public class ActionPanel extends JPanel {

    private Game game;
    private JLabel gridReference;

    public ActionPanel(Game game) {
        this.game = game;
        setPreferredSize(new Dimension(600, 1000));
        setBackground(Color.YELLOW);
        gridReference = new JLabel(new ImageIcon("gui/resized_board.jpg"));
        gridReference.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(gridReference);
    }
}

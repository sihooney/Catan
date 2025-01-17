package game;

import javax.swing.*;
import java.awt.*;

public class Catan {

    private final JFrame windowFrame;
    private JPanel menuPanel;
    private JPanel gamePanel;
    private ActionPanel actionPanel;
    private BoardPanel boardPanel;
    private InfoPanel infoPanel;

    private Game game;

    public Catan() {
        windowFrame = new JFrame("Settlers of Catan");
        windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowFrame.setPreferredSize(new Dimension(1800, 1000));;
        menuSetup();
        windowFrame.setResizable(false);
        windowFrame.pack();
        windowFrame.setVisible(true); // Display frame
    }

    private void menuSetup() {
        menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(1800, 1000));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Settlers of Catan");
        title.setFont(new Font(null, Font.PLAIN, 150));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton threePlayer = new JButton("3 Players");
        threePlayer.setFont(new Font(null, Font.PLAIN, 80));
        threePlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
        threePlayer.addActionListener(e -> gameSetup(3));
        JButton fourPlayer = new JButton("4 Players");
        fourPlayer.setFont(new Font(null, Font.PLAIN, 80));
        fourPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
        fourPlayer.addActionListener(e -> gameSetup(4));
        menuPanel.add(title);
        menuPanel.add(Box.createVerticalStrut(50));
        menuPanel.add(threePlayer);
        menuPanel.add(Box.createVerticalStrut(50));
        menuPanel.add(fourPlayer);
        windowFrame.add(menuPanel);
    }

    private void gameSetup(int numPlayers) {
        game = new Game(numPlayers);
        menuPanel.removeAll();
        windowFrame.remove(menuPanel);
        gamePanel = new JPanel();
        gamePanel.setPreferredSize(new Dimension(1800, 1000));
        gamePanel.setLayout(new BorderLayout());
        JPanel rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(1200, 1000));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.LIGHT_GRAY);
        actionPanel = new ActionPanel(game);
        boardPanel = new BoardPanel(game.board);
        infoPanel = new InfoPanel(game.players);
        rightPanel.add(boardPanel);
        rightPanel.add(infoPanel);
        gamePanel.add(actionPanel, BorderLayout.WEST);
        gamePanel.add(rightPanel, BorderLayout.EAST);
        windowFrame.add(gamePanel);
        windowFrame.revalidate();
        windowFrame.repaint();
    }

    public static void main(String[] args) {
        Catan catan = new Catan();
    }
}

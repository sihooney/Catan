package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static constants.Colors.NAMES;

public class Catan {

    private final JFrame windowFrame;
    private JPanel menuPanel;
    private JPanel gamePanel;
    private JPanel actionPanel;
    private BoardPanel boardPanel;
    private InfoPanel infoPanel;

    private Font actionFont;
    private JLabel gridReference;
    private JLabel playerLabel;
    private JButton rollDice;
    private boolean diceRolled;
    private JButton nextTurn;

    private Game game;

    public Catan() {
        windowFrame = new JFrame("Settlers of Catan");
        windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowFrame.setPreferredSize(new Dimension(1800, 1000));;
        menuSetup();
        windowFrame.setResizable(false);
        windowFrame.pack();
        windowFrame.setVisible(true);
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
        setupActionPanel();
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

    private void setupActionPanel() {
        actionPanel = new JPanel();
        diceRolled = false;
        actionPanel.setPreferredSize(new Dimension(600, 1000));
        actionPanel.setBackground(Color.YELLOW);
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        actionFont = new Font(null, Font.PLAIN, 20);
        gridReference = new JLabel(new ImageIcon("gui/resized_board.jpg"));
        gridReference.setAlignmentX(Component.CENTER_ALIGNMENT);
        actionPanel.add(gridReference);
        playerLabel = new JLabel("");
        playerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerLabel.setFont(actionFont);
        rollDice = new JButton("Roll Dice");
        rollDice.setAlignmentX(Component.CENTER_ALIGNMENT);
        rollDice.setFont(actionFont);
        rollDice.addActionListener(e -> diceRoll());
        nextTurn = new JButton("Next Turn");
        nextTurn.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextTurn.setFont(actionFont);
        nextTurn.addActionListener(e -> changePlayer());
        actionPanel.add(playerLabel);
        actionPanel.add(Box.createVerticalStrut(10));
        actionPanel.add(rollDice);
        actionPanel.add(Box.createVerticalStrut(10));
        actionPanel.add(nextTurn);
        actionPanel.add(Box.createVerticalStrut(10));
        displayName();
    }

    private void displayName() {
        playerLabel.setText("Player " + (game.curIndex + 1) + " " + NAMES[game.curIndex]);
    }

    private void diceRoll() {
        if (diceRolled) {
            JOptionPane.showMessageDialog(actionPanel, "You already rolled");
        } else {
            int roll = game.diceRoll();
            JOptionPane.showMessageDialog(actionPanel, "Dice Roll: " + roll);
            game.distributeResources(roll);
            infoPanel.update();
            diceRolled = true;
        }
    }

    private void changePlayer() {
        game.nextTurn();
        diceRolled = false;
        displayName();
    }

    public static void main(String[] args) {
        Catan catan = new Catan();
    }
}

package game;

import board.Building;

import javax.swing.*;
import java.awt.*;

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
    private JButton rollDiceButton;
    private boolean diceRolled;
    private JButton endTurnButton;
    private JButton settlementButton;
    private JButton cityButton;
    private JButton roadButton;
    private JButton buyDevCardButton;
    private JButton useDevCardButton;
    private JButton genericTradeButton;
    private JButton portTradeButton;
    private JButton playerTradeButton;

    private Game game;
    private int setupIdx;
    // TODO Initial Setup
    // 0 1 2 3 4 5 6 7 setupIdx
    // 0 1 2 3 3 2 1 0 playerIdx
    private final Integer[] rowChoices = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    private final Integer[] colChoices = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    public Catan() {
        windowFrame = new JFrame("Settlers of Catan");
        windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowFrame.setPreferredSize(new Dimension(1800, 1000));
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
        setupIdx = 0;
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
        rollDiceButton = new JButton("Roll Dice");
        rollDiceButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rollDiceButton.setFont(actionFont);
        rollDiceButton.addActionListener(e -> diceRoll());
        endTurnButton = new JButton("End Turn");
        endTurnButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        endTurnButton.setFont(actionFont);
        endTurnButton.addActionListener(e -> endTurn());
        settlementButton = new JButton("Build Settlement");
        settlementButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        settlementButton.setFont(actionFont);
        settlementButton.addActionListener(e -> placeSettlement());
        actionPanel.add(playerLabel);
        actionPanel.add(Box.createVerticalStrut(10));
        actionPanel.add(rollDiceButton);
        actionPanel.add(Box.createVerticalStrut(10));
        actionPanel.add(endTurnButton);
        actionPanel.add(Box.createVerticalStrut(10));
        actionPanel.add(settlementButton);
        actionPanel.add(Box.createVerticalStrut(10));
        displayName();
    }

    private void placeSettlement() {
        JComboBox<Integer> rowComboBox = new JComboBox<>(rowChoices);
        JComboBox<Integer> colComboBox = new JComboBox<>(colChoices);
        Object[] msg = {"Row Position:", rowComboBox, "Column position:", colComboBox};
        int res = JOptionPane.showConfirmDialog(actionPanel, msg, "Place Settlement",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            if (rowComboBox.getSelectedItem() != null && colComboBox.getSelectedItem() != null) {
                if (game.placeSettlement(new Building(game.curPlayer, (int) rowComboBox.getSelectedItem(),
                        (int) colComboBox.getSelectedItem(), Building.SETTLEMENT))) {
                    JOptionPane.showMessageDialog(actionPanel, "Building built");
                    boardPanel.repaint();
                    infoPanel.update();
                } else {
                    JOptionPane.showMessageDialog(actionPanel, "Unable to build");
                }
            }
        } else {
            JOptionPane.showMessageDialog(actionPanel, "Operation canceled.");
        }
    }

    private void placeRoad() {

    }

    private void initialSettlement() {

    }

    private void initialRoad() {

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

    private void endTurn() {
        int i = game.checkWin();
        if (i != -1) {
            JOptionPane.showMessageDialog(windowFrame, String.format("Player %d %s Wins", i + 1, NAMES[i]));
            gamePanel.removeAll();
            windowFrame.remove(gamePanel);
            windowFrame.revalidate();
            windowFrame.repaint();
        }
        game.nextTurn();
        diceRolled = false;
        displayName();
    }

    public static void main(String[] args) {
        Catan catan = new Catan();
    }
}

package game;

import board.*;
import board.Point;
import constants.Colors;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static constants.Colors.COLOR_TO_STRING;
import static constants.Colors.NAMES;

public class Catan {

    private final JFrame windowFrame;
    private JPanel menuPanel;
    private JPanel gamePanel;
    private JPanel actionPanel;
    private BoardPanel boardPanel;
    private InfoPanel infoPanel;

    private Font actionFont;
    private JLabel playerLabel;
    private JButton rollDiceButton;
    private boolean diceRolled;
    private JButton startGameButton;
    private JButton endTurnButton;
    private JButton roadButton;
    private JButton settlementButton;
    private JButton cityButton;

    private Game game;
    private int setupIdx;

    public Catan() {
        windowFrame = new JFrame("Settlers of Catan");
        windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowFrame.setPreferredSize(new Dimension(1800, 1000));
        windowFrame.setResizable(false);
    }

    public void run() {
        menuSetup();
        windowFrame.pack();
        windowFrame.setVisible(true);
    }

    private void menuSetup() {
        menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(1800, 1000));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Settlers of Catan");
        title.setFont(new Font(null, Font.PLAIN, 100));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton threePlayer = new JButton("3 Players");
        threePlayer.setFont(new Font(null, Font.PLAIN, 50));
        threePlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
        threePlayer.addActionListener(e -> gameSetup(3));
        JButton fourPlayer = new JButton("4 Players");
        fourPlayer.setFont(new Font(null, Font.PLAIN, 50));
        fourPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
        fourPlayer.addActionListener(e -> gameSetup(4));
        JLabel catanImg = new JLabel(new ImageIcon("gui/catan_logo.png"));
        catanImg.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(title);
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(threePlayer);
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(fourPlayer);
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(catanImg);
        menuPanel.revalidate();
        menuPanel.repaint();
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
        actionFont = new Font(null, Font.PLAIN, 18);
        JLabel gridReference = new JLabel(new ImageIcon("gui/resized_board.jpg"));
        gridReference.setAlignmentX(Component.CENTER_ALIGNMENT);
        actionPanel.add(gridReference);
        playerLabel = new JLabel("");
        playerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerLabel.setFont(actionFont);
        startGameButton = new JButton("Start Game");
        startGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startGameButton.setFont(actionFont);
        startGameButton.addActionListener(e -> initializeGame());
        actionPanel.add(Box.createVerticalStrut(10));
        actionPanel.add(startGameButton);
        displayName();
    }

    private void initializeGame() {
        while (setupIdx < game.N * 2) {
            if (setupIdx < game.N) {
                game.curIndex = setupIdx;
            } else {
                game.curIndex = 2 * game.N - setupIdx - 1;
            }
            game.setPlayer();
            if (placeSettlement(true)) {
                setupIdx++;
            }
        }
        game.distributeResources(-1);
        game.curIndex = 0;
        game.setPlayer();
        actionPanel.remove(startGameButton);
        rollDiceButton = new JButton("Roll Dice");
        rollDiceButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rollDiceButton.setFont(actionFont);
        rollDiceButton.addActionListener(e -> diceRoll());
        endTurnButton = new JButton("End Turn");
        endTurnButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        endTurnButton.setFont(actionFont);
        endTurnButton.addActionListener(e -> endTurn());
        roadButton = new JButton("Build Road");
        roadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        roadButton.setFont(actionFont);
        roadButton.addActionListener(e -> placeRoad());
        settlementButton = new JButton("Build Settlement");
        settlementButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        settlementButton.setFont(actionFont);
        settlementButton.addActionListener(e -> placeSettlement(false));
        cityButton = new JButton("Build City");
        cityButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cityButton.setFont(actionFont);
        cityButton.addActionListener(e -> placeCity());
        actionPanel.add(playerLabel);
        actionPanel.add(Box.createVerticalStrut(10));
        actionPanel.add(rollDiceButton);
        actionPanel.add(Box.createVerticalStrut(10));
        actionPanel.add(endTurnButton);
        actionPanel.add(Box.createVerticalStrut(10));
        actionPanel.add(roadButton);
        actionPanel.add(Box.createVerticalStrut(10));
        actionPanel.add(settlementButton);
        actionPanel.add(Box.createVerticalStrut(10));
        actionPanel.add(cityButton);
        actionPanel.add(Box.createVerticalStrut(10));
        actionPanel.revalidate();
        actionPanel.repaint();
    }

    private void displayName() {
        playerLabel.setText("Player " + (game.curIndex + 1) + " " + NAMES[game.curIndex]);
    }

    private void diceRoll() {
        if (diceRolled) {
            JOptionPane.showMessageDialog(actionPanel, "You already rolled");
        } else {
            int roll = game.rollDice();
            JOptionPane.showMessageDialog(actionPanel, "Dice Roll: " + roll);
            if (roll == 7) {
                game.halfResources();
                boolean moved;
                do {
                    moved = moveRobber();
                } while (!moved);
                boolean stolen;
                do {
                    stolen = stealResource();
                } while (!stolen);
            } else {
                game.distributeResources(roll);
            }
            updateGUI();
            diceRolled = true;
        }
    }

    private boolean moveRobber() {
        JComboBox<Integer> rowComboBox = new JComboBox<>(Board.ROW_INDEXES);
        JComboBox<Integer> colComboBox = new JComboBox<>(Board.COL_INDEXES);
        Object[] msg = {"Row position:", rowComboBox, "Column position:", colComboBox};
        int res = JOptionPane.showConfirmDialog(actionPanel, msg,
                String.format("Player %d move robber", game.curIndex + 1), JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            if (rowComboBox.getSelectedItem() != null && colComboBox.getSelectedItem() != null) {
                if (game.board.moveRobber(new Point((int) rowComboBox.getSelectedItem(),
                        (int) colComboBox.getSelectedItem()))) {
                    JOptionPane.showMessageDialog(actionPanel, "Robber moved");
                    updateGUI();
                    return true;
                } else {
                    JOptionPane.showMessageDialog(actionPanel, "Improper move");
                    return false;
                }
            }
        } else {
            JOptionPane.showMessageDialog(actionPanel, "Operation canceled.");
            return false;
        }
        return false;
    }

    private boolean stealResource() {
        ArrayList<Color> listChoices = robberTargets();
        if (listChoices.isEmpty()) {
            return true;
        }
        ButtonGroup group = new ButtonGroup();
        ArrayList<JRadioButton> radioButtons = new ArrayList<>();
        for (Color c : listChoices) {
            JRadioButton button = new JRadioButton(COLOR_TO_STRING.get(c));
            group.add(button);
            radioButtons.add(button);
        }
        Object[] msg = new Object[listChoices.size() + 1];
        msg[0] = "Select player to steal 1 random resource from:";
        for (int i = 1; i < msg.length; i++) {
            msg[i] = radioButtons.get(i - 1);
        }
        int res = JOptionPane.showConfirmDialog(actionPanel, msg, "Steal resource", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            int idx = -1;
            for (int i = 0; i < radioButtons.size(); i++) {
                if (radioButtons.get(i).isSelected()) {
                    idx = i;
                    break;
                }
            }
            if (idx != -1) {
                for (Player p : game.players) {
                    if (p.color == listChoices.get(idx)) {
                        game.takeRandomResource(p);
                        break;
                    }
                }
                return true;
            } else {
                JOptionPane.showMessageDialog(actionPanel, "No player selected");
            }
        } else {
            JOptionPane.showMessageDialog(actionPanel, "Operation cancelled");
        }
        return false;
    }

    private ArrayList<Color> robberTargets() {
        ArrayList<Color> listChoices = new ArrayList<>();
        for (Vertex v : game.board.tiles[game.board.robberLoc.getRow()][game.board.robberLoc.getCol()].getVertices()) {
            if (game.board.vertices[v.getRow()][v.getCol()] instanceof Building b) {
                boolean included = false;
                for (Color c : listChoices) {
                    if (c == b.owner.color) {
                        included = true;
                        break;
                    }
                }
                if (!included && b.owner.color != game.curPlayer.color) {
                    listChoices.add(b.owner.color);
                }
            }
        }
        return listChoices;
    }

    private void endTurn() {
        if (!diceRolled) {
            JOptionPane.showMessageDialog(actionPanel, "You must roll the dice");
            return;
        }
        int i = game.checkWin();
        if (i != -1) {
            StringBuilder sb = new StringBuilder(String.format("Player %d %s Wins\nFinal results:\n", i + 1, NAMES[i]));
            Player[] order = game.ranking();
            for (Player p : order) {
                sb.append(String.format("%s: %d", Colors.COLOR_TO_STRING.get(p.color), p.victoryPoints));
            }
            JOptionPane.showMessageDialog(windowFrame, sb.toString());
            gamePanel.removeAll();
            windowFrame.remove(gamePanel);
            windowFrame.revalidate();
            windowFrame.repaint();
        }
        game.nextTurn();
        diceRolled = false;
        displayName();
    }

    private void placeRoad() {
        if (!diceRolled) {
            JOptionPane.showMessageDialog(actionPanel, "You must roll dice first");
            return;
        }
        JComboBox<Integer> v1RowComboBox = new JComboBox<>(Board.ROW_INDEXES);
        JComboBox<Integer> v1ColComboBox = new JComboBox<>(Board.COL_INDEXES);
        JComboBox<Integer> v2RowComboBox = new JComboBox<>(Board.ROW_INDEXES);
        JComboBox<Integer> v2ColComboBox = new JComboBox<>(Board.COL_INDEXES);
        Object[] msg = {"Vertex 1 row position:", v1RowComboBox, "Vertex 1 column position:", v1ColComboBox,
                "Vertex 2 row position:", v2RowComboBox, "Vertex 2 column position:", v2ColComboBox};
        int res = JOptionPane.showConfirmDialog(actionPanel, msg,
                String.format("Player %d place road", game.curIndex + 1), JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            if (v1RowComboBox.getSelectedItem() != null && v1ColComboBox.getSelectedItem() != null &&
                    v2RowComboBox.getSelectedItem() != null && v2ColComboBox.getSelectedItem() != null) {
                Vertex u = new Vertex((int) (v1RowComboBox.getSelectedItem()), (int) (v1ColComboBox.getSelectedItem()));
                Vertex v = new Vertex((int) (v2RowComboBox.getSelectedItem()), (int) (v2ColComboBox.getSelectedItem()));
                if (game.placeRoad(new Edge(u, v))) {
                    JOptionPane.showMessageDialog(actionPanel, "Road built");
                    updateGUI();
                } else {
                    JOptionPane.showMessageDialog(actionPanel, "Unable to build");
                }
            }
        } else {
            JOptionPane.showMessageDialog(actionPanel, "Operation canceled.");
        }
    }

    private boolean placeSettlement(boolean setup) {
        if (!setup && !diceRolled) {
            JOptionPane.showMessageDialog(actionPanel, "You must roll dice first");
            return false;
        }
        JComboBox<Integer> rowComboBox = new JComboBox<>(Board.ROW_INDEXES);
        JComboBox<Integer> colComboBox = new JComboBox<>(Board.COL_INDEXES);
        Object[] msg = {"Row position:", rowComboBox, "Column position:", colComboBox};
        int res = JOptionPane.showConfirmDialog(actionPanel, msg,
                String.format("Player %d place settlement", game.curIndex + 1), JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            if (rowComboBox.getSelectedItem() != null && colComboBox.getSelectedItem() != null) {
                if (game.placeSettlement(new Building(game.curPlayer, (int) rowComboBox.getSelectedItem(),
                        (int) colComboBox.getSelectedItem(), Building.SETTLEMENT), setup)) {
                    JOptionPane.showMessageDialog(actionPanel, "Building built");
                    updateGUI();
                    return true;
                } else {
                    JOptionPane.showMessageDialog(actionPanel, "Unable to build");
                    return false;
                }
            }
        } else {
            JOptionPane.showMessageDialog(actionPanel, "Operation canceled.");
            return false;
        }
        return false;
    }

    private void placeCity() {
        if (!diceRolled) {
            JOptionPane.showMessageDialog(actionPanel, "You must roll dice first");
            return;
        }
        JComboBox<Integer> rowComboBox = new JComboBox<>(Board.ROW_INDEXES);
        JComboBox<Integer> colComboBox = new JComboBox<>(Board.COL_INDEXES);
        Object[] msg = {"Row position:", rowComboBox, "Column position:", colComboBox};
        int res = JOptionPane.showConfirmDialog(actionPanel, msg,
                String.format("Player %d place city", game.curIndex + 1), JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            if (rowComboBox.getSelectedItem() != null && colComboBox.getSelectedItem() != null) {
                if (game.placeCity(new Building(game.curPlayer, (int) rowComboBox.getSelectedItem(),
                        (int) colComboBox.getSelectedItem(), Building.CITY))) {
                    JOptionPane.showMessageDialog(actionPanel, "City built");
                    updateGUI();
                } else {
                    JOptionPane.showMessageDialog(actionPanel, "Unable to build");
                }
            }
        } else {
            JOptionPane.showMessageDialog(actionPanel, "Operation canceled.");
        }
    }

    private void updateGUI() {
        boardPanel.repaint();
        infoPanel.revalidate();
        infoPanel.repaint();
    }

    public static void main(String[] args) {
        Catan catan = new Catan();
        catan.run();
    }
}

package game;

import board.*;
import board.Point;
import constants.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static constants.Colors.COLOR_TO_STRING;
import static constants.Colors.NAMES;

/**
 * Settlers of Catan
 */
public class Catan {

    private final JFrame windowFrame; // Outermost JFrame window
    private JPanel menuPanel; // Panel for starting menu
    private JPanel gamePanel; // Container panel for gameplay
    private JPanel actionPanel; // Panel containing components for player actions
    private BoardPanel boardPanel; // BoardPanel object to display board state
    private InfoPanel infoPanel; // InfoPanel object to display player information

    private Font actionFont; // Font for components in action panel
    private JLabel playerLabel; // Label displaying current player
    private boolean diceRolled; // If current player has rolled the dice
    private JButton startGameButton; // Button to start game

    private Game game; // Game object
    private int setupIdx; // Index used for game setup
    // Row and column choices for combo boxes
    private static final Integer[] ROW_INDEXES = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    private static final Integer[] COL_INDEXES = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    /**
     * Creates a new Catan game
     */
    public Catan() {
        windowFrame = new JFrame("Settlers of Catan");
        windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowFrame.setPreferredSize(new Dimension(1800, 1000));
        windowFrame.setResizable(false);
        menuSetup(); // Display game menu
        windowFrame.pack();
        windowFrame.setVisible(true);
    }

    /**
     * Set up the game menu
     */
    private void menuSetup() {
        menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(1800, 1000));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel title = new JLabel("Settlers of Catan"); // Game title
        title.setFont(new Font(null, Font.PLAIN, 100));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton rulesButton = getRulesButton(); // Button to hyperlink to Catan game rules and almanac
        JButton threePlayer = new JButton("3 Players"); // Button to start 3 player game
        threePlayer.setFont(new Font(null, Font.PLAIN, 50));
        threePlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
        threePlayer.addActionListener(e -> gameSetup(3)); // Call game setup with 3 players
        JButton fourPlayer = new JButton("4 Players"); // Button to start 4 player game
        fourPlayer.setFont(new Font(null, Font.PLAIN, 50));
        fourPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
        fourPlayer.addActionListener(e -> gameSetup(4)); // Call game setup with 4 players
        JLabel catanImg = new JLabel(new ImageIcon("gui/catan_logo.png")); // Image of Catan
        catanImg.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(title);
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(rulesButton);
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

    /**
     * Create a button to open Catan game rules and almanac in browser
     *
     * @return {@code JButton} that opens Catan game rules and almanac in browser upon clicking
     */
    private JButton getRulesButton() {
        JButton rulesButton = new JButton("Game Rules and Almanac");
        rulesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rulesButton.setFont(new Font(null, Font.PLAIN, 40));
        rulesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        String url = "https://www.catan.com/sites/default/files/2021-06/catan_base_rules_2020_200707.pdf";
        rulesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(url));
                } catch (IOException | URISyntaxException ex) {
                    JOptionPane.showMessageDialog(windowFrame, "Failed to open link to game rules and almanac",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return rulesButton;
    }

    /**
     * Set up the gameplay
     *
     * @param numPlayers Number of players
     */
    private void gameSetup(int numPlayers) {
        game = new Game(numPlayers); // Create a new game with specified number of players
        setupIdx = 0; // Setup index set to 0
        menuPanel.removeAll();
        windowFrame.remove(menuPanel); // Remove menu
        gamePanel = new JPanel(); // Create outer container panel
        gamePanel.setPreferredSize(new Dimension(1800, 1000));
        gamePanel.setLayout(new BorderLayout());
        JPanel rightPanel = new JPanel(); // Right container panel to contain board panel and information panel
        rightPanel.setPreferredSize(new Dimension(1200, 1000));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.LIGHT_GRAY);
        setupActionPanel(); // Set up the action panel
        boardPanel = new BoardPanel(game.getBoard()); // Create board panel
        infoPanel = new InfoPanel(game.getPlayers()); // Create information panel
        rightPanel.add(boardPanel);
        rightPanel.add(infoPanel);
        gamePanel.add(actionPanel, BorderLayout.WEST); // Action panel on left
        gamePanel.add(rightPanel, BorderLayout.EAST); // Board and information panels on right
        windowFrame.add(gamePanel);
        windowFrame.revalidate();
        windowFrame.repaint();
    }

    /**
     * Set up the action panel
     */
    private void setupActionPanel() {
        actionPanel = new JPanel();
        diceRolled = false;
        actionPanel.setPreferredSize(new Dimension(600, 1000));
        actionPanel.setBackground(Color.YELLOW);
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        actionFont = new Font(null, Font.PLAIN, 18);
        JLabel gridReference = new JLabel(new ImageIcon("gui/resized_board.jpg")); // Reference image for coordinates
        gridReference.setAlignmentX(Component.CENTER_ALIGNMENT);
        actionPanel.add(gridReference);
        startGameButton = new JButton("Start Game"); // Button to start the game
        startGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startGameButton.setFont(actionFont);
        startGameButton.addActionListener(e -> initializeGame()); // Game setup phase if button pressed
        actionPanel.add(Box.createVerticalStrut(10));
        actionPanel.add(startGameButton);
    }

    /**
     * Perform the game setup phase where each player places 2 settlements, adhering to the distance rule
     * Each player places 1 settlement at a time, going according to this order:
     * 1, 2, 3, 3, 2, 1 for 3 player game
     * 1, 2, 3, 4, 4, 3, 2, 1 for 4 player game
     */
    private void initializeGame() {
        while (setupIdx < game.getNumPlayers() * 2) {
            if (setupIdx < game.getNumPlayers()) {
                game.setCurIdx(setupIdx); // If still placing first settlement
            } else {
                game.setCurIdx(2 * game.getNumPlayers() - setupIdx - 1); // Placing second settlement, order reversed
            }
            game.updateCurPlayer(); // Set the current player
            if (placeSettlement(true)) {
                setupIdx++; // Only move on if settlement placed properly
            }
        }
        game.distributeResources(-1); // Each player receives the resources that build a settlement upon
        game.setCurIdx(0);
        game.updateCurPlayer();
        actionPanel.remove(startGameButton); // Remove start game button
        playerLabel = new JLabel(""); // Label showing current player
        playerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerLabel.setFont(actionFont);
        JButton rollDiceButton = new JButton("Roll Dice"); // Button to roll dice
        rollDiceButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rollDiceButton.setFont(actionFont);
        rollDiceButton.addActionListener(e -> diceRoll()); // Call dice roll method
        JButton endTurnButton = new JButton("End Turn"); // Button to end turn
        endTurnButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        endTurnButton.setFont(actionFont);
        endTurnButton.addActionListener(e -> endTurn()); // Call end tun method
        JButton roadButton = new JButton("Build Road"); // Button road button
        roadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        roadButton.setFont(actionFont);
        roadButton.addActionListener(e -> placeRoad()); // Call place road method
        JButton settlementButton = new JButton("Build Settlement"); // Button settlement button
        settlementButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        settlementButton.setFont(actionFont);
        settlementButton.addActionListener(e -> placeSettlement(false)); // Call place settlement method
        JButton cityButton = new JButton("Build City"); // Build city button
        cityButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        cityButton.setFont(actionFont);
        cityButton.addActionListener(e -> placeCity()); // Call place city method
        JButton selfTradeButton = new JButton("4:1 Self Trade"); // Button to trade 4:1
        selfTradeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        selfTradeButton.setFont(actionFont);
        selfTradeButton.addActionListener(e -> genericTrade()); // Call generic trade method
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
        actionPanel.add(selfTradeButton);
        actionPanel.add(Box.createVerticalStrut(10));
        actionPanel.revalidate();
        actionPanel.repaint();
        displayName(); // Display current player
    }

    /**
     * Display the current player number and color
     */
    private void displayName() {
        playerLabel.setText("Player " + (game.getCurIdx() + 1) + " " + NAMES[game.getCurIdx()]);
    }

    /**
     * Current player rolls dice
     */
    private void diceRoll() {
        if (diceRolled) {
            JOptionPane.showMessageDialog(actionPanel, "You already rolled"); // Player can only roll once
        } else {
            int roll = game.rollDice();
            JOptionPane.showMessageDialog(actionPanel, "Dice Roll: " + roll);
            if (roll == 7) {
                game.halfResources(); // Halve resources for players more than 7 resources
                boolean moved;
                do {
                    moved = moveRobber(); // Player must move robber
                } while (!moved);
                boolean stolen;
                do {
                    stolen = stealResource(); // Player must steal resource if possible
                } while (!stolen);
            } else {
                game.distributeResources(roll); // Distribute resources if roll is not a 7
            }
            updateGUI(); // Update graphics
            diceRolled = true; // Set dice rolled to be true
        }
    }

    /**
     * Current player moves the robber
     *
     * @return {@code true} if moved successfully, {@code false} otherwise
     */
    private boolean moveRobber() {
        JComboBox<Integer> rowComboBox = new JComboBox<>(ROW_INDEXES);
        JComboBox<Integer> colComboBox = new JComboBox<>(COL_INDEXES);
        Object[] msg = {"Row position:", rowComboBox, "Column position:", colComboBox};
        int res = JOptionPane.showConfirmDialog(actionPanel, msg,
                String.format("Player %d move robber", game.getCurIdx() + 1), JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            if (rowComboBox.getSelectedItem() != null && colComboBox.getSelectedItem() != null) {
                if (game.getBoard().moveRobber(new Point((int) rowComboBox.getSelectedItem(),
                        (int) colComboBox.getSelectedItem()))) {
                    JOptionPane.showMessageDialog(actionPanel, "Robber moved");
                    updateGUI(); // Update graphics when robber moved
                    return true;
                } else {
                    JOptionPane.showMessageDialog(actionPanel, "Improper move"); // Invalid move
                    return false;
                }
            }
        } else {
            JOptionPane.showMessageDialog(actionPanel, "Operation canceled.");
            return false;
        }
        return false;
    }

    /**
     * Current player steals a random resource card from a player with a settlement that the robber is moved to
     *
     * @return {@code true} if operation successful, {@code false} otherwise
     */
    private boolean stealResource() {
        ArrayList<Color> listChoices = robberTargets();
        if (listChoices.isEmpty()) {
            return true; // If no one to steal from, operation finished
        }
        ButtonGroup group = new ButtonGroup(); // Group to ensure only 1 player selected
        ArrayList<JRadioButton> radioButtons = new ArrayList<>(); // Radio buttons for choices of players to steal from
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
                    idx = i; // Get index of selection
                    break;
                }
            }
            if (idx != -1) { // If a choice was selected
                for (Player p : game.getPlayers()) {
                    if (p.getColor() == listChoices.get(idx)) {
                        game.takeRandomResource(p); // Current player steal random resource from chosen player
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

    /**
     * Find possible players to steal resource card from after moving robber
     *
     * @return {@code ArrayList} of the {@code java.awt.Color} objects of players to steal from
     */
    private ArrayList<Color> robberTargets() {
        ArrayList<Color> listChoices = new ArrayList<>();
        int i = game.getBoard().getRobberLoc().getRow();
        int j = game.getBoard().getRobberLoc().getCol();
        for (Vertex v : game.getBoard().getTiles()[i][j].getVertices()) {
            // If the vertex at runtime is actually a building
            if (game.getBoard().getVertices()[v.getRow()][v.getCol()] instanceof Building b) {
                boolean included = false;
                for (Color c : listChoices) {
                    if (c == b.getOwner().getColor()) {
                        included = true; // Check if player already included
                        break;
                    }
                }
                if (!included && b.getOwner().getColor() != game.getCurPlayer().getColor()) {
                    listChoices.add(b.getOwner().getColor()); // Add player to list
                }
            }
        }
        return listChoices;
    }

    /**
     * End current player's turn and checks if game is over
     * If not, then move onto the next player's turn
     */
    private void endTurn() {
        if (!diceRolled) {
            JOptionPane.showMessageDialog(actionPanel, "You must roll the dice"); // Player must roll the dice
            return;
        }
        int i = game.checkWin();
        if (i != -1) { // If game is over
            StringBuilder sb = new StringBuilder(String.format("Player %d %s Wins\nFinal results:\n", i + 1, NAMES[i]));
            Player[] order = game.ranking(); // Get players sorted by victory points, descending
            for (Player p : order) {
                sb.append(String.format("%s: %d VP\n", Colors.COLOR_TO_STRING.get(p.getColor()), p.getVictoryPoints()));
            }
            JOptionPane.showMessageDialog(windowFrame, sb.toString()); // Display ending dialog
            System.exit(0); // Exit program
        }
        game.nextTurn(); // Proceed to next turn
        diceRolled = false; // Dice is not rolled
        displayName(); // Update player name
    }

    /**
     * Lets current player place a road
     */
    private void placeRoad() {
        if (!diceRolled) {
            JOptionPane.showMessageDialog(actionPanel, "You must roll dice first");
            return;
        }
        JComboBox<Integer> v1RowComboBox = new JComboBox<>(ROW_INDEXES);
        JComboBox<Integer> v1ColComboBox = new JComboBox<>(COL_INDEXES);
        JComboBox<Integer> v2RowComboBox = new JComboBox<>(ROW_INDEXES);
        JComboBox<Integer> v2ColComboBox = new JComboBox<>(COL_INDEXES);
        Object[] msg = {"Vertex 1 row position:", v1RowComboBox, "Vertex 1 column position:", v1ColComboBox,
                "Vertex 2 row position:", v2RowComboBox, "Vertex 2 column position:", v2ColComboBox};
        int res = JOptionPane.showConfirmDialog(actionPanel, msg,
                String.format("Player %d place road", game.getCurIdx() + 1), JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            if (v1RowComboBox.getSelectedItem() != null && v1ColComboBox.getSelectedItem() != null &&
                    v2RowComboBox.getSelectedItem() != null && v2ColComboBox.getSelectedItem() != null) {
                Vertex u = new Vertex((int) (v1RowComboBox.getSelectedItem()), (int) (v1ColComboBox.getSelectedItem()));
                Vertex v = new Vertex((int) (v2RowComboBox.getSelectedItem()), (int) (v2ColComboBox.getSelectedItem()));
                if (game.placeRoad(new Edge(u, v))) {
                    JOptionPane.showMessageDialog(actionPanel, "Road built");
                    updateGUI(); // Update when road is built successfully
                } else {
                    JOptionPane.showMessageDialog(actionPanel, "Unable to build");
                }
            }
        } else {
            JOptionPane.showMessageDialog(actionPanel, "Operation canceled.");
        }
    }

    /**
     * Lets current player build a settlement
     *
     * @param setup {@code true} if during game setup, {@code false} otherwise
     * @return {@code true} if built successfully, {@code false} otherwise
     */
    private boolean placeSettlement(boolean setup) {
        if (!setup && !diceRolled) {
            JOptionPane.showMessageDialog(actionPanel, "You must roll dice first"); // Dice must be rolled first
            return false;
        }
        JComboBox<Integer> rowComboBox = new JComboBox<>(ROW_INDEXES);
        JComboBox<Integer> colComboBox = new JComboBox<>(COL_INDEXES);
        Object[] msg = {"Row position:", rowComboBox, "Column position:", colComboBox};
        int res = JOptionPane.showConfirmDialog(actionPanel, msg,
                String.format("Player %d place settlement", game.getCurIdx() + 1), JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            if (rowComboBox.getSelectedItem() != null && colComboBox.getSelectedItem() != null) {
                if (game.placeSettlement(new Building(game.getCurPlayer(), (int) rowComboBox.getSelectedItem(),
                        (int) colComboBox.getSelectedItem(), Building.SETTLEMENT), setup)) {
                    JOptionPane.showMessageDialog(actionPanel, "Building built");
                    updateGUI(); // Update if building is built successfully
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

    /**
     * Lets current player build a city
     */
    private void placeCity() {
        if (!diceRolled) {
            JOptionPane.showMessageDialog(actionPanel, "You must roll dice first"); // Must roll dice first
            return;
        }
        JComboBox<Integer> rowComboBox = new JComboBox<>(ROW_INDEXES);
        JComboBox<Integer> colComboBox = new JComboBox<>(COL_INDEXES);
        Object[] msg = {"Row position:", rowComboBox, "Column position:", colComboBox};
        int res = JOptionPane.showConfirmDialog(actionPanel, msg,
                String.format("Player %d place city", game.getCurIdx() + 1), JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            if (rowComboBox.getSelectedItem() != null && colComboBox.getSelectedItem() != null) {
                if (game.placeCity(new Building(game.getCurPlayer(), (int) rowComboBox.getSelectedItem(),
                        (int) colComboBox.getSelectedItem(), Building.CITY))) {
                    JOptionPane.showMessageDialog(actionPanel, "City built");
                    updateGUI(); // Update if city is built
                } else {
                    JOptionPane.showMessageDialog(actionPanel, "Unable to build");
                }
            }
        } else {
            JOptionPane.showMessageDialog(actionPanel, "Operation canceled.");
        }
    }

    /**
     * Lets the player discard 4 identical resources to receive 1 resource of another type
     */
    private void genericTrade() {
        JRadioButton[] receiveButtons = new JRadioButton[Resource.RESOURCES.length];
        JRadioButton[] giveButtons = new JRadioButton[Resource.RESOURCES.length];
        ButtonGroup receiveGroup = new ButtonGroup(); // Group to select only 1 resource to receive
        ButtonGroup giveGroup = new ButtonGroup(); // Group to select only 1 resource to give
        for (int i = 0; i < Resource.RESOURCES.length; i++) {
            receiveButtons[i] = new JRadioButton(Resource.RESOURCES[i]);
            giveButtons[i] = new JRadioButton(Resource.RESOURCES[i]);
            receiveGroup.add(receiveButtons[i]);
            giveGroup.add(giveButtons[i]);
        }
        Object[] msg = new Object[receiveButtons.length + giveButtons.length + 2];
        msg[0] = "Resource to receive:";
        System.arraycopy(receiveButtons, 0, msg, 1, receiveButtons.length); // Shallow copy buttons into message array
        msg[receiveButtons.length + 1] = "Resource to give:";
        System.arraycopy(giveButtons, 0, msg, receiveButtons.length + 2, giveButtons.length);
        int res = JOptionPane.showConfirmDialog(actionPanel, msg, "4:1 Self Trade",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res == JOptionPane.OK_OPTION) {
            int receiveResourceIdx = -1;
            int giveResourceIdx = -1;
            for (int i = 0; i < receiveButtons.length; i++) {
                if (receiveButtons[i].isSelected()) {
                    receiveResourceIdx = i; // Get resource to receive
                    break;
                }
            }
            for (int i = 0; i < giveButtons.length; i++) {
                if (giveButtons[i].isSelected()) {
                    giveResourceIdx = i; // Get resource to give
                    break;
                }
            }
            if (receiveResourceIdx == -1 || giveResourceIdx == -1) {
                JOptionPane.showMessageDialog(actionPanel, "Improper trade"); // If one resource is not selected
            } else {
                if (game.getCurPlayer().selfTrade(receiveResourceIdx, giveResourceIdx)) {
                    JOptionPane.showMessageDialog(actionPanel,
                            String.format("Trade successful: 1 %s received for 4 %s",
                                    Resource.RESOURCES[receiveResourceIdx], Resource.RESOURCES[giveResourceIdx]));
                    updateGUI(); // Update if trade successful
                } else {
                    JOptionPane.showMessageDialog(actionPanel, "Improper trade");
                }
            }
        } else {
            JOptionPane.showMessageDialog(actionPanel, "Trade cancelled");
        }
    }

    /**
     * Update the board panel and information panel
     */
    private void updateGUI() {
        boardPanel.repaint();
        infoPanel.revalidate();
        infoPanel.repaint();
    }

    public static void main(String[] args) {
        Catan catan = new Catan();
    }
}

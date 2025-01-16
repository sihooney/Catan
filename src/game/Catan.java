package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Catan {

    private int gs = 0;
    private final JFrame windowFrame;
    private JPanel menuPanel;
    private JButton threePlayer;
    private JButton fourPlayer;
    private JPanel gamePanel;

    public Catan() {
        windowFrame = new JFrame("Settlers of Catan");
        windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowFrame.setPreferredSize(new Dimension(1800, 1000));
        menuSetup();
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
        threePlayer = new JButton("3 Players");
        threePlayer.setFont(new Font(null, Font.PLAIN, 80));
        threePlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
        threePlayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        fourPlayer = new JButton("4 Players");
        fourPlayer.setFont(new Font(null, Font.PLAIN, 80));
        fourPlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
        fourPlayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        menuPanel.add(title);
        menuPanel.add(Box.createVerticalStrut(50));
        menuPanel.add(threePlayer);
        menuPanel.add(Box.createVerticalStrut(50));
        menuPanel.add(fourPlayer);
        windowFrame.add(menuPanel);
    }

    private void gameSetup() {

    }

    public static void main(String[] args) {
        Catan catan = new Catan();
    }
}

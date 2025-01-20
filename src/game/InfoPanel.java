package game;

import constants.Items;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

import static constants.Colors.NAMES;

/**
 * Custom cell renderer class to render cells in JTable
 */
class CustomCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        JTextArea textArea = new JTextArea(value.toString());
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setOpaque(true);
        textArea.setEditable(false);
        int height = (int) (textArea.getPreferredSize().getHeight());
        table.setRowHeight(row, height);
        return textArea;
    }
}

/**
 * Display all player information
 */
public class InfoPanel extends JPanel {

    public InfoPanel(Player[] players) {
        setPreferredSize(new Dimension(1200, 300));
        setLayout(new FlowLayout());
        setBackground(Color.LIGHT_GRAY);
        JTextArea buildingCosts = new JTextArea(Items.BUILDING_COSTS); // Display building costs
        buildingCosts.setEditable(false);
        buildingCosts.setFont(new Font(null, Font.PLAIN, 14));
        DefaultTableModel model = new DefaultTableModel(); // Create model for JTable
        model.addColumn("Player");
        model.addColumn("Information");
        for (int i = 0; i < players.length; i++) {
            Object[] obj = new Object[2];
            obj[0] = String.format("Player %d: %s", i + 1, NAMES[i]); // Add player name and color
            obj[1] = players[i]; // Add player object to row
            model.addRow(obj);
        }
        JTable table = new JTable(model); // Create JTable
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(600);
        table.getColumnModel().getColumn(1).setCellRenderer(new CustomCellRenderer()); // Set custom cell renderer
        JScrollPane scrollPane = new JScrollPane(table); // Wrap JTable in a scroll pane
        scrollPane.setPreferredSize(new Dimension(750, 290));
        add(buildingCosts);
        add(scrollPane);
    }
}

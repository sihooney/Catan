package game;

import constants.Items;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

import static constants.Colors.NAMES;

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

public class InfoPanel extends JPanel {

    public InfoPanel(Player[] players) {
        setPreferredSize(new Dimension(1200, 300));
        setLayout(new FlowLayout());
        setBackground(Color.LIGHT_GRAY);
        JTextArea buildingCosts = new JTextArea(Items.BUILDING_COSTS);
        buildingCosts.setEditable(false);
        buildingCosts.setFont(new Font(null, Font.PLAIN, 14));
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Player");
        model.addColumn("Information");
        for (int i = 0; i < players.length; i++) {
            Object[] obj = new Object[2];
            obj[0] = String.format("Player %d: %s", i + 1, NAMES[i]);
            obj[1] = players[i];
            model.addRow(obj);
        }
        JTable table = new JTable(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(600);
        table.getColumnModel().getColumn(1).setCellRenderer(new CustomCellRenderer());
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(750, 290));
        add(buildingCosts);
        add(scrollPane);
    }
}

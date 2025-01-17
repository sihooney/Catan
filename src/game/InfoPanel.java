package game;

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

    private final Player[] players;
    private DefaultTableModel model;
    private JTable table;
    private JScrollPane scrollPane;

    public InfoPanel(Player[] players) {
        this.players = players;
        setPreferredSize(new Dimension(1200, 300));
        setLayout(new FlowLayout());
        setBackground(Color.LIGHT_GRAY);
        model = new DefaultTableModel();
        model.addColumn("Color");
        model.addColumn("Player");
        for (int i = 0; i < players.length; i++) {
            Object[] obj = new Object[2];
            obj[0] = NAMES[i];
            obj[1] = players[i];
            model.addRow(obj);
        }
        table = new JTable(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(900);
        table.getColumnModel().getColumn(1).setCellRenderer(new CustomCellRenderer());
        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(1000, 290));
        this.add(scrollPane);
    }
}

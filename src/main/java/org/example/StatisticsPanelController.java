package org.example;

import javax.swing.*;
import java.awt.*;

public class StatisticsPanelController {
    public static void statistics_show(JPanel panel) {
        panel.removeAll();
        panel.setLayout(new FlowLayout());

        JLabel label = new JLabel("Hello Statistics");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Malgun Gothic", Font.BOLD, 24));

        panel.add(label);
        panel.revalidate();
        panel.repaint();
    }
}

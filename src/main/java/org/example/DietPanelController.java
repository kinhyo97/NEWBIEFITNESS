package org.example;

import javax.swing.*;
import java.awt.*;

public class DietPanelController {
    public static void diet_show(JPanel panel) {
        panel.removeAll();
        panel.setLayout(new FlowLayout());

        JLabel label = new JLabel("Hello Diet");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Malgun Gothic", Font.BOLD, 24));

        panel.add(label);
        panel.revalidate();
        panel.repaint();
    }
}

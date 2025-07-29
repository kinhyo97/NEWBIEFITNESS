package org.example;

import javax.swing.*;
import java.awt.*;

public class TipPanelController {
    public static void tip_show(JPanel panel) {
        panel.removeAll();
        panel.setLayout(new FlowLayout());

        JLabel label = new JLabel("Hello Tip");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Malgun Gothic", Font.BOLD, 24));

        panel.add(label);
        panel.revalidate();
        panel.repaint();
    }
}

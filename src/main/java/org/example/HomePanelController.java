package org.example;

import javax.swing.*;
import java.awt.*;

public class HomePanelController {

    public static void home_show(JPanel panel) {
        panel.removeAll();

        JLabel label = new JLabel("Hello Home");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Malgun Gothic", Font.BOLD, 24));

        panel.setLayout(new FlowLayout());
        panel.add(label);

        panel.revalidate();
        panel.repaint();
    }
}
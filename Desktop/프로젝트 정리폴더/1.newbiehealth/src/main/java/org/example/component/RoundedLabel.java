package org.example.component;

import javax.swing.*;
import java.awt.*;

public class RoundedLabel extends JLabel {

    public RoundedLabel(String text) {
        super(text, SwingConstants.CENTER);
        setFont(new Font("굴림", Font.BOLD, 13));
        setOpaque(false); // 배경 직접 그림
        setForeground(Color.DARK_GRAY);
        setPreferredSize(new Dimension(90, 30));
        setMaximumSize(new Dimension(90, 30));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(255, 228, 235)); // 연핑크 배경
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15); // 🔴 라운드 처리

        super.paintComponent(g2);
        g2.dispose();
    }
}

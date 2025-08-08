package org.example.component;

import javax.swing.*;
import java.awt.*;

public class RoundSelectButton extends JToggleButton {

    private Color selectedColor = new Color(255, 192, 203); // 연핑크
    private Color defaultColor = Color.WHITE;
    private Color borderColor = new Color(220, 220, 220);

    public RoundSelectButton(String text) {
        super(text);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFont(new Font("맑은 고딕", Font.BOLD, 14));
        setForeground(Color.BLACK);
        setPreferredSize(new Dimension(120, 40));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int arc = 30;

        // 배경 색상 설정
        if (isSelected()) {
            g2.setColor(selectedColor);
        } else {
            g2.setColor(defaultColor);
        }
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        // 테두리
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        // 텍스트
        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    public boolean isContentAreaFilled() {
        return false; // 배경 직접 그림
    }
}

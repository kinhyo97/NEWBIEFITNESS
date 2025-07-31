package org.example.component;

import javax.swing.*;
import java.awt.*;

public class RoundButton1 extends JButton {
    private int radius;

    public RoundButton1(String text, int radius) {
        super(text);
        this.radius = radius;
        setFocusPainted(false); // 포커스 테두리 제거
        setContentAreaFilled(false); // 기본 버튼 배경 제거
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // 안쪽 여백

        // 🔴 버튼 배경을 연핑크로 지정
        setBackground(new Color(255, 182, 193)); // LightPink
        setForeground(Color.WHITE); // 텍스트 색 (필요 시 변경)
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(getBackground());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(getForeground()); // 테두리 색
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        g2.dispose();
    }
}
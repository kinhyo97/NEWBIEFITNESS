package org.example.component;

import javax.swing.*;
import java.awt.*;

public class CircularProgressBar extends JPanel {
    private int progress = 0;  // 0~100

    public CircularProgressBar() {
        setPreferredSize(new Dimension(150, 150));
        setOpaque(false);
    }

    public void setProgress(int value) {
        this.progress = value;
        repaint();
    }

    public int getProgress() {
        return this.progress;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();
        int size = Math.min(width, height);  // 패널 크기만큼 원도 맞추기
        int thickness = 20;

        int arcSize = size - thickness;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 배경 원
        g2.setColor(Color.DARK_GRAY);
        g2.setStroke(new BasicStroke(thickness));
        g2.drawArc((width - arcSize) / 2, (height - arcSize) / 2, arcSize, arcSize, 0, 360);

        // 진행 원
        g2.setColor(Color.MAGENTA);
        g2.drawArc((width - arcSize) / 2, (height - arcSize) / 2, arcSize, arcSize, 90, -(int)(360 * (progress / 100.0)));

        // 텍스트
        String text = progress + "%";
        g2.setFont(new Font("Malgun Gothic", Font.BOLD, size / 6));  // 텍스트 크기도 비례
        FontMetrics fm = g2.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        int y = (height + fm.getAscent()) / 2 - 5;

        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);

        g2.dispose();
    }
}
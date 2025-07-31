package org.example.component;

import javax.swing.*;
import java.awt.*;

class RoundedPanel1 extends JPanel {
    private int cornerRadius;

    public RoundedPanel1(int radius) {
        super();
        this.cornerRadius = radius;
        setOpaque(false); // 꼭 필요함! 배경 투명 유지
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension arcs = new Dimension(cornerRadius, cornerRadius);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcs.width, arcs.height);
        g2.dispose();
    }}
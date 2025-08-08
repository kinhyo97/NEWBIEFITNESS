package org.example.component;

import javax.swing.border.Border;
import java.awt.*;

class RoundedBorder implements Border {
    private final int radius;
    private final Color borderColor;

    public RoundedBorder(int radius, Color color) {
        this.radius = radius;
        this.borderColor = color;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(radius, radius, radius, radius);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(4));
        g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        g2.dispose();
    }
}
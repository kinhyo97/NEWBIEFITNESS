package org.example.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SmallButton extends JComponent {
    private String buttonText;
    private boolean hover = false;

    public SmallButton(String buttonText) {
        this.buttonText = buttonText;
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }
        });
    }

    // ✅ 크기 고정
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(120, 20);
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 배경 색
        if (hover) {
            g2.setColor(new Color(100, 100, 100));
        } else {
            g2.setColor(new Color(70, 70, 70));
        }
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

        // 텍스트
        g2.setColor(Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(buttonText);
        int textHeight = fm.getAscent();
        g2.drawString(buttonText, (getWidth() - textWidth) / 2, (getHeight() + textHeight) / 2 - 3);
    }



}

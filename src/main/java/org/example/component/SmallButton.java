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
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 🔴 버튼 배경 (hover 시 더 진하게)
        g2.setColor(hover ? new Color(255, 105, 180) : new Color(255, 182, 193)); // 핫핑크 / 연핑크
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

        // 텍스트
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(buttonText)) / 2;
        int y = (getHeight() + fm.getAscent()) / 2 - 4;
        g2.drawString(buttonText, x, y);

        g2.dispose();
    }


}

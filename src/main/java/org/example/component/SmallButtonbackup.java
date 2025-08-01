package org.example.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SmallButtonbackup extends JComponent {
    private String buttonText;
    private boolean hover = false;

    public SmallButtonbackup(String buttonText) {
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

    }


    public void addActionListener(Object o) {
    }
}

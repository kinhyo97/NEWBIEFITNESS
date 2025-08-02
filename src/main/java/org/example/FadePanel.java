package org.example;

import javax.swing.*;
import java.awt.*;

public class FadePanel extends JPanel {
    private float alpha = 0.0f;
    private Timer timer; // final 제거

    public FadePanel(JPanel content) {
        setLayout(new BorderLayout());
        add(content, BorderLayout.CENTER);
        setOpaque(false);

        // ⚠️ 여기서 초기화
        timer = new Timer(40, e -> {
            alpha += 0.02f;
            if (alpha >= 1f) {
                alpha = 1f;
                timer.stop();
            }
            repaint();
        });
    }

    public void startFadeIn() {
        alpha = 0.0f;
        timer.start();
    }

    @Override
    protected void paintChildren(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        super.paintChildren(g2);
        g2.dispose();
    }
}

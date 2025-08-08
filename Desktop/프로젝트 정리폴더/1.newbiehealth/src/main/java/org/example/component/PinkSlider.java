package org.example.component;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;

// Slider 구현을 위한 커스텀 슬라이더
public class PinkSlider extends JSlider {
    public PinkSlider(int min, int max, int value) {
        super(min, max, value);

        setOpaque(false);
        setPaintTicks(true);
        setPaintTrack(true);
        setPaintLabels(true);
        setMajorTickSpacing(30);
        setMinorTickSpacing(5);
        setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        setForeground(Color.PINK);
        setPreferredSize(new Dimension(500, 50));
        setMaximumSize(new Dimension(500, 50));
        setUI(new PinkSliderUI(this));
        setBorder(BorderFactory.createEmptyBorder(10, 15, 0, 0));
    }

    private static class PinkSliderUI extends BasicSliderUI {
        public PinkSliderUI(JSlider slider) {
            super(slider);
        }

        @Override
        public void paintThumb(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.PINK);
            g2.fillOval(thumbRect.x, thumbRect.y, 16, 16);
            g2.dispose();
        }

        @Override
        public void paintTrack(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillRoundRect(trackRect.x, trackRect.y + 6, trackRect.width, 4, 8, 8);
            g2.dispose();
        }

        @Override
        public void paintTicks(Graphics g) {
            super.paintTicks(g); // 기본 틱 사용
        }
    }
}

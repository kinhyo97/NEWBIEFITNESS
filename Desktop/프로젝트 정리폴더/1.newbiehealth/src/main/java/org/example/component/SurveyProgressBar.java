package org.example.component;

import javax.swing.*;
import java.awt.*;

public class SurveyProgressBar extends JProgressBar {

    public SurveyProgressBar(int min, int max) {
        super(min, max);
        setOpaque(false); // 배경 투명
        setForeground(Color.PINK); // 진행 색
        setBackground(Color.LIGHT_GRAY); // 배경색
        setBorder(BorderFactory.createEmptyBorder(20, 5, 0, 5)); // 위 20px, 좌우 5px 여백
        setStringPainted(true); // "20%" 글씨 표시
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int width = getWidth();
        int height = getHeight();
        int arc = 5;

        // 배경 바
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, width, height, arc, arc);

        // 채워진 바
        int fillWidth = (int) (getPercentComplete() * width);
        g2.setColor(getForeground());
        g2.fillRoundRect(0, 0, fillWidth, height, arc, arc);

        // 글자
        g2.setColor(Color.DARK_GRAY);
        g2.setFont(getFont());
        String percentText = String.format("", (int) (getPercentComplete() * 100));
        FontMetrics fm = g2.getFontMetrics();
        int textX = (width - fm.stringWidth(percentText)) / 2;
        int textY = (height + fm.getAscent()) / 2 - 2;
        g2.drawString(percentText, textX, textY);

        g2.dispose();
    }
}

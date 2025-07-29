package org.example;

import javax.swing.*;
import java.awt.*;


public class StatisticsPanelController {
    public static void statistics_show(JPanel panel) {
        panel.removeAll();
        panel.setLayout(new BorderLayout());  // flowLayout 에서 수정

        JLabel label = new JLabel("Statistics");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Malgun Gothic", Font.BOLD, 24));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);  // 수직 중앙 정렬
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.NORTH); // 상단 제목 배치


        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.BLACK);
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 신체 정보 및 근력 정보
        JPanel infoPanel = new JPanel(new GridLayout(1, 2));
        infoPanel.setPreferredSize(new Dimension(300, 60));
        infoPanel.setMaximumSize(new Dimension(300, 300));
        infoPanel.setBackground(Color.DARK_GRAY);
        infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);


        JLabel bodyInfo = new JLabel("신체 정보 : ");
        bodyInfo.setForeground(Color.WHITE);
        bodyInfo.setFont(new Font("Malgun Gothic", Font.PLAIN, 16));

        JLabel strengthInfo = new JLabel("근력 정보 : ");
        strengthInfo.setForeground(Color.WHITE);
        strengthInfo.setFont(new Font("Malgun Gothic", Font.PLAIN, 16));

        infoPanel.add(bodyInfo);
        infoPanel.add(strengthInfo);

        JPanel ratePanel = new JPanel();
        ratePanel.setBackground(Color.BLACK);
        ratePanel.setMaximumSize(new Dimension(400, 200));
        ratePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ratePanel.setLayout(new BoxLayout(ratePanel, BoxLayout.Y_AXIS));
        ratePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JLabel achievement = new JLabel("운동 목표 달성률");
        achievement.setFont(new Font("Malgun Gothic", Font.BOLD, 30));
        achievement.setForeground(Color.YELLOW);
        achievement.setAlignmentX(Component.CENTER_ALIGNMENT);
        ratePanel.add(achievement);

        // 가운데 도넛 그래프 코드
        DonutProgressCircle donut = new DonutProgressCircle(90);
        donut.setAlignmentX(Component.CENTER_ALIGNMENT);

        // createVerticalStrut() 메소드 : 고정 수직 여백 설정
        centerPanel.add(Box.createVerticalStrut(15));
        centerPanel.add(infoPanel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(ratePanel);
        centerPanel.add(Box.createVerticalStrut(50));
        centerPanel.add(donut);
        centerPanel.add(Box.createVerticalStrut(15));

        panel.add(centerPanel, BorderLayout.CENTER);

        panel.revalidate();
        panel.repaint();
    }
}


class DonutProgressCircle extends JPanel {
    private int progress = 0;

    public DonutProgressCircle(int progress) {
        this.progress = progress;
        setPreferredSize(new Dimension(200, 200));
        setBackground(Color.BLACK);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {  // 부모 클래스의 페인팅 로직
        super.paintComponent(g);  // 부모 클래스의 페인팅 로직(화면 지우기 등)
        // 패널 중앙에 원을 그림

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int size = Math.min(getWidth(), getHeight() - 20);
        int thickness = 25;

        int x = (getWidth() - size) / 2;  // 전체 너비에서 지름만큼 빼고 2로 나눔 -> 가운데 정렬
        int y = (getHeight() - size) / 2 - 10;

        // 배경 원
        g2d.setColor(Color.DARK_GRAY);
        g2d.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawArc(x, y, size, size, 90, 360);

        // 진행률 원
        g2d.setColor(Color.magenta);
        int angle = -(int) (360 * (progress / 100.0));
        g2d.drawArc(x, y, size, size, 90, angle);

        // 중앙 텍스트
        String text = progress + "%";
        Font font = new Font("Malgun Gothic", Font.BOLD, 30);
        g2d.setFont(font);

        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();

        g2d.setColor(Color.WHITE);
        g2d.drawString(text, getWidth() / 2 - textWidth / 2, getHeight() / 2 + textHeight / 4);
        g2d.dispose();
    }
}

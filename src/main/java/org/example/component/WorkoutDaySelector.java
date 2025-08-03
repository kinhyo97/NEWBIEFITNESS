package org.example.component;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class WorkoutDaySelector extends JPanel {

    private final Map<String, JToggleButton> dayToggleMap = new LinkedHashMap<>();

    public WorkoutDaySelector() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(360, 999));

        String[] days = {"월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일"};

        for (String day : days) {
            JPanel row = new JPanel();
            row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
            row.setBackground(Color.WHITE);
            row.setAlignmentX(Component.LEFT_ALIGNMENT);
            row.setMaximumSize(new Dimension(320, 40));
            row.setPreferredSize(new Dimension(320, 40));

            // 🔹 요일 라벨
            RoundedLabel dayLabel = new RoundedLabel(day);
            dayLabel.setFont(new Font("굴림", Font.BOLD, 13));
            dayLabel.setForeground(Color.DARK_GRAY);
            dayLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); // padding
            dayLabel.setPreferredSize(new Dimension(90, 30));
            dayLabel.setMaximumSize(new Dimension(90, 30));
            dayLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

            // 🔹 토글 버튼
            JToggleButton toggle = new JToggleButton("OFF") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(isSelected() ? new Color(255, 182, 193) : Color.LIGHT_GRAY);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    super.paintComponent(g);
                    g2.dispose();
                }
            };
            toggle.setFont(new Font("굴림", Font.BOLD, 13));
            toggle.setFocusPainted(false);
            toggle.setContentAreaFilled(false);
            toggle.setBorderPainted(false);         // ✅ 네모 테두리 제거
            toggle.setOpaque(false);
            toggle.setForeground(Color.BLACK);
            toggle.setPreferredSize(new Dimension(80, 30));
            toggle.setMaximumSize(new Dimension(80, 30));
            toggle.setAlignmentY(Component.CENTER_ALIGNMENT);

            toggle.addActionListener(e -> {
                toggle.setText(toggle.isSelected() ? "ON" : "OFF");
            });

            // 🔹 조합
            row.add(dayLabel);
            row.add(Box.createHorizontalStrut(20));
            row.add(toggle);

            add(row);
            add(Box.createVerticalStrut(10));

            dayToggleMap.put(day, toggle);
        }
    }

    public List<String> getSelectedDays() {
        List<String> selected = new ArrayList<>();
        for (Map.Entry<String, JToggleButton> entry : dayToggleMap.entrySet()) {
            if (entry.getValue().isSelected()) {
                selected.add(entry.getKey());
            }
        }
        return selected;
    }
}

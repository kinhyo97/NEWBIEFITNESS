package org.example.pages;

import org.example.UIUtils;

import javax.swing.*;
import java.awt.*;
import org.example.component.WorkoutDaySelector;

public class PageWorkoutDays extends JPanel {
    private WorkoutDaySelector selector;

    public PageWorkoutDays() {
        setBackground(Color.WHITE);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // 여백 설정

        // 🔷 제목 라벨 (2줄)
        JLabel label1 = new JLabel("운동 유형을 알려주시면");
        label1.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        label1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label2 = new JLabel("플랜을 계획적으로 세워드려요");
        label2.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        label2.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        labelPanel.setBackground(Color.WHITE);
        labelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelPanel.add(label1);
        labelPanel.add(label2);

        add(labelPanel);
        add(Box.createVerticalStrut(30)); // 제목 아래 간격

        JPanel title = UIUtils.createLabelOnlyRow("운동 가능한 요일을 선택해주세요");
        add(title);

        selector = new WorkoutDaySelector();

        selector = new WorkoutDaySelector();
        selector.setAlignmentX(Component.LEFT_ALIGNMENT); // 🔑 반드시 명시

        add(selector); // ✅ wrapper 없이 직접 추가
    }


    public java.util.List<String> getSelectedDays() {
        return selector.getSelectedDays();
    }

}

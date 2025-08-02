package org.example.pages;

import javax.swing.*;
import java.awt.*;
import org.example.UIUtils;
import org.example.component.PinkSlider;

public class PageExerciseHistory extends JPanel {
    public PageExerciseHistory() {
        setBackground(Color.WHITE);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // 여백 설정

        // 🔷 제목 라벨 (2줄)
        JLabel label1 = new JLabel("당신의 운동경력을 알려주시면");
        label1.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        label1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label2 = new JLabel("계획에 맞게 운동을 추천해줘요");
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

        // 🔶 여기에 본문 입력 영역 또는 선택지 컴포넌트 삽입
        // 예시로 빈 JLabel 추가
        JPanel title = UIUtils.createLabelOnlyRow("당신의 키는?");
        add(title);

        JLabel ageLabel = new JLabel();  // 초기화만 해두고 아래서 값 설정
        ageLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        add(ageLabel);

        JSlider ageslider = new PinkSlider(0, 250, 50); // 예시: 0~100 범위, 초기 50
        ageslider.setMajorTickSpacing(30);
        ageslider.setPaintTicks(true);
        add(ageslider);
        ageslider.addChangeListener(e -> {
            int newValue = ageslider.getValue();
            ageLabel.setText(newValue + "cm");
        });
        ageLabel.setText(ageslider.getValue() + "cm");
    }
}

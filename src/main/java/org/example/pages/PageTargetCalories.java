package org.example.pages;

import javax.swing.*;
import java.awt.*;
import org.example.UIUtils;
import org.example.component.*;

public class PageTargetCalories extends JPanel {
    public PageTargetCalories() {
        setBackground(Color.WHITE);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // 여백 설정

        // 🔷 제목 라벨 (2줄)
        JLabel label1 = new JLabel("현재 다이어트 상태 및");
        label1.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        label1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label2 = new JLabel("섭취를 늘리고 싶은 영양소를 알려주세요");
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

        JPanel title = UIUtils.createLabelOnlyRow("현재 다이어트 상태");
        add(title);

        RoundSelectButton selectButtonbtn = new RoundSelectButton("다이어트");
        RoundSelectButton selectButtonbtn2 = new RoundSelectButton("벌크업");
        RoundSelectButton selectButtonbtn3 = new RoundSelectButton("유지어트");

// 버튼 그룹 생성
        ButtonGroup group = new ButtonGroup();
        group.add(selectButtonbtn);
        group.add(selectButtonbtn2);
        group.add(selectButtonbtn3);

        add(selectButtonbtn);
        add(Box.createVerticalStrut(15));
        add(selectButtonbtn2);
        add(Box.createVerticalStrut(15));
        add(selectButtonbtn3);
        add(Box.createVerticalStrut(15));



        JLabel caloriesLable = new JLabel();  // 초기화만 해두고 아래서 값 설정
        caloriesLable.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        add(caloriesLable);
        JPanel title2 = UIUtils.createLabelOnlyRow("목표 칼로리");
        add(title2);
        JSlider Calorieslider = new PinkSlider(0, 4000, 50); // 예시: 0~100 범위, 초기 50
        Calorieslider.setMajorTickSpacing(30);
        Calorieslider.setPaintTicks(true);
        Calorieslider.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(Calorieslider);
        Calorieslider.addChangeListener(e -> {
            int newValue = Calorieslider.getValue();
            caloriesLable.setText(newValue + " Kcal");
        });
        caloriesLable.setText(Calorieslider.getValue() + " Kcal");
    }
}

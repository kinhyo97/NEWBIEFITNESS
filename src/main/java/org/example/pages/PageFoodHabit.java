package org.example.pages;

import org.example.UIUtils;
import org.example.component.RoundSelectButton;

import javax.swing.*;
import java.awt.*;

public class PageFoodHabit extends JPanel {
    public PageFoodHabit() {
        setBackground(Color.WHITE);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // 여백 설정

        // 🔷 제목 라벨 (2줄)
        JLabel label1 = new JLabel("식습관을 알려주시면");
        label1.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        label1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label2 = new JLabel("맛있는 식단을 계획해드릴게요");
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

        JPanel title = UIUtils.createLabelOnlyRow("일일 식사 횟수는 어떻게 되시나요?");
        add(title);

        RoundSelectButton selectButtonbtn = new RoundSelectButton("1회");
        RoundSelectButton selectButtonbtn2 = new RoundSelectButton("2회");
        RoundSelectButton selectButtonbtn3 = new RoundSelectButton("3회");

// 버튼 그룹 생성
        ButtonGroup group = new ButtonGroup();
        group.add(selectButtonbtn);
        group.add(selectButtonbtn2);
        group.add(selectButtonbtn3);

// 패널에 추가
        add(selectButtonbtn);
        add(Box.createVerticalStrut(15));
        add(selectButtonbtn2);
        add(Box.createVerticalStrut(15));
        add(selectButtonbtn3);
        add(Box.createVerticalStrut(15));

        JPanel title2 = UIUtils.createLabelOnlyRow("식단 제한 유형이 있으신가요?");
        add(title2);

        RoundSelectButton selectButtonbtn4 = new RoundSelectButton("채식");
        RoundSelectButton selectButtonbtn5 = new RoundSelectButton("저탄수");
        RoundSelectButton selectButtonbtn6 = new RoundSelectButton("고단백");
        RoundSelectButton selectButtonbtn7 = new RoundSelectButton("없음");

        ButtonGroup group2 = new ButtonGroup();
        group2.add(selectButtonbtn4);
        group2.add(selectButtonbtn5);
        group2.add(selectButtonbtn6);
        group2.add(selectButtonbtn7);

        add(selectButtonbtn4);
        add(Box.createVerticalStrut(15));
        add(selectButtonbtn5);
        add(Box.createVerticalStrut(15));
        add(selectButtonbtn6);
        add(Box.createVerticalStrut(15));
        add(selectButtonbtn7);
        add(Box.createVerticalStrut(15));



    }
}

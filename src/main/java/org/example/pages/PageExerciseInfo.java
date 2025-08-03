package org.example.pages;

import org.example.UIUtils;
import org.example.component.RoundSelectButton;

import javax.swing.*;
import java.awt.*;

public class PageExerciseInfo extends JPanel {
    public PageExerciseInfo() {
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

        JPanel title = UIUtils.createLabelOnlyRow("어디에서 주로 운동하시나요?");
        add(title);

        RoundSelectButton selectButtonbtn = new RoundSelectButton("집");
        RoundSelectButton selectButtonbtn2 = new RoundSelectButton("헬스장");
        RoundSelectButton selectButtonbtn3 = new RoundSelectButton("야외");

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

        JPanel title2 = UIUtils.createLabelOnlyRow("언제 주로 운동하시나요?");
        add(title2);


        RoundSelectButton selectButtonbtn4 = new RoundSelectButton("아침");
        RoundSelectButton selectButtonbtn5 = new RoundSelectButton("점심");
        RoundSelectButton selectButtonbtn6 = new RoundSelectButton("저녁");

        ButtonGroup group2 = new ButtonGroup();
        group2.add(selectButtonbtn4);
        group2.add(selectButtonbtn5);
        group2.add(selectButtonbtn6);

        add(selectButtonbtn4);
        add(Box.createVerticalStrut(15));
        add(selectButtonbtn5);
        add(Box.createVerticalStrut(15));
        add(selectButtonbtn6);
        add(Box.createVerticalStrut(15));

    }
}

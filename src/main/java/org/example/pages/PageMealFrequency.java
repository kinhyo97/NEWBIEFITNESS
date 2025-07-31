package org.example.pages;

import org.example.UIUtils;
import org.example.component.RoundTextField1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PageMealFrequency extends JPanel {
    public PageMealFrequency() {
        setBackground(Color.WHITE);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // 여백 설정

        // 🔷 제목 라벨 (2줄)
        JLabel label1 = new JLabel("맞춤 목표 계산 시작!");
        label1.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        label1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label2 = new JLabel("기본 정보를 알려주세요");
        label2.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        label2.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        labelPanel.setBackground(Color.WHITE);
        labelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelPanel.add(label1);
        labelPanel.add(label2);

        add(labelPanel);
        add(Box.createVerticalStrut(30));

        //성별 label
        JLabel genderTitle = new JLabel("성별");
        genderTitle.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        genderTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(genderTitle);
        add(Box.createVerticalStrut(10)); // 제목 아래 여백

// 2. 성별 선택 패널
        JPanel genderSelectPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        genderSelectPanel.setBackground(Color.WHITE);
        genderSelectPanel.setMaximumSize(new Dimension(400, 150));
        genderSelectPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

// 아이콘 공통 경로
        ImageIcon genderIcon = UIUtils.createRoundedIcon("/icons/newbiehealthlogo.png", 80);


// 3. 여성 패널
        JPanel femalePanel = new JPanel();
        femalePanel.setLayout(new BoxLayout(femalePanel, BoxLayout.Y_AXIS));
        femalePanel.setBackground(Color.WHITE);

        JLabel femaleIcon = new JLabel(genderIcon);
        femaleIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel femaleLabel = new JLabel("여성");
        femaleLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        femaleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        femalePanel.add(femaleIcon);
        femalePanel.add(Box.createVerticalStrut(5));
        femalePanel.add(femaleLabel);

// 4. 남성 패널
        JPanel malePanel = new JPanel();
        malePanel.setLayout(new BoxLayout(malePanel, BoxLayout.Y_AXIS));
        malePanel.setBackground(Color.WHITE);

        JLabel maleIcon = new JLabel(genderIcon);
        maleIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel maleLabel = new JLabel("남성");
        maleLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        maleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        malePanel.add(maleIcon);
        malePanel.add(Box.createVerticalStrut(5));
        malePanel.add(maleLabel);

// 5. 패널에 추가
        genderSelectPanel.add(femalePanel);
        genderSelectPanel.add(malePanel);
        add(Box.createVerticalStrut(10)); // 제목 아래 여백

// 6. 전체 레이아웃에 추가
        add(genderSelectPanel);

        JPanel ageWrapper = new JPanel();
        ageWrapper.setLayout(new BoxLayout(ageWrapper, BoxLayout.Y_AXIS));
        ageWrapper.setBackground(Color.WHITE);
        ageWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

// 🔹 "나이" 라벨
        JLabel ageLabel = new JLabel("나이");
        ageLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        ageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        ageLabel.setPreferredSize(new Dimension(200, 20));

// 🔹 텍스트필드 + "세" 라벨
        JPanel ageFieldPanel = new JPanel();
        ageFieldPanel.setLayout(new BoxLayout(ageFieldPanel, BoxLayout.X_AXIS));
        ageFieldPanel.setBackground(Color.WHITE);
        ageFieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundTextField1 ageField = new RoundTextField1(30);
        ageField.setMaximumSize(new Dimension(200, 30));
        ageField.setHorizontalAlignment(JTextField.RIGHT);

        JLabel ageUnit = new JLabel("세");
        ageUnit.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        ageUnit.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

// 🔹 조립
        ageFieldPanel.add(ageField);
        ageFieldPanel.add(ageUnit);

        ageWrapper.add(ageLabel);
        ageWrapper.add(Box.createVerticalStrut(5));
        ageWrapper.add(ageFieldPanel);

// 🔹 전체에 추가
        add(Box.createVerticalStrut(30)); // 기존 체중 입력 필드와의 간격
        add(ageWrapper);



    }
}

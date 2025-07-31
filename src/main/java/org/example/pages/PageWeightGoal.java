package org.example.pages;

import org.example.component.RoundTextField1;

import javax.swing.*;
import java.awt.*;

public class PageWeightGoal extends JPanel {
    public PageWeightGoal() {
        setBackground(Color.WHITE);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // 🔷 제목
        JLabel label1 = new JLabel("목표 체중도 알려주시면");
        label1.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        label1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label2 = new JLabel("추천 계획을 짜볼게요");
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

        // 🔷 입력 필드 패널 (좌우 2개)
        JPanel gridPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setMaximumSize(new Dimension(400, 100));
        gridPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ✅ 공통 스타일
        Dimension labelSize = new Dimension(200, 20);
        Dimension fieldSize = new Dimension(200, 30);

        // 🔶 시작 체중 패널
        JPanel startWrapper = new JPanel();
        startWrapper.setLayout(new BoxLayout(startWrapper, BoxLayout.Y_AXIS));
        startWrapper.setBackground(Color.WHITE);

        JLabel startLabel = new JLabel("시작 체중");
        startLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        startLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        startLabel.setPreferredSize(labelSize);

        JPanel startFieldPanel = new JPanel();
        startFieldPanel.setLayout(new BoxLayout(startFieldPanel, BoxLayout.X_AXIS));
        startFieldPanel.setBackground(Color.WHITE);
        startFieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundTextField1 startField = new RoundTextField1(30);
        startField.setMaximumSize(fieldSize);
        startField.setHorizontalAlignment(JTextField.RIGHT);

        JLabel startKg = new JLabel("kg");
        startKg.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        startKg.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        startFieldPanel.add(startField);
        startFieldPanel.add(startKg);

        startWrapper.add(startLabel);
        startWrapper.add(Box.createVerticalStrut(5));
        startWrapper.add(startFieldPanel);

        // 🔶 목표 체중 패널
        JPanel goalWrapper = new JPanel();
        goalWrapper.setLayout(new BoxLayout(goalWrapper, BoxLayout.Y_AXIS));
        goalWrapper.setBackground(Color.WHITE);

        JLabel goalLabel = new JLabel("목표 체중");
        goalLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        goalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        goalLabel.setPreferredSize(labelSize);

        JPanel goalFieldPanel = new JPanel();
        goalFieldPanel.setLayout(new BoxLayout(goalFieldPanel, BoxLayout.X_AXIS));
        goalFieldPanel.setBackground(Color.WHITE);
        goalFieldPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        RoundTextField1 goalField = new RoundTextField1(30);
        goalField.setMaximumSize(fieldSize);
        goalField.setHorizontalAlignment(JTextField.RIGHT);

        JLabel goalKg = new JLabel("kg");
        goalKg.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        goalKg.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        goalFieldPanel.add(goalField);
        goalFieldPanel.add(goalKg);

        goalWrapper.add(goalLabel);
        goalWrapper.add(Box.createVerticalStrut(5));
        goalWrapper.add(goalFieldPanel);

        // 🔷 그리드에 추가
        gridPanel.add(startWrapper);
        gridPanel.add(goalWrapper);
        add(gridPanel);
    }
}

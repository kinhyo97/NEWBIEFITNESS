// SurveyPage.java
package org.example;

import org.example.component.RoundButton1;
import org.example.component.SurveyProgressBar;
import org.example.pages.PageWeightGoal;
import org.example.pages.PageMealFrequency;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SurveyPage extends JFrame {
    private JProgressBar progressBar;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private int totalPages;
    private int currentPage = 1;

    private final List<JPanel> surveyPages = List.of(
            new PageWeightGoal(),
            new PageMealFrequency()
            // 필요한 만큼 페이지 추가
    );

    public SurveyPage() {
        setTitle("SUGGEST FITNESS - SURVEY");
        setSize(500, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        totalPages = surveyPages.size(); // ✅ 먼저 설정

        // 상단 프로그레스 바
        progressBar = new SurveyProgressBar(currentPage, totalPages);
        progressBar.setPreferredSize(new Dimension(500, 5));
        JPanel progressWrapper = new JPanel(new BorderLayout());
        progressWrapper.setBackground(Color.WHITE);
        progressWrapper.setBorder(BorderFactory.createEmptyBorder(50, 150, 0, 150));
        progressWrapper.add(progressBar, BorderLayout.CENTER);
        add(progressWrapper, BorderLayout.NORTH);

        // 하단 버튼
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 50, 30));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton nextButton = new RoundButton1("다음", 50);
        nextButton.setPreferredSize(new Dimension(400, 40));
        nextButton.addActionListener(e -> nextPage());
        bottomPanel.add(nextButton);

        // 카드 레이아웃 구성
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(Color.WHITE);

        for (int i = 0; i < totalPages; i++) {
            cardPanel.add(surveyPages.get(i), String.valueOf(i + 1));
        }

        add(cardPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    public void nextPage() {
        if (currentPage < totalPages) {
            currentPage++;
            cardLayout.next(cardPanel);
            progressBar.setValue(currentPage);
        }
    }

    public static void main(String[] args) {
        new SurveyPage();
    }
}

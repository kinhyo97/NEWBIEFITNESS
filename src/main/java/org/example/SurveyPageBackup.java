// SurveyPage.java
package org.example;

import org.example.component.RoundButton1;
import org.example.component.SurveyProgressBar;
import org.example.pages.PageExerciseHistory;
import org.example.pages.PageMealFrequency;
import org.example.pages.PageWeightGoal;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SurveyPageBackup extends JFrame {
    private JProgressBar progressBar;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private int totalPages; // 전체 페이지 수 저장
    private int currentPage = 1; //현재 페이지 저장
    private JButton backButton;

    private final List<JPanel> surveyPages = List.of(
            new PageWeightGoal(),
            new PageMealFrequency(),
            new PageExerciseHistory()
            // 필요한 만큼 페이지 추가
    ); //list 객체로 surveyPages를 통해 페이지 수를 관리

    public SurveyPageBackup() {
        JButton floatingButton = new RoundButton1("←", 30);
        floatingButton.setBounds(10, 10, 50, 30);  // 원하는 위치에 띄우기
        floatingButton.addActionListener(e -> previousPage());

        //  오버레이 패널 만들기
        JLayeredPane overlay = getLayeredPane();  // JFrame 위에 떠 있는 레이어
        floatingButton.setVisible(true);
        overlay.add(floatingButton, JLayeredPane.PALETTE_LAYER); // 중간 위 레이어에 추가



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
       // if (currentPage < totalPages) {
           // currentPage++;
           // cardLayout.next(cardPanel);
           // progressBar.setValue(currentPage);

        if (currentPage < totalPages) {
            currentPage++;

            // ✅ 진짜 내 페이지 가져오기
            JPanel realPage = surveyPages.get(currentPage - 1);

            // ✅ FadePanel로 감싸기
            FadePanel fadePanel = new FadePanel(realPage);
            fadePanel.setBackground(Color.WHITE);

            // ✅ 카드에 등록
            cardPanel.add(fadePanel, String.valueOf(currentPage));
            cardLayout.show(cardPanel, String.valueOf(currentPage));
            fadePanel.startFadeIn();

            progressBar.setValue(currentPage);
        }

    }

    public void previousPage() {
        if (currentPage > 1) {
            currentPage--;
            cardLayout.previous(cardPanel);
            progressBar.setValue(currentPage);
        }
        if (currentPage == 1) {
        }
    }
    public static void main(String[] args) {
        new SurveyPageBackup();
    }
}

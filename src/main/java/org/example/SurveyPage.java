package org.example;

import org.example.component.RoundButton1;
import org.example.component.SurveyProgressBar;
import org.example.db.DatabaseUtil;
import org.example.pages.*;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class SurveyPage extends JFrame {
    private App app;
    private JProgressBar progressBar;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private int totalPages;
    private int currentPage = 1;
    private JButton nextButton;
    private JButton endButton;

    private final List<FadePanel> surveyPages = List.of(
            new FadePanel(new PageWeightGoal()),
            new FadePanel(new PageMealFrequency()),
            new FadePanel(new PageExerciseHistory()),
            new FadePanel(new PageExerciseInfo()),
            new FadePanel(new PageWorkoutDays()),
            new FadePanel(new PageFoodHabit()),
            new FadePanel(new PageTargetCalories()),
            new FadePanel(new PageLastPage())

    );

    public SurveyPage(Runnable onSurveyComplete) {
        // ← floating 버튼
        String currentUserId = App.user_id;
        System.out.println("설문 유저 ID: " + currentUserId);

        JButton floatingButton = new RoundButton1("←", 30);
        floatingButton.setBounds(10, 10, 50, 30);
        floatingButton.addActionListener(e -> previousPage());
        floatingButton.setVisible(true);

        // 레이어 위에 버튼 추가
        JLayeredPane overlay = getLayeredPane();
        overlay.add(floatingButton, JLayeredPane.PALETTE_LAYER);

        // 프레임 설정
        setTitle("SUGGEST FITNESS - SURVEY");
        setSize(500, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        totalPages = surveyPages.size();

        // 상단 프로그레스 바
        progressBar = new SurveyProgressBar(currentPage, totalPages);
        progressBar.setPreferredSize(new Dimension(500, 5));
        JPanel progressWrapper = new JPanel(new BorderLayout());
        progressWrapper.setBackground(Color.WHITE);
        progressWrapper.setBorder(BorderFactory.createEmptyBorder(50, 150, 0, 150));
        progressWrapper.add(progressBar, BorderLayout.CENTER);
        add(progressWrapper, BorderLayout.NORTH);

        // 하단 "다음" 버튼
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 50, 30));

        nextButton = new RoundButton1("다음", 50);
        nextButton.setPreferredSize(new Dimension(400, 40));
        nextButton.addActionListener(e -> nextPage());
        bottomPanel.add(nextButton);
        add(bottomPanel, BorderLayout.SOUTH);

        endButton = new RoundButton1("설문종료", 50);
        endButton.setPreferredSize(new Dimension(400, 40));
        endButton.addActionListener(e ->{
                dispose(); // 현재 SurveyPage 창 닫기
                saveGoalWeightOnly();
                updateSurveyCompleted();
                saveSurveyResult();
                new App().home_show();

        });
       bottomPanel.add(endButton);
       add(bottomPanel, BorderLayout.SOUTH);
        endButton.setVisible(false); // 🔥 처음에는 숨기기

        // 카드 레이아웃 패널
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(Color.WHITE);

        for (int i = 0; i < totalPages; i++) {
            cardPanel.add(surveyPages.get(i), String.valueOf(i + 1));
        }

        add(cardPanel, BorderLayout.CENTER);
        setVisible(true);

        // 첫 페이지 페이드 인 시작
        surveyPages.get(0).startFadeIn();
    }

    public void nextPage() {
        if (currentPage < totalPages) {
            currentPage++;
            FadePanel page = surveyPages.get(currentPage - 1);
            cardLayout.show(cardPanel, String.valueOf(currentPage));
            page.startFadeIn();
            progressBar.setValue(currentPage);

            if (currentPage == totalPages) {
                nextButton.setVisible(false);

                endButton.setVisible(true);

        }
    }}

    public void previousPage() {
        if (currentPage > 1) {
            currentPage--;
            FadePanel page = surveyPages.get(currentPage - 1);
            cardLayout.show(cardPanel, String.valueOf(currentPage));
            page.startFadeIn();
            progressBar.setValue(currentPage);
        }
    }

    private void saveSurveyResult() {
        String sql = "INSERT INTO survey_result (user_key, question_id, answer, created_at) VALUES (?, ?, ?, NOW())";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 예시로 하나만 넣는 경우 (반복문으로 여러 개 넣어도 됨)
            pstmt.setString(1, App.userKey);       // 사용자 키
            pstmt.setString(2, "Q001");            // 질문 ID
            pstmt.setString(3, "다이어트");        // 답변 내용

            pstmt.executeUpdate();
            System.out.println("✅ 설문 결과 저장 완료");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateSurveyCompleted() {
        String sql = "UPDATE user SET survey_completed = TRUE WHERE user_key = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, App.userKey);  // 현재 로그인한 사용자 키
            pstmt.executeUpdate();

            System.out.println("✅ survey_completed 값 TRUE로 업데이트됨");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveGoalWeightOnly() {
        String sql = "INSERT INTO user_survey (user_key, goal_weight) VALUES (?, ?) " +
                "ON DUPLICATE KEY UPDATE goal_weight = VALUES(goal_weight)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 🔍 surveyPages에서 PageWeightGoal 찾기
            PageWeightGoal goalPage = null;
            for (FadePanel panel : surveyPages) {
                Component page = panel.getComponent(0);
                if (page instanceof PageWeightGoal pwg) {
                    goalPage = pwg;
                    break;
                }
            }

            if (goalPage == null) {
                System.out.println("❗ PageWeightGoal 없음");
                return;
            }

            float weight = goalPage.getGoalWeight();
            if (weight <= 0) {
                JOptionPane.showMessageDialog(this, "목표 체중을 올바르게 입력하세요.");
                return;
            }

            pstmt.setString(1, App.userKey);
            pstmt.setFloat(2, weight);
            pstmt.executeUpdate();

            System.out.println("✅ 목표 체중 저장 완료: " + weight + "kg");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public static void main(String[] args) {
        new SurveyPage(() -> {
            // 설문 완료 후 실행할 동작
            System.out.println("설문 완료됨");
        });
    }
}

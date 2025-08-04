package org.example;

import org.example.dao.ExerciseDao;
import org.example.model.Exercise;
import org.example.detail.ExerciseDetailPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TipPanelController {

    public static void tip_show(JPanel panel) {
        panel.removeAll();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.DARK_GRAY);

        List<Exercise> exercises = ExerciseDao.getAllExercises();

        // 상단 바
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.BLACK);
        topBar.setPreferredSize(new Dimension(480, 40));

        JLabel title = new JLabel("  Newbie Health");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Malgun Gothic", Font.BOLD, 20));

        ImageIcon scaledIcon;
        try {
            ImageIcon icon = new ImageIcon("src/JS_icons/icons8-search-48.png");
            Image image = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            scaledIcon = new ImageIcon(image);
        } catch (Exception ex) {
            System.out.println("아이콘 로딩 실패: " + ex.getMessage());
            scaledIcon = new ImageIcon();
        }

        JButton searchToggleButton = new JButton(scaledIcon);
        searchToggleButton.setPreferredSize(new Dimension(36, 36));
        searchToggleButton.setFocusPainted(false);
        searchToggleButton.setContentAreaFilled(false);
        searchToggleButton.setBorderPainted(false);

        topBar.add(title, BorderLayout.WEST);
        topBar.add(searchToggleButton, BorderLayout.EAST);

        // 검색창
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.DARK_GRAY);
        JLabel label = new JLabel("운동 팁을 검색하세요:");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
        JTextField searchField = new JTextField(10);
        JButton searchButton = new JButton("검색");
        searchPanel.add(label);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        JPanel searchAreaPanel = new JPanel(new BorderLayout());
        searchAreaPanel.setBackground(Color.DARK_GRAY);
        searchAreaPanel.add(searchPanel, BorderLayout.CENTER);
        searchAreaPanel.setVisible(false);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(topBar, BorderLayout.NORTH);
        headerPanel.add(searchAreaPanel, BorderLayout.SOUTH);
        panel.add(headerPanel, BorderLayout.NORTH);

        // 결과 리스트 패널
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(Color.DARK_GRAY);

        JScrollPane scrollPane = new JScrollPane(resultPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(40);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 검색 버튼 동작
        searchButton.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            resultPanel.removeAll();

            if (!keyword.isEmpty()) {
                List<Exercise> tips = getTipsFromDB(keyword);
                if (tips.isEmpty()) {
                    JLabel noResult = new JLabel("검색 결과가 없습니다.");
                    noResult.setForeground(Color.WHITE);
                    resultPanel.add(noResult);
                } else {
                    for (Exercise tip : tips) {
                        JPanel card = createExerciseCard(tip, panel);
                        resultPanel.add(card);
                        resultPanel.add(Box.createVerticalStrut(10));
                    }
                }
            }

            resultPanel.revalidate();
            resultPanel.repaint();
        });

        // 초기 카드 출력
        resultPanel.removeAll();
        for (Exercise ex : exercises) {
            JPanel card = createExerciseCard(ex, panel);
            resultPanel.add(card);
            resultPanel.add(Box.createVerticalStrut(10));
        }

        // 검색창 토글
        searchToggleButton.addActionListener(e -> {
            searchAreaPanel.setVisible(!searchAreaPanel.isVisible());
            headerPanel.revalidate();
            headerPanel.repaint();
        });

        panel.revalidate();
        panel.repaint();
    }

    public static void tip_show(JPanel panel, Object ignoredApp) {
        tip_show(panel);
    }

    // 운동 카드 생성 (영상 링크 제거됨)
    private static JPanel createExerciseCard(Exercise exercise, JPanel parentPanel) {
        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(480, 80));
        card.setBackground(Color.LIGHT_GRAY);
        card.setLayout(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel(exercise.getExerciseName());
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 14));

        JLabel detailLabel = new JLabel("부위: " + exercise.getBodyPart() + ", 난이도: " + exercise.getDifficulty());
        detailLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 11));

        JLabel targetMuscleLabel = new JLabel("자극부위: " + exercise.getTargetMuscle());
        targetMuscleLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 10));

        JPanel textPanel = new JPanel(new GridLayout(3, 1));
        textPanel.setBackground(Color.LIGHT_GRAY);
        textPanel.add(titleLabel);
        textPanel.add(detailLabel);
        textPanel.add(targetMuscleLabel);

        card.add(textPanel, BorderLayout.CENTER);

        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Runnable backAction = () -> tip_show(parentPanel);
                ExerciseDetailPanel detailPanel = new ExerciseDetailPanel(exercise, backAction);

                parentPanel.removeAll();
                parentPanel.setLayout(new BorderLayout());
                parentPanel.add(detailPanel, BorderLayout.CENTER);
                parentPanel.revalidate();
                parentPanel.repaint();
            }
        });

        return card;
    }

    private static List<Exercise> getTipsFromDB(String keyword) {
        ExerciseDao dao = new ExerciseDao();
        return dao.searchByKeyword(keyword);
    }
}

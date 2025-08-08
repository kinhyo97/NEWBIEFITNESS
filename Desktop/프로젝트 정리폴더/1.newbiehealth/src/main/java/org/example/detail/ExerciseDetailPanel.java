package org.example.detail;

import org.example.model.Exercise;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


//GIF 파일을 불러오기 위한 Exercise CLASS
public class ExerciseDetailPanel extends JPanel {

    public ExerciseDetailPanel(Exercise exercise, Runnable onBackAction) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(600, 700));

        JLabel titleLabel = new JLabel(exercise.getExerciseName(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        titleLabel.setForeground(Color.WHITE);

        JPanel gifPanel = new JPanel(null);
        gifPanel.setBackground(Color.BLACK);
        gifPanel.setPreferredSize(new Dimension(600, 300));

        try {
            String name = exercise.getExerciseName().replaceAll("\\s+", "");

            // ✅ 예외 운동 처리용 Map
            Map<String, String> customGifMap = new HashMap<>();
            customGifMap.put("벤치프레스", "벤치프레스.gif");
            customGifMap.put("딥스머신", "딥스머신.gif");
            customGifMap.put("픽델머신", "픽델머신.gif");
            customGifMap.put("체스트프레스머신", "체스트프레스머신.gif");
            customGifMap.put("케이블크로스오버", "케이블크로스오버.gif");
            // 💡 필요한 만큼 추가하세요

            String gifFileName = customGifMap.getOrDefault(name, name + ".gif");
            String gifPath = "src/assets/gifs/" + gifFileName;

            File gifFile = new File(gifPath);
            if (gifFile.exists()) {
                ImageIcon gifIcon = new ImageIcon(gifPath);
                JLabel gifLabel = new JLabel(gifIcon);
                gifPanel.add(gifLabel);

                new Timer(100, e -> {
                    int panelWidth = gifPanel.getWidth();
                    int gifWidth = gifLabel.getIcon().getIconWidth();
                    int gifHeight = gifLabel.getIcon().getIconHeight();
                    int x = (panelWidth - gifWidth) / 2;
                    gifLabel.setBounds(x, 10, gifWidth, gifHeight);
                    gifPanel.revalidate();
                    gifPanel.repaint();
                    ((Timer) e.getSource()).stop();
                }).start();
            } else {
                JLabel errorLabel = new JLabel("GIF 파일이 준비되지 않았습니다.");
                errorLabel.setForeground(Color.GRAY);
                errorLabel.setFont(new Font("Malgun Gothic", Font.ITALIC, 14));
                errorLabel.setBounds(150, 100, 300, 30);
                gifPanel.add(errorLabel);
            }

        } catch (Exception e) {
            JLabel errorLabel = new JLabel("GIF를 불러오는 중 오류가 발생했습니다.");
            errorLabel.setForeground(Color.RED);
            errorLabel.setBounds(150, 100, 300, 30);
            gifPanel.add(errorLabel);
        }

        JTextArea description = new JTextArea();
        description.setEditable(false);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
        description.setText(
                "운동명: " + exercise.getExerciseName() + "\n" +
                        "부위: " + exercise.getBodyPart() + "\n" +
                        "자극 부위: " + exercise.getTargetMuscle() + "\n" +
                        "난이도: " + exercise.getDifficulty() + "\n"
                       // "영상 링크: " + exercise.getVideoUrl()
        );
        JScrollPane scrollPane = new JScrollPane(description);
        scrollPane.setPreferredSize(new Dimension(600, 120));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton backButton = new JButton("← 뒤로가기");
        backButton.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> onBackAction.run());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.add(backButton);

        add(titleLabel, BorderLayout.NORTH);
        add(gifPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.PAGE_END);
    }
}

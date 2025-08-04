package org.example.component;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;
import java.util.List;
import org.example.service.ExerciseService;

public class ExerciseSelectorPopup extends JFrame {
    public ExerciseSelectorPopup(RoutineRowPanel callerPanel) {
        setTitle("운동 선택");
        setSize(400, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        Map<String, List<String>> exerciseMap = ExerciseService.getExerciseMapByBodyPart();

        for (String bodyPart : exerciseMap.keySet()) {
            JButton partButton = new JButton(bodyPart);
            partButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            partButton.setMaximumSize(new Dimension(300, 40)); // ✅ 크기 고정
            partButton.setPreferredSize(new Dimension(300, 40)); // (옵션)
            partButton.addActionListener(e -> {
                showExerciseSelection(exerciseMap.get(bodyPart), callerPanel);
                dispose();
            });
            mainPanel.add(partButton);
            mainPanel.add(Box.createVerticalStrut(10));
        }

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void showExerciseSelection(List<String> exercises, RoutineRowPanel callerPanel) {
        JFrame subFrame = new JFrame("운동 선택");
        subFrame.setSize(600, 800);
        subFrame.setLocationRelativeTo(null);
        subFrame.setLayout(new GridLayout(0, 2));

        for (String ex : exercises) {
            JButton btn = new JButton(ex);
            btn.setPreferredSize(new Dimension(300, 45));      // ✅ 크기 설정
            btn.setMaximumSize(new Dimension(300, 45));
            btn.addActionListener(e -> {
                callerPanel.setExerciseName(ex);
                subFrame.dispose();
            });
            subFrame.add(btn);
        }

        subFrame.setVisible(true);
    }

    private Map<String, List<String>> fetchExerciseByBodyPart() {
        Map<String, List<String>> map = new LinkedHashMap<>();
        String query = "SELECT body_part, exercise_name FROM exercise ORDER BY body_part, exercise_name";
        String url = "jdbc:mariadb://localhost:3306/newbiehealth";
        String user = "root";
        String password = "1234";

        try (
                Connection conn = DriverManager.getConnection(url, user, password);
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                String part = rs.getString("body_part");
                String name = rs.getString("exercise_name");
                map.computeIfAbsent(part, k -> new ArrayList<>()).add(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }
}

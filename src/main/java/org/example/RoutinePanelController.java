// RoutinePanelController.java + routineRowPanel + ExerciseSelectorPopup
package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import org.example.component.*;

public class RoutinePanelController {
    private static JLabel dateLabel;

    public static void routine_show(JPanel panel, App app) {
        String url = "jdbc:mariadb://localhost:3306/newbiehealth";
        String user = "root";
        String password = "1234";
        String Date = LocalDate.now().toString();

        panel.removeAll();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK);

        SmallButton loadButton = new SmallButton("Load Routine");
        loadButton.setForeground(Color.WHITE);
        loadButton.setBackground(new Color(70, 70, 70));
        loadButton.setOpaque(true);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.BLACK);

        loadButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try (Connection conn = DriverManager.getConnection(url, user, password)) {
                    PreparedStatement stmt = conn.prepareStatement(
                            "SELECT routine_id, routine_name FROM workout_routine WHERE user_key = ?");
                    stmt.setString(1, App.userKey);
                    ResultSet rs = stmt.executeQuery();

                    List<String> routineNames = new ArrayList<>();
                    List<Integer> routineIds = new ArrayList<>();
                    while (rs.next()) {
                        routineIds.add(rs.getInt("routine_id"));
                        routineNames.add(rs.getString("routine_name"));
                    }

                    if (routineNames.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "저장된 루틴이 없습니다.");
                        return;
                    }

                    String selectedRoutine = (String) JOptionPane.showInputDialog(
                            null, "불러올 루틴을 선택하세요:", "루틴 선택", JOptionPane.PLAIN_MESSAGE,
                            null, routineNames.toArray(), routineNames.get(0));

                    if (selectedRoutine != null) {
                        int index = routineNames.indexOf(selectedRoutine);
                        int selectedRoutineId = routineIds.get(index);

                        PreparedStatement detailStmt = conn.prepareStatement(
                                "SELECT e.exercise_name, r.reps, r.weight, r.sets FROM routine_exercise_detail r " +
                                        "JOIN exercise e ON r.exercise_id = e.exercise_id WHERE r.routine_id = ?");
                        detailStmt.setInt(1, selectedRoutineId);
                        ResultSet detailRs = detailStmt.executeQuery();

                        listPanel.removeAll();
                        while (detailRs.next()) {
                            String name = detailRs.getString("exercise_name");
                            String reps = detailRs.getString("reps");
                            String weight = detailRs.getString("weight");
                            String sets = detailRs.getString("sets");

                            JPanel wrapper = new JPanel();
                            wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
                            wrapper.setOpaque(false);
                            wrapper.add(Box.createVerticalStrut(10));

                            RoutineRowPanel row = new RoutineRowPanel(listPanel, wrapper, name, reps, weight, sets);
                            wrapper.add(row);
                            listPanel.add(wrapper);
                        }
                        listPanel.revalidate();
                        listPanel.repaint();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "루틴 불러오기 중 오류 발생: " + ex.getMessage());
                }
            }
        });

        JPanel leftWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftWrap.setOpaque(false);
        leftWrap.add(loadButton);
        loadButton.setPreferredSize(new Dimension(120, 30));
        leftWrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, loadButton.getPreferredSize().height));
        panel.add(leftWrap);

        panel.add(Box.createVerticalStrut(30));
        org.example.UIUtils.addTitleLabel(panel, "TODAY'S ROUTINE", 30, Color.PINK);
        org.example.UIUtils.addTitleLabel(panel, "5 X 5 SUPER SET", 20, Color.WHITE);

        ActionPrettyButton2 prettyBtn = new ActionPrettyButton2("Date LOAD");
        prettyBtn.addActionListener(e -> {
            JFrame calendarFrame = new JFrame("캘린더 테스트");
            calendarFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            calendarFrame.setSize(400, 400);
            calendarFrame.setLayout(new BorderLayout());

            MonthCalendarPanel calendar = new MonthCalendarPanel((LocalDate selectedDate) -> {
                String selectedDateStr = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                System.out.println("✅ 선택된 날짜: " + selectedDateStr);
                dateLabel.setText(selectedDateStr);
                loadRoutineByDate(App.userKey, selectedDateStr, listPanel);
                calendarFrame.dispose();
            });

            calendarFrame.add(calendar, BorderLayout.CENTER);
            calendarFrame.setVisible(true);
        });

        panel.add(prettyBtn);
        dateLabel = new JLabel(Date, SwingConstants.CENTER);
        dateLabel.setForeground(Color.WHITE);
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(dateLabel);
        org.example.UIUtils.addTitleLabel(panel, "   EXERCISE     COUNT   WEIGHT      SET       ", 20, Color.WHITE);
        panel.add(listPanel);

        JButton addButton = new JButton("+ Add");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.addActionListener(e -> {
            JPanel wrapper = new JPanel();
            wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
            wrapper.setOpaque(false);
            wrapper.add(Box.createVerticalStrut(10));
            RoutineRowPanel row = new RoutineRowPanel(listPanel, wrapper);
            wrapper.add(row);
            listPanel.add(wrapper);
            listPanel.revalidate();
            listPanel.repaint();
        });
        panel.add(Box.createVerticalStrut(10));
        panel.add(addButton);
        panel.add(Box.createVerticalGlue());

        PrettyButton prettyButton = new PrettyButton("Exercise Done !");
        prettyButton.setBounds(30, 100, 200, 50);
        prettyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String selectedDate = dateLabel.getText();
                String userKey = App.userKey;
                Component[] rows = listPanel.getComponents();
                try (Connection conn = DriverManager.getConnection(url, user, password)) {
                    PreparedStatement deleteStmt = conn.prepareStatement(
                            "DELETE FROM workout_record WHERE user_key = ? AND date = ?");
                    deleteStmt.setString(1, userKey);
                    deleteStmt.setString(2, selectedDate);
                    deleteStmt.executeUpdate();
                    deleteStmt.close();

                    for (Component comp : rows) {
                        if (comp instanceof JPanel wrapper && wrapper.getComponentCount() >= 2) {
                            Component inner = wrapper.getComponent(1);
                            if (inner instanceof RoutineRowPanel rowPanel && rowPanel.isChecked()) {
                                String exName = rowPanel.getExerciseName();
                                int exId = getExerciseIdFromName(conn, exName);
                                if (exId == -1) {
                                    System.out.println("❌ 운동 ID 찾을 수 없음: " + exName);
                                    continue;
                                }
                                String reps = rowPanel.getReps();
                                String weight = rowPanel.getWeight();
                                String sets = rowPanel.getSets();

                                PreparedStatement insertStmt = conn.prepareStatement(
                                        "INSERT INTO workout_record (user_key, exercise_id, reps, weight, sets, date) " +
                                                "VALUES (?, ?, ?, ?, ?, ?)");
                                insertStmt.setString(1, userKey);
                                insertStmt.setInt(2, exId);
                                insertStmt.setString(3, reps);
                                insertStmt.setString(4, weight);
                                insertStmt.setString(5, sets);
                                insertStmt.setString(6, selectedDate);
                                insertStmt.executeUpdate();
                                insertStmt.close();
                            }
                        }
                    }
                    JOptionPane.showMessageDialog(null, "운동 기록이 저장되었습니다!");
                    loadRoutineByDate(userKey, selectedDate, listPanel);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "저장 중 오류: " + ex.getMessage());
                }
            }
        });

        panel.add(prettyButton);
        panel.add(Box.createVerticalStrut(30));
        panel.revalidate();
        panel.repaint();
    }

    private static int getExerciseIdFromName(Connection conn, String name) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT exercise_id FROM exercise WHERE TRIM(LOWER(exercise_name)) = TRIM(LOWER(?))")) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("exercise_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static void loadRoutineByDate(String userKey, String date, JPanel listPanel) {
        listPanel.removeAll();
        String url = "jdbc:mariadb://localhost:3306/newbiehealth";
        String user = "root";
        String password = "1234";
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT e.exercise_name, r.reps, r.weight, r.sets FROM workout_record r " +
                    "JOIN exercise e ON r.exercise_id = e.exercise_id WHERE r.user_key = ? AND r.date = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, userKey);
            stmt.setString(2, date);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("exercise_name");
                String reps = rs.getString("reps");
                String weight = rs.getString("weight");
                String sets = rs.getString("sets");

                JPanel wrapper = new JPanel();
                wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
                wrapper.setOpaque(false);
                wrapper.add(Box.createVerticalStrut(10));

                RoutineRowPanel row = new RoutineRowPanel(listPanel, wrapper, name, reps, weight, sets);
                wrapper.add(row);
                listPanel.add(wrapper);
            }
            listPanel.revalidate();
            listPanel.repaint();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "선택 날짜 기록 불러오기 오류: " + ex.getMessage());
        }
    }
}
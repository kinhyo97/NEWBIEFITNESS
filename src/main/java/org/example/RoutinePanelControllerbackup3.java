//package org.example;
//
//import org.example.component.ActionPrettyButton2;
//import org.example.component.MonthCalendarPanel;
//import org.example.component.PrettyButton;
//import org.example.component.SmallButton;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.sql.*;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class RoutinePanelControllerbackup3 {
//    private static JLabel dateLabel;
//    public static void routine_show(JPanel panel, App app) {
//        String url = "jdbc:mariadb://localhost:3306/newbiehealth";
//        String user = "root";
//        String password = "1234";
//        String Date = LocalDate.now().toString();
//        panel.removeAll();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//        panel.setBackground(Color.BLACK);
//
//        // 루틴 불러오기 버튼
//        SmallButton loadButton = new SmallButton("Load Routine");
//        JPanel listPanel = new JPanel();
//        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
//        listPanel.setBackground(Color.BLACK);
//
//        loadButton.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                try {
//                    Connection conn = DriverManager.getConnection(url, user, password);
//                    PreparedStatement stmt = conn.prepareStatement(
//                            "SELECT routine_id, routine_name FROM workout_routine WHERE user_key = ?"
//                    );
//                    stmt.setString(1, App.userKey);  // 로그인 시 저장된 userKey 사용
//                    ResultSet rs = stmt.executeQuery();
//                    List<String> routineNames = new ArrayList<>();
//                    List<Integer> routineIds = new ArrayList<>();
//
//                    while (rs.next()) {
//                        routineIds.add(rs.getInt("routine_id"));
//                        routineNames.add(rs.getString("routine_name"));
//                    }
//
//                    rs.close();
//                    stmt.close();
//
//                    if (routineNames.isEmpty()) {
//                        JOptionPane.showMessageDialog(null, "저장된 루틴이 없습니다.");
//                        return;
//                    }
//
//                    String selectedRoutine = (String) JOptionPane.showInputDialog(
//                            null,
//                            "불러올 루틴을 선택하세요:",
//                            "루틴 선택",
//                            JOptionPane.PLAIN_MESSAGE,
//                            null,
//                            routineNames.toArray(),
//                            routineNames.get(0)
//                    );
//
//                    if (selectedRoutine != null) {
//                        int index = routineNames.indexOf(selectedRoutine);
//                        int selectedRoutineId = routineIds.get(index);
//
//                        PreparedStatement detailStmt = conn.prepareStatement(
//                                "SELECT e.exercise_name, r.reps, r.weight, r.sets " +
//                                        "FROM routine_exercise_detail r " +
//                                        "JOIN exercise e ON r.exercise_id = e.exercise_id " +
//                                        "WHERE r.routine_id = ?"
//                        );
//                        detailStmt.setInt(1, selectedRoutineId);
//                        ResultSet detailRs = detailStmt.executeQuery();
//
//                        listPanel.removeAll();
//
//                        while (detailRs.next()) {
//                            String name = detailRs.getString("exercise_name");
//                            String reps = detailRs.getString("reps");
//                            String weight = detailRs.getString("weight");
//                            String sets = detailRs.getString("sets");
//
//                            JPanel wrapper = new JPanel();
//                            wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
//                            wrapper.setOpaque(false);
//                            wrapper.add(Box.createVerticalStrut(10));
//
//                            routineRowPanel row = new routineRowPanel(listPanel, wrapper, name, reps, weight, sets);
//                            wrapper.add(row);
//                            listPanel.add(wrapper);
//                        }
//
//                        listPanel.revalidate();
//                        listPanel.repaint();
//
//                        detailRs.close();
//                        detailStmt.close();
//                        conn.close();
//                    }
//
//                } catch (SQLException ex) {
//                    ex.printStackTrace();
//                    JOptionPane.showMessageDialog(null, "루틴 불러오기 중 오류 발생: " + ex.getMessage());
//                }
//            }
//        });
//
//        JPanel leftWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
//        leftWrap.setOpaque(false);
//        leftWrap.add(loadButton);
//        leftWrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, loadButton.getPreferredSize().height));
//        panel.add(leftWrap);
//
//        panel.add(Box.createVerticalStrut(30));
//        UIUtils.addTitleLabel(panel, "TODAY'S ROUTINE", 30, Color.PINK);
//        UIUtils.addTitleLabel(panel, "5 X 5 SUPER SET", 20, Color.WHITE);
//
//
//        ActionPrettyButton2 prettyBtn = new ActionPrettyButton2("Date LOAD");
//        prettyBtn.addActionListener(e -> {
//            JFrame calendarFrame = new JFrame("캘린더 테스트");
//            calendarFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//            calendarFrame.setSize(400, 400);
//            calendarFrame.setLayout(new BorderLayout());
//
//            MonthCalendarPanel calendar = new MonthCalendarPanel((LocalDate selectedDate) -> {
//                String selectedDateStr = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//                System.out.println("✅ 선택된 날짜: " + selectedDateStr);
//                dateLabel.setText(selectedDateStr);
//
//                // 👉 해당 날짜와 로그인 유저 기준으로 DB에서 운동 기록 조회
//                loadRoutineByDate(App.userKey, selectedDateStr, listPanel);
//
//                calendarFrame.dispose(); // 달력 닫기
//            });
//
//            calendarFrame.add(calendar, BorderLayout.CENTER);
//            calendarFrame.setVisible(true);
//        });
//
//        panel.add(prettyBtn);
//        dateLabel = new JLabel(Date, SwingConstants.CENTER);  // 초기값 오늘 날짜
//        dateLabel.setForeground(Color.WHITE);
//        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
//        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        panel.add(dateLabel);  // 실제 화면에 추가
//
//        UIUtils.addTitleLabel(panel, "   EXERCISE     COUNT   WEIGHT      SET       ", 20, Color.WHITE);
//
//        panel.add(listPanel);
//
//        JButton addButton = new JButton("+ Add");
//        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
//        addButton.addActionListener(e -> {
//            JPanel wrapper = new JPanel();
//            wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
//            wrapper.setOpaque(false);
//            wrapper.add(Box.createVerticalStrut(10));
//            routineRowPanel row = new routineRowPanel(listPanel, wrapper);
//            wrapper.add(row);
//            listPanel.add(wrapper);
//            listPanel.revalidate();
//            listPanel.repaint();
//        });
//
//        panel.add(Box.createVerticalStrut(10));
//        panel.add(addButton);
//        panel.add(Box.createVerticalGlue());
//
//        PrettyButton prettyButton = new PrettyButton("Exercise Done !");
//        prettyButton.setBounds(30, 100, 200, 50);
//        prettyButton.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                System.out.println("운동 완료 버튼 클릭됨!");
//                app.switchCard("ROUTINE");
//                app.routine_show();
//            }
//        });
//
//
//        //달력테스트
//
//
//        panel.add(prettyButton);
//        panel.add(Box.createVerticalStrut(30));
//        panel.revalidate();
//        panel.repaint();
//    }
//    private static void loadRoutineByDate(String userKey, String date, JPanel listPanel) {
//        listPanel.removeAll();
//
//        String url = "jdbc:mariadb://localhost:3306/newbiehealth";
//        String user = "root";
//        String password = "1234";
//
//        try {
//            Connection conn = DriverManager.getConnection(url, user, password);
//            String query = "SELECT e.exercise_name, r.reps, r.weight, r.sets " +
//                    "FROM workout_record r JOIN exercise e ON r.exercise_id = e.exercise_id " +
//                    "WHERE r.user_key = ? AND r.date = ?";
//            PreparedStatement stmt = conn.prepareStatement(query);
//            stmt.setString(1, userKey);
//            stmt.setString(2, date);
//            ResultSet rs = stmt.executeQuery();
//
//            while (rs.next()) {
//                String name = rs.getString("exercise_name");
//                String reps = rs.getString("reps");
//                String weight = rs.getString("weight");
//                String sets = rs.getString("sets");
//
//                JPanel wrapper = new JPanel();
//                wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
//                wrapper.setOpaque(false);
//                wrapper.add(Box.createVerticalStrut(10));
//
//                routineRowPanel row = new routineRowPanel(listPanel, wrapper, name, reps, weight, sets);
//                wrapper.add(row);
//                listPanel.add(wrapper);
//            }
//
//            rs.close();
//            stmt.close();
//            conn.close();
//
//            listPanel.revalidate();
//            listPanel.repaint();
//
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(null, "선택 날짜 기록 불러오기 오류: " + ex.getMessage());
//        }
//    }
//}
//

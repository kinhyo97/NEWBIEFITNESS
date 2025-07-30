package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.example.component.PrettyButton;
import org.example.component.SmallButton;

public class RoutinePanelController {
    public static void routine_show(JPanel panel, App app) {
        String url = "jdbc:mariadb://localhost:3306/newbiehealth";
        String user = "root";
        String password = "1234";

        panel.removeAll();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK);

        // 루틴 불러오기 버튼
        SmallButton loadButton = new SmallButton("Load Routine");
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.BLACK);

        loadButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Connection conn = DriverManager.getConnection(url, user, password);
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT routine_id, routine_name FROM workout_routine");

                    List<String> routineNames = new ArrayList<>();
                    List<Integer> routineIds = new ArrayList<>();

                    while (rs.next()) {
                        routineIds.add(rs.getInt("routine_id"));
                        routineNames.add(rs.getString("routine_name"));
                    }

                    rs.close();
                    stmt.close();

                    if (routineNames.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "저장된 루틴이 없습니다.");
                        return;
                    }

                    String selectedRoutine = (String) JOptionPane.showInputDialog(
                            null,
                            "불러올 루틴을 선택하세요:",
                            "루틴 선택",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            routineNames.toArray(),
                            routineNames.get(0)
                    );

                    if (selectedRoutine != null) {
                        int index = routineNames.indexOf(selectedRoutine);
                        int selectedRoutineId = routineIds.get(index);

                        PreparedStatement detailStmt = conn.prepareStatement(
                                "SELECT e.exercise_name, r.reps, r.weight, r.sets " +
                                        "FROM routine_exercise_detail r " +
                                        "JOIN exercise e ON r.exercise_id = e.exercise_id " +
                                        "WHERE r.routine_id = ?"
                        );
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

                            routineRowPanel row = new routineRowPanel(listPanel, wrapper, name, reps, weight, sets);
                            wrapper.add(row);
                            listPanel.add(wrapper);
                        }

                        listPanel.revalidate();
                        listPanel.repaint();

                        detailRs.close();
                        detailStmt.close();
                        conn.close();
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
        leftWrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, loadButton.getPreferredSize().height));
        panel.add(leftWrap);

        panel.add(Box.createVerticalStrut(30));
        org.example.UIUtils.addTitleLabel(panel, "TODAY'S ROUTINE", 30, Color.PINK);
        org.example.UIUtils.addTitleLabel(panel, "5 X 5 SUPER SET", 20, Color.WHITE);
        org.example.UIUtils.addTitleLabel(panel, "2025-07-29 (MON)", 15, Color.WHITE);
        org.example.UIUtils.addTitleLabel(panel, "   EXERCISE     COUNT   WEIGHT      SET       ", 20, Color.WHITE);

        panel.add(listPanel);

        JButton addButton = new JButton("+ Add");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.addActionListener(e -> {
            JPanel wrapper = new JPanel();
            wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
            wrapper.setOpaque(false);
            wrapper.add(Box.createVerticalStrut(10));
            routineRowPanel row = new routineRowPanel(listPanel, wrapper);
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
                System.out.println("운동 완료 버튼 클릭됨!");
                app.switchCard("ROUTINE");
                app.routine_show();
            }
        });

        panel.add(prettyButton);
        panel.add(Box.createVerticalStrut(30));
        panel.revalidate();
        panel.repaint();
    }
}

class routineRowPanel extends JPanel {
    public routineRowPanel(JPanel parentListPanel, JPanel wrapperToRemove) {
        this(parentListPanel, wrapperToRemove, "Exercise", "5", "50", "3");
    }

    public routineRowPanel(JPanel parentListPanel, JPanel wrapperToRemove, String name, String reps, String weight, String sets) {
        setLayout(new BorderLayout(10, 0));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        setBackground(Color.BLACK);

        JButton deleteButton = new JButton("X");
        deleteButton.setForeground(Color.RED);
        deleteButton.setPreferredSize(new Dimension(30, 30));
        deleteButton.setMargin(new Insets(0, 0, 0, 0));

        JPanel deleteWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        deleteWrapper.setBackground(Color.BLACK);
        deleteWrapper.add(Box.createHorizontalStrut(5));
        deleteWrapper.add(deleteButton);
        add(deleteWrapper, BorderLayout.WEST);

        JPanel infoPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        infoPanel.setBackground(Color.BLACK);

        JTextField exName = new JTextField(name);
        exName.setFont(new Font("", Font.PLAIN, 9));
        JTextField count = new JTextField(reps);
        count.setFont(new Font("Arial", Font.PLAIN, 9));
        JTextField weightField = new JTextField(weight + "kg");
        weightField.setFont(new Font("Arial", Font.PLAIN, 9));
        JTextField setsField = new JTextField(sets);
        setsField.setFont(new Font("Arial", Font.PLAIN, 9));

        for (JTextField field : new JTextField[]{exName, count, weightField, setsField}) {
            field.setHorizontalAlignment(JTextField.CENTER);
            infoPanel.add(field);
        }

        add(infoPanel, BorderLayout.CENTER);

        JCheckBox doneCheckBox = new JCheckBox();
        doneCheckBox.setBackground(Color.BLACK);
        add(doneCheckBox, BorderLayout.EAST);

        deleteButton.addActionListener(e -> {
            parentListPanel.remove(wrapperToRemove);
            parentListPanel.revalidate();
            parentListPanel.repaint();
        });
    }
}

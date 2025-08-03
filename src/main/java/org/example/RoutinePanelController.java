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
import org.example.service.RoutineService;
import org.example.model.RoutineDetail;
import org.example.model.WorkoutRecord;
import org.example.service.WorkoutRecordService;
import org.example.db.DatabaseUtil;


import org.example.component.*;

public class RoutinePanelController {
    private static JLabel dateLabel;

    public static void routine_show(JPanel panel, App app) {
        String Date = LocalDate.now().toString();

        panel.removeAll();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK);

        //운동 리스트를 추가하는 listpanel
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.BLACK);

        //loadRoutineButton의 위치
        addLoadRoutineButton(panel, listPanel); // 리팩토링된 메서드 호출
        panel.add(Box.createVerticalStrut(30));

        //타이틀 배치
        org.example.UIUtils.addTitleLabel(panel, "TODAY'S ROUTINE", 30, Color.PINK);
        org.example.UIUtils.addTitleLabel(panel, "5 X 5 SUPER SET", 20, Color.WHITE);

        //날짜선택 버튼
        addDateSelectorButton(panel, listPanel);

        //Date Label 배치
        dateLabel = new JLabel(Date, SwingConstants.CENTER);
        dateLabel.setForeground(Color.WHITE);
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(dateLabel);

        //listpanel 설명 Label
        org.example.UIUtils.addTitleLabel(panel, "   EXERCISE     COUNT   WEIGHT      SET       ", 20, Color.WHITE);

        //list패널 배치
        panel.add(listPanel);
        addAddRoutineButton(panel, listPanel);
        panel.add(Box.createVerticalGlue());

        //운동기록정보 저장 버튼
        addSaveRoutineButton(panel, listPanel);

        panel.revalidate();
        panel.repaint();
    }

    //  운동 이름으로 exercise_id 조회 (소문자/공백 무시)
    // DB 연결(Connection) 받아서 단일 ID 반환
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

    // 📅 특정 날짜에 해당하는 운동 기록 불러와 listPanel에 표시
    // DB에서 운동명, 반복수, 무게, 세트 수를 가져와 행 추가
    private static void loadRoutineByDate(String userKey, String date, JPanel listPanel) {
        listPanel.removeAll();
        try (Connection conn = DatabaseUtil.getConnection()) {
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

                addRoutineRow(listPanel, name, reps, weight, sets);
            }
            listPanel.revalidate();
            listPanel.repaint();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "선택 날짜 기록 불러오기 오류: " + ex.getMessage());
        }
    }

    // 📋 저장된 루틴 리스트 중 하나를 선택해 listPanel에 로딩
    // RoutineService에서 루틴 정보를 가져와 UI에 그려줌
    private static void addLoadRoutineButton(JPanel panel, JPanel listPanel) {
        SmallButton loadButton = new SmallButton("Load Routine");
        loadButton.setForeground(Color.WHITE);
        loadButton.setBackground(new Color(70, 70, 70));
        loadButton.setOpaque(true);

        loadButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try (Connection conn = DatabaseUtil.getConnection()) {
                    Map<Integer, String> routineMap = RoutineService.getRoutineListByUserKey(App.userKey);

                    if (routineMap.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "저장된 루틴이 없습니다.");
                        return;
                    }

                    List<String> routineNames = new ArrayList<>(routineMap.values());
                    String selectedRoutine = (String) JOptionPane.showInputDialog(
                            null, "불러올 루틴을 선택하세요:", "루틴 선택", JOptionPane.PLAIN_MESSAGE,
                            null, routineNames.toArray(), routineNames.get(0));

                    if (selectedRoutine != null) {
                        int selectedRoutineId = routineMap.entrySet().stream()
                                .filter(entry -> entry.getValue().equals(selectedRoutine))
                                .map(Map.Entry::getKey)
                                .findFirst()
                                .orElse(-1);

                        List<RoutineDetail> details = RoutineService.getRoutineDetails(selectedRoutineId);

                        listPanel.removeAll();
                        for (RoutineDetail detail : details) {
                            addRoutineRow(listPanel, detail.exerciseName, detail.reps, detail.weight, detail.sets);
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
    }

    // 📆 달력 팝업을 통해 날짜 선택 후 해당 날짜의 기록을 로딩
    // 선택한 날짜를 기준으로 WorkoutRecordService에서 불러옴
    private static void addDateSelectorButton(JPanel panel, JPanel listPanel) {
        ActionPrettyButton2 prettyBtn = new ActionPrettyButton2("Date LOAD");
        prettyBtn.addActionListener(e -> {
            // Calendar 생성
            JFrame calendarFrame = new JFrame("캘린더 테스트");
            calendarFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            calendarFrame.setSize(400, 400);
            calendarFrame.setLayout(new BorderLayout());

            MonthCalendarPanel calendar = new MonthCalendarPanel((LocalDate selectedDate) -> {
                String selectedDateStr = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                System.out.println("✅ 선택된 날짜: " + selectedDateStr);

                //UI에 날짜 표시
                dateLabel.setText(selectedDateStr);
                //해당 날짜에 저장된 운동 기록 불러오기
                List<WorkoutRecord> records = WorkoutRecordService.loadByDate(App.userKey, selectedDateStr);
                //불러온 운동 기록을 listpanel에 새로 표시
                refreshListPanel(listPanel, records);
                //달력팝업 종료
                calendarFrame.dispose();
            });



            //calendar 컴포넌트를 팝업 프레임에 추가
            calendarFrame.add(calendar, BorderLayout.CENTER);
            calendarFrame.setVisible(true);
        });

        panel.add(prettyBtn);
    }

    // 💾 체크된 운동만 저장 후, 저장 결과를 다시 UI에 갱신
    // 날짜와 유저키 기준으로 저장 및 리스트 갱신 처리
    private static void handleSaveWorkout(JPanel listPanel, JLabel dateLabel) {
        String selectedDate = dateLabel.getText();
        String userKey = App.userKey;
        List<WorkoutRecord> records = extractCheckedRecords(listPanel); // extractCheckedRecords에 listPanel데이터를 통해 추출

        WorkoutRecordService.saveRecords(userKey, selectedDate, records);
        JOptionPane.showMessageDialog(null, "운동 기록이 저장되었습니다!");

        List<WorkoutRecord> newList = WorkoutRecordService.loadByDate(userKey, selectedDate);
        refreshListPanel(listPanel, newList);

    }

    // ➕ 루틴 행 추가 버튼 (+ Add)을 눌렀을 때 실행
// 빈 RoutineRowPanel을 listPanel에 추가함
    private static void addAddRoutineButton(JPanel panel, JPanel listPanel) {
        //add버튼 생성
        JButton addButton = new JButton("+ Add");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        //add버튼 행동 지정
        addButton.addActionListener(e -> {
            JPanel wrapper = new JPanel(); // wrapper로 Add버튼 감쌀예정
            //wrapper레이아웃 지정
            wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
            wrapper.setOpaque(false);
            wrapper.add(Box.createVerticalStrut(10));

            //wrapper에 listpanel객체를 담아서 row 생성
            RoutineRowPanel row = new RoutineRowPanel(listPanel, wrapper);
            wrapper.add(row);
            listPanel.add(wrapper);
            listPanel.revalidate();
            listPanel.repaint();
        });

        panel.add(Box.createVerticalStrut(10));
        panel.add(addButton);
    }

    // ✅ 'Exercise Done!' 버튼 클릭 시 선택된 운동 기록 저장
    // 저장 후 다시 해당 날짜의 기록으로 listPanel 갱신
    private static void addSaveRoutineButton(JPanel panel, JPanel listPanel) {
        //운동 저장 버튼인 prettyButton 생성
        PrettyButton prettyButton = new PrettyButton("Exercise Done !");
        prettyButton.setBounds(30, 100, 200, 50);

        //prettybutton의 행동지정 addMouselistener도 actionlistener와 동일한 행동
        //mouselistener와 mouseadapter는 세트로 다님
        prettyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String selectedDate = dateLabel.getText();// datelabel로부터 날짜를 받아옴
                String userKey = App.userKey; // App에서 user정보를 받아옴

                // ✅ 체크된 운동 기록만 추출
                List<WorkoutRecord> records = extractCheckedRecords(listPanel); // 체크된 운동 체크하는 로직을 수행해서 getter를 통해 records list에 담는다
                //이부분에서 결국 DTO를 이용해서 WorkoutRecord라는 객체에 list형태로 담는것


                // 비즈니스db저장로직인 workoutrecordservice에 있는 saveRecords 함수를 사용해 userkey,selectedDate,records를 넘겨주고 저장시킴
                WorkoutRecordService.saveRecords(userKey, selectedDate, records);
                //사용자에게 정보를 알려주는 팝업 창을 띄우는 기능
                JOptionPane.showMessageDialog(null, "운동 기록이 저장되었습니다!");

                // 비즈니스로직인 workoutRecordService내의 loadbydate함수를 사용해 userkey와 선택된 날짜를 통해 데이터를 받아 dto인 workoutrecord객체에 list형태로 담음
                List<WorkoutRecord> newList = WorkoutRecordService.loadByDate(userKey, selectedDate);
                //listpanel을 최신화함
                refreshListPanel(listPanel, newList);
            }
        });


        panel.add(prettyButton);
        panel.add(Box.createVerticalStrut(30));
    }


    // 📄 주어진 운동 데이터를 기반으로 listPanel에 새 행 추가
    // 하나의 운동 정보를 UI 행(RoutineRowPanel)으로 표현
    private static void addRoutineRow(JPanel listPanel, String name, String reps, String weight, String sets) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setOpaque(false);
        wrapper.add(Box.createVerticalStrut(10));

        RoutineRowPanel row = new RoutineRowPanel(listPanel, wrapper, name, reps, weight, sets);
        wrapper.add(row);
        listPanel.add(wrapper);
    }


    // ♻️ 주어진 WorkoutRecord 리스트로 listPanel 완전히 갱신
    // 각 운동 정보를 반복해서 UI에 재구성
    private static void refreshListPanel(JPanel listPanel, List<WorkoutRecord> records) {
        listPanel.removeAll(); //리스트패널의 모든정보를 지우고
        for (WorkoutRecord rec : records) { //dto로 받은 WorkoutRecord를 통해 list에 담은 객체를 다 뽑을때까지 wrapper에 쌓아서 row를 추가함

            // Routin을 row에 담아서 그걸 wrapper로 싸서 추가시킬 부분
            JPanel wrapper = new JPanel();
            wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
            wrapper.setOpaque(false);
            wrapper.add(Box.createVerticalStrut(10));

            RoutineRowPanel row = new RoutineRowPanel(
                    listPanel, wrapper,
                    rec.exerciseName, rec.reps, rec.weight, rec.sets
            );

            wrapper.add(row);
            listPanel.add(wrapper);
        }


        listPanel.revalidate();
        listPanel.repaint();
    }

    // ✔️ 체크된 루틴 행들만 추출하여 WorkoutRecord 리스트로 반환
    // 저장 버튼 클릭 시 이 데이터만 저장 대상이 됨
    private static List<WorkoutRecord> extractCheckedRecords(JPanel listPanel) {
        List<WorkoutRecord> records = new ArrayList<>();
        for (Component comp : listPanel.getComponents()) {
            if (comp instanceof JPanel wrapper && wrapper.getComponentCount() >= 2) {
                Component inner = wrapper.getComponent(1);
                if (inner instanceof RoutineRowPanel rowPanel && rowPanel.isChecked()) {
                    records.add(new WorkoutRecord(
                            rowPanel.getExerciseName(),
                            rowPanel.getReps(),
                            rowPanel.getWeight(),
                            rowPanel.getSets()
                    )); // RoutineRowPanel에서 getter를 통해 순회하며 데이터를 조회
                }
            }
        }
        return records;
    }
}
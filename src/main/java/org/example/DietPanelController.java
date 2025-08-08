package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.lang.Exception;
import org.example.component.MonthCalendarPanel;




public class DietPanelController {
    static int currentBreakfastCalories = 0;
    static int currentLunchCalories = 0;
    static int currentDinnerCalories = 0;

    static int currentBreakfastfat = 0;
    static int currentLunchfat = 0;
    static int currentDinnerfat = 0;

    static int currentBreakfastprotein = 0;
    static int currentLunchprotein = 0;
    static int currentDinnerprotein = 0;

    static int currentBreakfastcarbohydrate = 0;
    static int currentLunchcarbohydrate = 0;
    static int currentDinnercarbohydrate = 0;

    private static void updateTotalCalories(JLabel totalLabel) {
        int total = currentBreakfastCalories + currentLunchCalories + currentDinnerCalories;
        totalLabel.setText("총 섭취 칼로리: " + total + " kcal");
    }
    private static void updateTotalfat(JLabel totalLabel3) {
        int total1 = currentBreakfastfat + currentLunchfat+ currentDinnerfat;
        totalLabel3.setText("총 섭취 지방: " + total1 + " g");
    }

    private static void updateTotalcarbohydrate(JLabel totalLabel1) {
        int total2 = currentBreakfastcarbohydrate + currentLunchcarbohydrate+ currentDinnercarbohydrate;
        totalLabel1.setText("총 섭취 탄수화물: " + total2 + " g");
    }


    private static void updateTotalprotein(JLabel totalLabel2) {
        int total3 = currentBreakfastprotein + currentLunchprotein+ currentDinnerprotein;
        totalLabel2.setText("총 섭취 단백질: " + total3 + " g");
    }







    private static JPanel wrapWithPanel(JButton button) {

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setBackground(Color.black); // 배경색 유지
        wrapper.add(button);

        return wrapper;
    }


    public static void diet_show(JPanel panel, App app){


        // 1. 날짜 라벨 먼저 만들자 (화면 상단 or 중간에 보여줄 라벨)
        JLabel dateLabel = new JLabel(LocalDate.now().toString(), SwingConstants.CENTER);
        dateLabel.setForeground(Color.WHITE);
        dateLabel.setFont(new Font("Pretendard", Font.PLAIN, 14));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(dateLabel);  // 화면 어디든 적당히 add







        // 2. 버튼 만들기 (날짜 선택용)

        ImageIcon icon6 = new ImageIcon("src/icons/calendar.png");
        Image scaledImage6 = icon6.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon6 = new ImageIcon(scaledImage6);
        JButton dateButton = new JButton(scaledIcon6);
        dateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        dateButton.setBackground(Color.black);
        dateButton.setOpaque(true);//배경 불투명
        dateButton.setContentAreaFilled(true);
        dateButton.setBorderPainted(false);
        dateButton.addActionListener(e -> {
            JFrame calendarFrame = new JFrame("달력 선택");
            calendarFrame.setSize(400, 400);
            calendarFrame.setLayout(new BorderLayout());

            MonthCalendarPanel calendarPanel = new MonthCalendarPanel((LocalDate selectedDate) -> {
                String selectedDateStr = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                dateLabel.setText(selectedDateStr);  // 선택한 날짜를 위에 표시

                // 👉 여기에 DB에서 선택한 날짜에 맞는 식단 불러오는 함수 호출 (loadDietByDate() 같은 거)
                // 예: loadDietByDate(App.userKey, selectedDateStr); ← 이건 네가 정의해야 함

                calendarFrame.dispose(); // 달력 닫기
            });

            calendarFrame.add(calendarPanel, BorderLayout.CENTER);
            calendarFrame.setVisible(true);
        });
        panel.add(dateButton);














        JPanel IT = new JPanel();
        IT.setLayout(new GridLayout(2, 2, 50, 30));
        IT.setBackground(Color.BLACK);


        //칼로리 표시용
        JLabel totalLabel = new JLabel("총 섭취 칼로리: 0 kcal");
        totalLabel.setForeground(Color.WHITE);
        totalLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);


        IT.add(totalLabel);
        //탄수 표시용
        JLabel totalLabel1 = new JLabel("총 섭취 탄수화물: 0 g");
        totalLabel1.setForeground(Color.WHITE);
        totalLabel1.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        totalLabel1.setHorizontalAlignment(SwingConstants.CENTER);

        IT.add(totalLabel1);

        //단백 표시용
        JLabel totalLabel2 = new JLabel("총 섭취 단백질: 0 g");
        totalLabel2.setForeground(Color.WHITE);
        totalLabel2.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        totalLabel2.setHorizontalAlignment(SwingConstants.CENTER);

        IT.add(totalLabel2);
        //지방 표시용
        JLabel totalLabel3 = new JLabel("총 섭취 지방: 0 g");
        totalLabel3.setForeground(Color.WHITE);
        totalLabel3.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        totalLabel3.setHorizontalAlignment(SwingConstants.CENTER);

        IT.add(totalLabel3);
        ImageIcon icon5 = new ImageIcon("src/icons/reset.png");
        Image scaledImage5 = icon5.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon5 = new ImageIcon(scaledImage5);
        JButton reset = new JButton(scaledIcon5);
        reset.setBackground(Color.black);
        reset.setOpaque(true);//배경 불투명
        reset.setContentAreaFilled(true);
        reset.setBorderPainted(false);
        reset.addActionListener(e ->{
            currentBreakfastCalories = 0;
            currentLunchCalories = 0;
            currentDinnerCalories = 0;

            currentBreakfastcarbohydrate = 0;
            currentLunchcarbohydrate = 0;
            currentDinnercarbohydrate = 0;

            currentBreakfastprotein = 0;
            currentLunchprotein = 0;
            currentDinnerprotein = 0;

            currentBreakfastfat = 0;
            currentLunchfat = 0;
            currentDinnerfat = 0;

            // 총합 라벨 업데이트
            updateTotalCalories(totalLabel);
            updateTotalcarbohydrate(totalLabel1);
            updateTotalprotein(totalLabel2);
            updateTotalfat(totalLabel3);
        });

        ImageIcon icon7 = new ImageIcon("src/icons/search.png");
        Image scaledImage7 = icon7.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon7 = new ImageIcon(scaledImage7);
        JButton search = new JButton(scaledIcon7);
        search.setBackground(Color.black);
        search.setOpaque(true);//배경 불투명
        search.setContentAreaFilled(true);
        search.setBorderPainted(false);
        search.addActionListener(e -> {
            JFrame calendarFrame = new JFrame("달력 선택");
            calendarFrame.setSize(400, 400);
            calendarFrame.setLayout(new BorderLayout());


            MonthCalendarPanel calendarPanel = new MonthCalendarPanel((LocalDate selectedDate) -> {
                String selectedDateStr = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                dateLabel.setText(selectedDateStr);
                calendarFrame.dispose(); // 달력 닫기

                try {
                    Class.forName("org.mariadb.jdbc.Driver");
                    Connection conn = DriverManager.getConnection(
                            "jdbc:mariadb://localhost:3306/newbiehealth", "root", "1234"
                    );

                    String query = "SELECT * FROM eaten_by_date WHERE date = ?";
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setDate(1, java.sql.Date.valueOf(selectedDate));
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        // 데이터가 있는 경우 → 새 창 띄워서 보여주기
                        String breakfast = rs.getString("breakfast");
                        String lunch = rs.getString("lunch");
                        String dinner = rs.getString("dinner");
                        String snack = rs.getString("snack");
                        int calories = rs.getInt("calories");

                        JFrame infoFrame = new JFrame("섭취 기록");
                        infoFrame.setSize(400, 300);
                        infoFrame.setLayout(new GridLayout(6, 1));

                        infoFrame.add(new JLabel(" 날짜: " + selectedDateStr));
                        infoFrame.add(new JLabel(" 아침: " + (breakfast != null ? breakfast : "없음")));
                        infoFrame.add(new JLabel(" 점심: " + (lunch != null ? lunch : "없음")));
                        infoFrame.add(new JLabel(" 저녁: " + (dinner != null ? dinner : "없음")));
                        infoFrame.add(new JLabel(" 간식: " + (snack != null ? snack : "없음")));
                        infoFrame.add(new JLabel(" 총 칼로리: " + calories + " kcal"));

                        infoFrame.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "해당 날짜에는 식단 정보가 없습니다.");
                    }

                    rs.close();
                    pstmt.close();
                    conn.close();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "DB 조회 중 오류: " + ex.getMessage());
                }
            });

            calendarFrame.add(calendarPanel, BorderLayout.CENTER);
            calendarFrame.setVisible(true);
        });








        panel.add(IT, BorderLayout.SOUTH);
        panel.setBorder(null);



        // 버튼들을 담을 패널 생성
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2, 3, 3));// 1행 3열, 좌우 간격 50, 상하 간격 30
        buttonPanel.setBackground(Color.BLACK);

// 버튼 크기 지정
        Dimension buttonSize = new Dimension(200, 200); // 너비 100, 높이 40


// 아침 버튼
        ImageIcon icon = new ImageIcon("src/icons/아침.png");

        Image scaledImage = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JButton btn = new JButton("아침",scaledIcon);
        btn.setBackground(Color.black);
        btn.setOpaque(true);//배경 불투명
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setForeground(Color.white);
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        buttonPanel.add(btn);

        btn.setPreferredSize(buttonSize);
        btn.setBorder(BorderFactory.createBevelBorder(1, new Color(80, 80, 80), new Color(10, 10, 10)));
        btn.setFocusPainted(false);
        btn.setSize(30,30);
        buttonPanel.add(wrapWithPanel(btn));


        btn.addActionListener(e -> {
            final JTextField inputField1 = new JTextField(15);
            final JTextField inputField2= new JTextField(15);
            final JTextField inputField3= new JTextField(15);
            final JTextField inputField4= new JTextField(15);
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBackground(Color.WHITE);  // 진한 블랙
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));  // 여백


            JFrame newFrame = new JFrame("아침 식단 입력");//창이름


            newFrame.setSize(350, 500);//크기설정
            newFrame.setLocationRelativeTo(null); // 가운데 정렬
            newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel JP = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JP.setBackground(Color.WHITE);

            JLabel lab = new JLabel("섭취한 음식 이름:");
            lab.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
            lab.setForeground(new Color(30, 30, 30));

            JTextField inputField = new JTextField(15); // 입력받는 곳
            List<String> foodList = new ArrayList<>();

            try {
                Class.forName("org.mariadb.jdbc.Driver");
                Connection conn = DriverManager.getConnection(
                        "jdbc:mariadb://localhost:3306/newbiehealth", "root", "1234"
                );
                String sql = "SELECT food_name FROM food"; // 🔧 테이블명과 컬럼명 수정
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    foodList.add(rs.getString("food_name")); // 🔧 정확한 컬럼명
                }

                rs.close();
                pstmt.close();
                conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            JPopupMenu suggestionMenu = new JPopupMenu();
            suggestionMenu.setFocusable(false);
            inputField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) { updateSuggestions(); }
                public void removeUpdate(DocumentEvent e) { updateSuggestions(); }
                public void insertUpdate(DocumentEvent e) { updateSuggestions(); }

                private void updateSuggestions() {
                    String text = inputField.getText().trim();
                    suggestionMenu.removeAll();

                    if (text.isEmpty()) {
                        suggestionMenu.setVisible(false);
                        return;
                    }

                    for (String food : foodList) {
                        if (food.toLowerCase().contains(text.toLowerCase())) { // 🔧 대소문자 무시
                            JMenuItem item = new JMenuItem(food);
                            item.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
                            item.addActionListener(ev -> {
                                inputField.setText(food);
                                suggestionMenu.setVisible(false);

                                try {
                                    Class.forName("org.mariadb.jdbc.Driver");
                                    Connection conn = DriverManager.getConnection(
                                            "jdbc:mariadb://localhost:3306/newbiehealth", "root", "1234"
                                    );
                                    String sql = "SELECT food_calories, food_carbohydrate, food_fat, food_protein FROM food WHERE food_name = ?";
                                    PreparedStatement pstmt = conn.prepareStatement(sql);
                                    pstmt.setString(1, food);
                                    ResultSet rs = pstmt.executeQuery();

                                    if (rs.next()) {
                                        inputField1.setText(String.valueOf(rs.getFloat("food_calories")));
                                        inputField2.setText(String.valueOf(rs.getFloat("food_carbohydrate")));
                                        inputField4.setText(String.valueOf(rs.getFloat("food_fat")));
                                        inputField3.setText(String.valueOf(rs.getFloat("food_protein")));
                                    }

                                    rs.close();
                                    pstmt.close();
                                    conn.close();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            });

                            suggestionMenu.add(item);
                        }
                    }

                    if (suggestionMenu.getComponentCount() > 0) {
                        suggestionMenu.show(inputField, 0, inputField.getHeight());
                    } else {
                        suggestionMenu.setVisible(false);
                    }
                }
            });

// 마지막에 컴포넌트 붙이기
            JP.add(lab);
            JP.add(inputField);





            JPanel JP1 = new JPanel (new FlowLayout(FlowLayout.LEFT));
            JP1.setBackground(Color.WHITE);
            JLabel lab1 = new JLabel("음식의 칼로리:");
            lab1.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
            lab1.setForeground(new Color(30, 30, 30));
            JP1.add(lab1);
            JP1.add(inputField1);


            JPanel JP2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JP2.setBackground(Color.WHITE);
            JLabel lab2 = new JLabel("탄수화물 함량:");
            lab2.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
            lab2.setForeground(new Color(30, 30, 30));
            JP2.add(lab2);
            JP2.add(inputField2);

            JPanel JP3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JP3.setBackground(Color.WHITE);
            JLabel lab3 = new JLabel("단백질 함량:");
            lab3.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
            lab3.setForeground(new Color(30, 30, 30));
            JP3.add(lab3);
            JP3.add(inputField3);

            JPanel JP4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JP4.setBackground(Color.WHITE);
            JLabel lab4 = new JLabel("지방 함량:");
            lab4.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
            lab4.setForeground(new Color(30, 30, 30));
            JP4.add(lab4);
            JP4.add(inputField4);

            mainPanel.add(JP);
            mainPanel.add(JP1);
            mainPanel.add(JP2);
            mainPanel.add(JP3);
            mainPanel.add(JP4);

            mainPanel.setVisible(true);





            JPanel JPJP = new JPanel();
            JButton saveButton = new JButton("저장");
            JPJP.add(saveButton);
            JPJP.setBackground(new Color(20, 20, 20));
            saveButton.setBackground(new Color(70, 130, 180));
            saveButton.setForeground(Color.WHITE);
            saveButton.addActionListener(ev -> {
                String foodName = inputField.getText();
                double kcal = 0, fat = 0, carbohydrate = 0, protein = 0;

                try {
                    kcal = Double.parseDouble(inputField1.getText());
                    currentBreakfastCalories += kcal;
                    updateTotalCalories(totalLabel);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame, "칼로리는 숫자로 입력하세요");
                }

                try {
                    fat = Double.parseDouble(inputField4.getText());
                    currentBreakfastfat += fat;
                    updateTotalfat(totalLabel3);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame, "섭취한 지방의 양은 숫자로 입력하세요");
                }

                try {
                    carbohydrate = Double.parseDouble(inputField2.getText());
                    currentBreakfastcarbohydrate += carbohydrate;
                    updateTotalcarbohydrate(totalLabel1);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame, "섭취한 탄수화물의 양은 숫자로 입력하세요");
                }

                try {
                    protein = Double.parseDouble(inputField3.getText());
                    currentBreakfastprotein += protein;
                    updateTotalprotein(totalLabel2);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame, "섭취한 단백질의 양은 숫자로 입력하세요");
                }

                // DB 저장
                try {
                    Class.forName("org.mariadb.jdbc.Driver");
                    Connection conn = DriverManager.getConnection(
                            "jdbc:mariadb://localhost:3306/newbiehealth", "root", "1234"
                    );

                    // 📅 dateLabel로부터 날짜 가져오기 → LocalDate → java.sql.Date
                    String dateText = dateLabel.getText();  // 예: "2025-08-04"
                    LocalDate localDate = LocalDate.parse(dateText);
                    Date sqlDate = Date.valueOf(localDate);

                    // 오늘 날짜 데이터 있는지 확인
                    String selectSql = "SELECT * FROM eaten_by_date WHERE date = ?";
                    PreparedStatement selectStmt = conn.prepareStatement(selectSql);
                    selectStmt.setDate(1, (java.sql.Date) sqlDate);
                    ResultSet rs = selectStmt.executeQuery();

                    if (rs.next()) {
                        //  있으면 UPDATE
                        String existingBreakfast = rs.getString("breakfast");
                        int existingCalories = rs.getInt("calories");

                        String updatedBreakfast = (existingBreakfast == null || existingBreakfast.isEmpty())
                                ? foodName
                                : existingBreakfast + ", " + foodName;

                        int updatedCalories = existingCalories + (int) kcal;

                        String updateSql = "UPDATE eaten_by_date SET breakfast = ?, calories = ? WHERE date = ?";
                        PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                        updateStmt.setString(1, updatedBreakfast);
                        updateStmt.setInt(2, updatedCalories);
                        updateStmt.setDate(3, (java.sql.Date) sqlDate);
                        updateStmt.executeUpdate();
                        updateStmt.close();
                    } else {
                        // ✅ 없으면 INSERT
                        String insertSql = "INSERT INTO eaten_by_date (date, breakfast, calories) VALUES (?, ?, ?)";
                        PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                        insertStmt.setDate(1, (java.sql.Date) sqlDate);
                        insertStmt.setString(2, foodName);
                        insertStmt.setInt(3, (int) kcal);
                        insertStmt.executeUpdate();
                        insertStmt.close();
                    }

                    rs.close();
                    selectStmt.close();
                    conn.close();
                    JOptionPane.showMessageDialog(newFrame, "식단 저장 완료!");

                } catch (Exception ed) {
                    ed.printStackTrace();
                    JOptionPane.showMessageDialog(newFrame, "DB 저장 중 오류: " + ed.getMessage());
                }
            });



            JPanel all = new JPanel();
            all.setLayout(new BoxLayout(all, BoxLayout.Y_AXIS));
            all.add(JP);
            all.add(JP1);
            all.add(JP2);
            all.add(JP3);
            all.add(JP4);
            all.add(saveButton);

            newFrame.add(all);
            newFrame.setVisible(true);
        });



// 점심
        ImageIcon icon2 = new ImageIcon("src/icons/점심.png");
        Image scaledImage2 = icon2.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon2 = new ImageIcon(scaledImage2);
        JButton btn2 = new JButton("점심",scaledIcon2);
        buttonPanel.add(btn2);
        btn2.setSize(30,30);
        btn2.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        btn2.setPreferredSize(buttonSize);
        btn2.setBackground(Color.black);
        btn2.setOpaque(true);//배경 불투명
        btn2.setContentAreaFilled(true);
        btn2.setBorderPainted(false);
        btn2.setForeground(Color.white);
        btn2.setFocusPainted(false);
        buttonPanel.add(wrapWithPanel(btn2));
        btn2.addActionListener(e -> {
            final JTextField inputField6 = new JTextField(15);
            final JTextField inputField7= new JTextField(15);
            final JTextField inputField9= new JTextField(15);
            final JTextField inputField8= new JTextField(15);
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBackground(Color.WHITE);  // 진한 블랙
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));  // 여백


            JFrame newFrame = new JFrame("간식 식단 입력");//창이름


            newFrame.setSize(350, 500);//크기설정
            newFrame.setLocationRelativeTo(null); // 가운데 정렬
            newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel JP5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JP5.setBackground(Color.WHITE);

            JLabel lab5= new JLabel("섭취한 음식 이름:");
            lab5.setFont(new Font("Malgun Gothic", Font.BOLD, 16));



            JTextField inputField5 = new JTextField(15); // 입력받는 곳
            List<String> foodList = new ArrayList<>();

            JP5.add(lab5);
            JP5.add(inputField5);

            try {
                Class.forName("org.mariadb.jdbc.Driver");
                Connection conn = DriverManager.getConnection(
                        "jdbc:mariadb://localhost:3306/newbiehealth", "root", "1234"
                );
                String sql = "SELECT food_name FROM food"; // 🔧 테이블명과 컬럼명 수정
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    foodList.add(rs.getString("food_name")); // 🔧 정확한 컬럼명
                }

                rs.close();
                pstmt.close();
                conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            JPopupMenu suggestionMenu = new JPopupMenu();
            suggestionMenu.setFocusable(false);
            inputField5.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) { updateSuggestions(); }
                public void removeUpdate(DocumentEvent e) { updateSuggestions(); }
                public void insertUpdate(DocumentEvent e) { updateSuggestions(); }

                private void updateSuggestions() {
                    String text1 = inputField5.getText().trim();
                    suggestionMenu.removeAll();

                    if (text1.isEmpty()) {
                        suggestionMenu.setVisible(false);
                        return;
                    }

                    for (String food : foodList) {
                        if (food.toLowerCase().contains(text1.toLowerCase())) { // 🔧 대소문자 무시
                            JMenuItem item = new JMenuItem(food);
                            item.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
                            item.addActionListener(ev -> {
                                inputField5.setText(food);
                                suggestionMenu.setVisible(false);

                                try {
                                    Class.forName("org.mariadb.jdbc.Driver");
                                    Connection conn = DriverManager.getConnection(
                                            "jdbc:mariadb://localhost:3306/newbiehealth", "root", "1234"
                                    );
                                    String sql = "SELECT food_calories, food_carbohydrate, food_fat, food_protein FROM food WHERE food_name = ?";
                                    PreparedStatement pstmt = conn.prepareStatement(sql);
                                    pstmt.setString(1, food);
                                    ResultSet rs = pstmt.executeQuery();

                                    if (rs.next()) {
                                        inputField6.setText(String.valueOf(rs.getFloat("food_calories")));
                                        inputField7.setText(String.valueOf(rs.getFloat("food_carbohydrate")));
                                        inputField9.setText(String.valueOf(rs.getFloat("food_fat")));
                                        inputField8.setText(String.valueOf(rs.getFloat("food_protein")));
                                    }

                                    rs.close();
                                    pstmt.close();
                                    conn.close();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            });

                            suggestionMenu.add(item);
                        }
                    }

                    if (suggestionMenu.getComponentCount() > 0) {
                        suggestionMenu.show(inputField5, 0, inputField5.getHeight());
                    } else {
                        suggestionMenu.setVisible(false);
                    }
                }
            });

            JPanel JP6 = new JPanel (new FlowLayout(FlowLayout.LEFT));
            JP6.setBackground(Color.WHITE);
            JLabel lab6 = new JLabel("음식의 칼로리:");
            lab6.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
            lab6.setBackground(Color.ORANGE);
            JP6.add(lab6);
            JP6.add(inputField6);


            JPanel JP7 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JP7.setBackground(Color.WHITE);
            JLabel lab7 = new JLabel("탄수화물 함량:");
            lab7.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
            lab7.setBackground(Color.ORANGE);
            JP7.add(lab7);
            JP7.add(inputField7);

            JPanel JP8 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JP8.setBackground(Color.WHITE);
            JLabel lab8 = new JLabel("단백질 함량:");
            lab8.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
            lab8.setBackground(Color.ORANGE);
            JP8.add(lab8);
            JP8.add(inputField8);

            JPanel JP9 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JP9.setBackground(Color.WHITE);
            JLabel lab9 = new JLabel("지방 함량:");
            lab9.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
            lab9.setBackground(Color.ORANGE);
            JP9.add(lab9);
            JP9.add(inputField9);






            JButton saveButton = new JButton("저장");
            saveButton.addActionListener(ev -> {
                String foodName = inputField5.getText();
                double kcal = 0, fat = 0, carbohydrate = 0, protein = 0;

                try {
                    kcal = Double.parseDouble(inputField6.getText());
                    currentBreakfastCalories += kcal;
                    updateTotalCalories(totalLabel);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame, "칼로리는 숫자로 입력하세요");
                }

                try {
                    fat = Double.parseDouble(inputField9.getText());
                    currentBreakfastfat += fat;
                    updateTotalfat(totalLabel3);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame, "섭취한 지방의 양은 숫자로 입력하세요");
                }

                try {
                    carbohydrate = Double.parseDouble(inputField7.getText());
                    currentBreakfastcarbohydrate += carbohydrate;
                    updateTotalcarbohydrate(totalLabel1);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame, "섭취한 탄수화물의 양은 숫자로 입력하세요");
                }

                try {
                    protein = Double.parseDouble(inputField8.getText());
                    currentBreakfastprotein += protein;
                    updateTotalprotein(totalLabel2);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame, "섭취한 단백질의 양은 숫자로 입력하세요");
                }

                // ✅ DB 저장
                try {
                    Class.forName("org.mariadb.jdbc.Driver");
                    Connection conn = DriverManager.getConnection(
                            "jdbc:mariadb://localhost:3306/newbiehealth", "root", "1234"
                    );

                    //  dateLabel로부터 날짜 가져오기 → LocalDate → java.sql.Date
                    String dateText = dateLabel.getText();  // 예: "2025-08-04"
                    LocalDate localDate = LocalDate.parse(dateText);
                    Date sqlDate = Date.valueOf(localDate);

                    // 오늘 날짜 데이터 있는지 확인
                    String selectSql = "SELECT * FROM eaten_by_date WHERE date = ?";
                    PreparedStatement selectStmt = conn.prepareStatement(selectSql);
                    selectStmt.setDate(1, (java.sql.Date) sqlDate);
                    ResultSet rs = selectStmt.executeQuery();

                    if (rs.next()) {
                        String existinglunch = rs.getString("lunch");
                        int existingCalories = rs.getInt("calories");

                        String updatedBreakfast = (existinglunch== null || existinglunch.isEmpty())
                                ? foodName
                                : existinglunch + ", " + foodName;

                        int updatedCalories = existingCalories + (int) kcal;

                        String updateSql = "UPDATE eaten_by_date SET lunch = ?, calories = ? WHERE date = ?";
                        PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                        updateStmt.setString(1, updatedBreakfast);
                        updateStmt.setInt(2, updatedCalories);
                        updateStmt.setDate(3, (java.sql.Date) sqlDate);
                        updateStmt.executeUpdate();
                        updateStmt.close();
                    } else {
                        String insertSql = "INSERT INTO eaten_by_date (date, lunch, calories) VALUES (?, ?, ?)";
                        PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                        insertStmt.setDate(1, (java.sql.Date) sqlDate);
                        insertStmt.setString(2, foodName);
                        insertStmt.setInt(3, (int) kcal);
                        insertStmt.executeUpdate();
                        insertStmt.close();
                    }

                    rs.close();
                    selectStmt.close();
                    conn.close();
                    JOptionPane.showMessageDialog(newFrame, "식단 저장 완료!");

                } catch (Exception ed) {
                    ed.printStackTrace();
                    JOptionPane.showMessageDialog(newFrame, "DB 저장 중 오류: " + ed.getMessage());
                }
            });

            JPanel all = new JPanel();
            all.setLayout(new BoxLayout(all, BoxLayout.Y_AXIS));
            all.add(JP5);
            all.add(JP6);
            all.add(JP7);
            all.add(JP8);
            all.add(JP9);
            all.add(saveButton);
            newFrame.add(all);
            newFrame.setVisible(true);
        });


// 저녁 버튼
        ImageIcon icon3 = new ImageIcon("src/icons/저녁.png");
        Image scaledImage3 = icon3.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon3 = new ImageIcon(scaledImage3);
        JButton btn3 = new JButton("저녁",scaledIcon3);
        buttonPanel.add(btn3);
        btn3.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        btn3.setPreferredSize(buttonSize);
        btn3.setBackground(Color.black);
        btn3.setOpaque(true);//배경 불투명
        btn3.setContentAreaFilled(true);
        btn3.setBorderPainted(false);
        btn3.setForeground(Color.white);
        btn3.setFocusPainted(false);


        btn3.setBorder(BorderFactory.createBevelBorder(1, new Color(80, 80, 80), new Color(10, 10, 10)));
        btn3.setFocusPainted(false);
        buttonPanel.add(wrapWithPanel(btn3));



        btn3.addActionListener(e -> {
            final JTextField inputField11 = new JTextField(15);
            final JTextField inputField12= new JTextField(15);
            final JTextField inputField13= new JTextField(15);
            final JTextField inputField14= new JTextField(15);
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBackground(Color.WHITE);  // 진한 블랙
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));  // 여백


            JFrame newFrame3 = new JFrame("저녁 식단 입력");//창이름


            newFrame3.setSize(350, 500);//크기설정
            newFrame3.setLocationRelativeTo(null); // 가운데 정렬
            newFrame3.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel JP10 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JP10.setBackground(Color.WHITE);

            JLabel lab10= new JLabel("섭취한 음식 이름:");
            lab10.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
            lab10.setForeground(new Color(30, 30, 30));

            JTextField inputField10 = new JTextField(15); // 입력받는 곳
            List<String> foodList = new ArrayList<>();
            JP10.add(lab10);
            JP10.add(inputField10);

            try {
                Class.forName("org.mariadb.jdbc.Driver");
                Connection conn = DriverManager.getConnection(
                        "jdbc:mariadb://localhost:3306/newbiehealth", "root", "1234"
                );
                String sql = "SELECT food_name FROM food"; // 🔧 테이블명과 컬럼명 수정
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    foodList.add(rs.getString("food_name")); // 🔧 정확한 컬럼명
                }

                rs.close();
                pstmt.close();
                conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            JPopupMenu suggestionMenu = new JPopupMenu();
            suggestionMenu.setFocusable(false);
            inputField10.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) { updateSuggestions(); }
                public void removeUpdate(DocumentEvent e) { updateSuggestions(); }
                public void insertUpdate(DocumentEvent e) { updateSuggestions(); }

                private void updateSuggestions() {
                    String text2 = inputField10.getText().trim();
                    suggestionMenu.removeAll();

                    if (text2.isEmpty()) {
                        suggestionMenu.setVisible(false);
                        return;
                    }

                    for (String food : foodList) {
                        if (food.toLowerCase().contains(text2.toLowerCase())) { // 🔧 대소문자 무시
                            JMenuItem item = new JMenuItem(food);
                            item.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
                            item.addActionListener(ev -> {
                                inputField10.setText(food);
                                suggestionMenu.setVisible(false);

                                try {
                                    Class.forName("org.mariadb.jdbc.Driver");
                                    Connection conn = DriverManager.getConnection(
                                            "jdbc:mariadb://localhost:3306/newbiehealth", "root", "1234"
                                    );
                                    String sql = "SELECT food_calories, food_carbohydrate, food_fat, food_protein FROM food WHERE food_name = ?";
                                    PreparedStatement pstmt = conn.prepareStatement(sql);
                                    pstmt.setString(1, food);
                                    ResultSet rs = pstmt.executeQuery();

                                    if (rs.next()) {
                                        inputField11.setText(String.valueOf(rs.getFloat("food_calories")));
                                        inputField12.setText(String.valueOf(rs.getFloat("food_carbohydrate")));
                                        inputField14.setText(String.valueOf(rs.getFloat("food_fat")));
                                        inputField13.setText(String.valueOf(rs.getFloat("food_protein")));
                                    }

                                    rs.close();
                                    pstmt.close();
                                    conn.close();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            });

                            suggestionMenu.add(item);
                        }
                    }

                    if (suggestionMenu.getComponentCount() > 0) {
                        suggestionMenu.show(inputField10, 0, inputField10.getHeight());
                    } else {
                        suggestionMenu.setVisible(false);
                    }
                }
            });


            JPanel JP11 = new JPanel (new FlowLayout(FlowLayout.LEFT));
            JLabel lab11 = new JLabel("음식의 칼로리:");
            JP11.setBackground(Color.WHITE);
            lab11.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
            lab11.setForeground(new Color(30, 30, 30));
            JP11.add(lab11);
            JP11.add(inputField11);


            JPanel JP12 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel lab12 = new JLabel("탄수화물 함량:");
            JP12.setBackground(Color.WHITE);
            lab12.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
            lab12.setForeground(new Color(30, 30, 30));
            JP12.add(lab12);
            JP12.add(inputField12);

            JPanel JP13 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel lab13 = new JLabel("단백질 함량:");
            JP13.setBackground(Color.WHITE);
            lab13.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
            lab13.setForeground(new Color(30, 30, 30));
            JP13.add(lab13);
            JP13.add(inputField13);

            JPanel JP14 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel lab14 = new JLabel("지방 함량:");
            JP14.setBackground(Color.WHITE);
            lab14.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
            lab14.setForeground(new Color(30, 30, 30));
            JP14.add(lab14);
            JP14.add(inputField14);






            JButton saveButton = new JButton("저장");
            saveButton.addActionListener(ev -> {
                String foodName = inputField10.getText();
                double kcal = 0, fat = 0, carbohydrate = 0, protein = 0;

                try {
                    kcal = Double.parseDouble(inputField11.getText());
                    currentBreakfastCalories += kcal;
                    updateTotalCalories(totalLabel);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame3, "칼로리는 숫자로 입력하세요");
                }

                try {
                    fat = Double.parseDouble(inputField14.getText());
                    currentBreakfastfat += fat;
                    updateTotalfat(totalLabel3);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame3, "섭취한 지방의 양은 숫자로 입력하세요");
                }

                try {
                    carbohydrate = Double.parseDouble(inputField12.getText());
                    currentBreakfastcarbohydrate += carbohydrate;
                    updateTotalcarbohydrate(totalLabel1);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame3, "섭취한 탄수화물의 양은 숫자로 입력하세요");
                }

                try {
                    protein = Double.parseDouble(inputField13.getText());
                    currentBreakfastprotein += protein;
                    updateTotalprotein(totalLabel2);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame3, "섭취한 단백질의 양은 숫자로 입력하세요");
                }

                // ✅ DB 저장
                try {
                    Class.forName("org.mariadb.jdbc.Driver");
                    Connection conn = DriverManager.getConnection(
                            "jdbc:mariadb://localhost:3306/newbiehealth", "root", "1234"
                    );

                    // 📅 dateLabel로부터 날짜 가져오기 → LocalDate → java.sql.Date
                    String dateText = dateLabel.getText();  // 예: "2025-08-04"
                    LocalDate localDate = LocalDate.parse(dateText);
                    Date sqlDate = Date.valueOf(localDate);

                    // 오늘 날짜 데이터 있는지 확인
                    String selectSql = "SELECT * FROM eaten_by_date WHERE date = ?";
                    PreparedStatement selectStmt = conn.prepareStatement(selectSql);
                    selectStmt.setDate(1, (java.sql.Date) sqlDate);
                    ResultSet rs = selectStmt.executeQuery();

                    if (rs.next()) {
                        // ✅ 있으면 UPDATE
                        String existingdinner = rs.getString("dinner");
                        int existingCalories = rs.getInt("calories");

                        String updatedBreakfast = (existingdinner == null || existingdinner.isEmpty())
                                ? foodName
                                : existingdinner + ", " + foodName;

                        int updatedCalories = existingCalories + (int) kcal;

                        String updateSql = "UPDATE eaten_by_date SET dinner = ?, calories = ? WHERE date = ?";
                        PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                        updateStmt.setString(1, updatedBreakfast);
                        updateStmt.setInt(2, updatedCalories);
                        updateStmt.setDate(3, (java.sql.Date) sqlDate);
                        updateStmt.executeUpdate();
                        updateStmt.close();
                    } else {
                        // ✅ 없으면 INSERT
                        String insertSql = "INSERT INTO eaten_by_date (date, dinner, calories) VALUES (?, ?, ?)";
                        PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                        insertStmt.setDate(1, (java.sql.Date) sqlDate);
                        insertStmt.setString(2, foodName);
                        insertStmt.setInt(3, (int) kcal);
                        insertStmt.executeUpdate();
                        insertStmt.close();
                    }

                    rs.close();
                    selectStmt.close();
                    conn.close();
                    JOptionPane.showMessageDialog(newFrame3, "식단 저장 완료!");

                } catch (Exception ed) {
                    ed.printStackTrace();
                    JOptionPane.showMessageDialog(newFrame3, "DB 저장 중 오류: " + ed.getMessage());
                }
            });

            JPanel all = new JPanel();
            all.setLayout(new BoxLayout(all, BoxLayout.Y_AXIS));
            all.add(JP10);
            all.add(JP11);
            all.add(JP12);
            all.add(JP13);
            all.add(JP14);
            all.add(saveButton);
            newFrame3.add(all);
            newFrame3.setVisible(true);
        });


        //간식 버튼
        ImageIcon icon4 = new ImageIcon("src/icons/간식.png");
        Image scaledImage4 = icon4.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon4 = new ImageIcon(scaledImage4);
        JButton btn4 = new JButton("간식",scaledIcon4);
        buttonPanel.add(btn4);

        btn4.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        btn4.setPreferredSize(buttonSize);
        btn4.setBackground(Color.black);
        btn4.setOpaque(true);//배경 불투명
        btn4.setContentAreaFilled(true);
        btn4.setBorderPainted(false);
        btn4.setForeground(Color.white);
        btn4.setFocusPainted(false);
        buttonPanel.add(wrapWithPanel(btn4));

        btn4.addActionListener(e -> {
            final JTextField inputField16 = new JTextField(15);
            final JTextField inputField17= new JTextField(15);
            final JTextField inputField18= new JTextField(15);
            final JTextField inputField19= new JTextField(15);
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBackground(Color.WHITE);  // 진한 블랙
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));  // 여백


            JFrame newFrame5 = new JFrame("간식 식단 입력");//창이름


            newFrame5.setSize(350, 500);//크기설정
            newFrame5.setLocationRelativeTo(null); // 가운데 정렬
            newFrame5.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel JP15 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JP15.setBackground(Color.WHITE);

            JLabel lab15= new JLabel("섭취한 음식 이름:");
            lab15.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
            lab15.setForeground(new Color(30, 30, 30));


            JTextField inputField15 = new JTextField(15); // 입력받는 곳
            List<String> foodList = new ArrayList<>();
            JP15.add(lab15);
            JP15.add(inputField15);

            try {
                Class.forName("org.mariadb.jdbc.Driver");
                Connection conn = DriverManager.getConnection(
                        "jdbc:mariadb://localhost:3306/newbiehealth", "root", "1234"
                );
                String sql = "SELECT food_name FROM food"; // 🔧 테이블명과 컬럼명 수정
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    foodList.add(rs.getString("food_name")); // 🔧 정확한 컬럼명
                }

                rs.close();
                pstmt.close();
                conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            JPopupMenu suggestionMenu = new JPopupMenu();
            suggestionMenu.setFocusable(false);
            inputField15.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) { updateSuggestions(); }
                public void removeUpdate(DocumentEvent e) { updateSuggestions(); }
                public void insertUpdate(DocumentEvent e) { updateSuggestions(); }

                private void updateSuggestions() {
                    String text3 = inputField15.getText().trim();
                    suggestionMenu.removeAll();

                    if (text3.isEmpty()) {
                        suggestionMenu.setVisible(false);
                        return;
                    }

                    for (String food : foodList) {
                        if (food.toLowerCase().contains(text3.toLowerCase())) { // 🔧 대소문자 무시
                            JMenuItem item = new JMenuItem(food);
                            item.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
                            item.addActionListener(ev -> {
                                inputField15.setText(food);
                                suggestionMenu.setVisible(false);

                                try {
                                    Class.forName("org.mariadb.jdbc.Driver");
                                    Connection conn = DriverManager.getConnection(
                                            "jdbc:mariadb://localhost:3306/newbiehealth", "root", "1234"
                                    );
                                    String sql = "SELECT food_calories, food_carbohydrate, food_fat, food_protein FROM food WHERE food_name = ?";
                                    PreparedStatement pstmt = conn.prepareStatement(sql);
                                    pstmt.setString(1, food);
                                    ResultSet rs = pstmt.executeQuery();

                                    if (rs.next()) {
                                        inputField16.setText(String.valueOf(rs.getFloat("food_calories")));
                                        inputField17.setText(String.valueOf(rs.getFloat("food_carbohydrate")));
                                        inputField19.setText(String.valueOf(rs.getFloat("food_fat")));
                                        inputField18.setText(String.valueOf(rs.getFloat("food_protein")));
                                    }

                                    rs.close();
                                    pstmt.close();
                                    conn.close();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            });

                            suggestionMenu.add(item);
                        }
                    }

                    if (suggestionMenu.getComponentCount() > 0) {
                        suggestionMenu.show(inputField15, 0, inputField15.getHeight());
                    } else {
                        suggestionMenu.setVisible(false);
                    }
                }
            });





            JPanel JP16 = new JPanel (new FlowLayout(FlowLayout.LEFT));
            JP16.setBackground(Color.WHITE);
            JLabel lab16 = new JLabel("음식의 칼로리:");
            lab16.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
            lab16.setForeground(new Color(30, 30, 30));
            JP16.add(lab16);
            JP16.add(inputField16);


            JPanel JP17 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JP17.setBackground(Color.WHITE);
            JLabel lab17 = new JLabel("탄수화물 함량:");
            lab17.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
            lab17.setForeground(new Color(30, 30, 30));
            JP17.add(lab17);
            JP17.add(inputField17);

            JPanel JP18 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JP18.setBackground(Color.WHITE);
            JLabel lab18 = new JLabel("단백질 함량:");
            lab18.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
            lab18.setForeground(new Color(30, 30, 30));
            JP18.add(lab18);
            JP18.add(inputField18);

            JPanel JP19 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JP19.setBackground(Color.WHITE);
            JLabel lab19 = new JLabel("지방 함량:");
            lab19.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
            lab19.setForeground(new Color(30, 30, 30));
            JP19.add(lab19);
            JP19.add(inputField19);

            mainPanel.add(JP15);
            mainPanel.add(JP16);
            mainPanel.add(JP17);
            mainPanel.add(JP18);
            mainPanel.add(JP19);

            mainPanel.setVisible(true);





            JPanel JPJP1 = new JPanel();
            JButton saveButton = new JButton("저장");
            JPJP1.add(saveButton);
            JPJP1.setBackground(new Color(20, 20, 20));
            saveButton.setBackground(new Color(70, 130, 180));
            saveButton.setForeground(Color.WHITE);
            saveButton.addActionListener(ev -> {
                String foodName = inputField15.getText();
                double kcal = 0, fat = 0, carbohydrate = 0, protein = 0;

                try {
                    kcal = Double.parseDouble(inputField16.getText());
                    currentBreakfastCalories += kcal;
                    updateTotalCalories(totalLabel);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame5, "칼로리는 숫자로 입력하세요");
                }

                try {
                    fat = Double.parseDouble(inputField19.getText());
                    currentBreakfastfat += fat;
                    updateTotalfat(totalLabel3);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame5, "섭취한 지방의 양은 숫자로 입력하세요");
                }

                try {
                    carbohydrate = Double.parseDouble(inputField17.getText());
                    currentBreakfastcarbohydrate += carbohydrate;
                    updateTotalcarbohydrate(totalLabel1);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame5, "섭취한 탄수화물의 양은 숫자로 입력하세요");
                }

                try {
                    protein = Double.parseDouble(inputField18.getText());
                    currentBreakfastprotein += protein;
                    updateTotalprotein(totalLabel2);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame5, "섭취한 단백질의 양은 숫자로 입력하세요");
                }

                // DB 저장
                try {
                    Class.forName("org.mariadb.jdbc.Driver");
                    Connection conn = DriverManager.getConnection(
                            "jdbc:mariadb://localhost:3306/newbiehealth", "root", "1234"
                    );

                    //   dateLabel로부터 날짜 가져오기 → LocalDate → java.sql.Date
                    String dateText = dateLabel.getText();  // 예: "2025-08-04"
                    LocalDate localDate = LocalDate.parse(dateText);
                    Date sqlDate = Date.valueOf(localDate);

                    // 오늘 날짜 데이터 있는지 확인
                    String selectSql = "SELECT * FROM eaten_by_date WHERE date = ?";
                    PreparedStatement selectStmt = conn.prepareStatement(selectSql);
                    selectStmt.setDate(1, (java.sql.Date) sqlDate);
                    ResultSet rs = selectStmt.executeQuery();

                    if (rs.next()) {
                        //  있으면 UPDATE
                        String existingsnack = rs.getString("snack");
                        int existingCalories = rs.getInt("calories");

                        String updatedsnack = (existingsnack == null || existingsnack.isEmpty())
                                ? foodName
                                : existingsnack + ", " + foodName;

                        int updatedCalories = existingCalories + (int) kcal;

                        String updateSql = "UPDATE eaten_by_date SET snack = ?, calories = ? WHERE date = ?";
                        PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                        updateStmt.setString(1, updatedsnack);
                        updateStmt.setInt(2, updatedCalories);
                        updateStmt.setDate(3, (java.sql.Date) sqlDate);
                        updateStmt.executeUpdate();
                        updateStmt.close();
                    } else {
                        //  없으면 INSERT
                        String insertSql = "INSERT INTO eaten_by_date (date, snack, calories) VALUES (?, ?, ?)";
                        PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                        insertStmt.setDate(1, (java.sql.Date) sqlDate);
                        insertStmt.setString(2, foodName);
                        insertStmt.setInt(3, (int) kcal);
                        insertStmt.executeUpdate();
                        insertStmt.close();
                    }

                    rs.close();
                    selectStmt.close();
                    conn.close();
                    JOptionPane.showMessageDialog(newFrame5, "식단 저장 완료!");

                } catch (Exception ed) {
                    ed.printStackTrace();
                    JOptionPane.showMessageDialog(newFrame5, "DB 저장 중 오류: " + ed.getMessage());
                }
            });

            JPanel all = new JPanel();
            all.setLayout(new BoxLayout(all, BoxLayout.Y_AXIS));
            all.add(JP15);
            all.add(JP16);
            all.add(JP17);
            all.add(JP18);
            all.add(JP19);
            all.add(saveButton);

            newFrame5.add(all);
            newFrame5.setVisible(true);
        });



        JPanel fullContent = new JPanel(new BorderLayout());
        fullContent.setBackground(Color.BLACK);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.BLACK);
        topPanel.add(dateLabel);
        topPanel.add(IT);
        JPanel topPanel1 = new JPanel();
        topPanel1.setLayout(new BoxLayout(topPanel1, BoxLayout.X_AXIS));
        topPanel1.add(dateButton);
        topPanel1.add(reset);
        topPanel1.add(search);
        topPanel.add(topPanel1);
        topPanel.setBorder(null);


        fullContent.add(topPanel, BorderLayout.NORTH);
        fullContent.add(buttonPanel, BorderLayout.CENTER);


        JScrollPane scrollPane = new JScrollPane(fullContent);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        scrollPane.setViewportBorder(null);


        panel.setLayout(new BorderLayout());
        panel.removeAll();  // 혹시 기존 내용 남아 있으면 제거
        panel.add(scrollPane, BorderLayout.CENTER);

    }
}

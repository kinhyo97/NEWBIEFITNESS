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
import java.time.ZoneId;
import java.util.Date;
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
        dateLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 15));
        panel.add(dateLabel);  // 화면 어디든 적당히 add





        // 2. 버튼 만들기 (날짜 선택용)
        JButton dateButton = new JButton("📅 날짜 선택");
        dateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
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
        JLabel totalLabel1 = new JLabel("총 섭취 탄수화물: 0 G");
        totalLabel1.setForeground(Color.WHITE);
        totalLabel1.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        totalLabel1.setHorizontalAlignment(SwingConstants.CENTER);

        IT.add(totalLabel1);

        //단백 표시용
        JLabel totalLabel2 = new JLabel("총 섭취 단백질: 0 G");
        totalLabel2.setForeground(Color.WHITE);
        totalLabel2.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        totalLabel2.setHorizontalAlignment(SwingConstants.CENTER);

        IT.add(totalLabel2);
        //지방 표시용
        JLabel totalLabel3 = new JLabel("총 섭취 지방: 0 G");
        totalLabel3.setForeground(Color.WHITE);
        totalLabel3.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        totalLabel3.setHorizontalAlignment(SwingConstants.CENTER);

        IT.add(totalLabel3);





        panel.add(IT, BorderLayout.SOUTH);




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

        btn.setBackground(Color.white);

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
                System.out.println("입력된 음식: " + foodName); // 나중엔 DB로 저장 가능
                try {
                    double kcal = Double.parseDouble(inputField1.getText()); // 또는 inputField6, 11 등
                    currentBreakfastCalories += kcal; // 또는 lunch, dinner에 따라 따로 변수
                    updateTotalCalories(totalLabel);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame, "칼로리는 숫자로 입력하세요");
                }
                try {
                    double fat = Double.parseDouble(inputField4.getText()); // 또는 inputField6, 11 등
                    currentBreakfastfat += fat; // 또는 lunch, dinner에 따라 따로 변수
                    updateTotalfat(totalLabel3);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame, "섭취한 지방의 양은 숫자로 입력하세요");
                }

                try {
                    double carbohydrate = Double.parseDouble(inputField2.getText()); // 또는 inputField6, 11 등
                    currentBreakfastcarbohydrate += carbohydrate; // 또는 lunch, dinner에 따라 따로 변수
                    updateTotalcarbohydrate(totalLabel1);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame, "섭취한 탄수화물의 양은 숫자로 입력하세요");

                }
                try {
                    double protein = Double.parseDouble(inputField3.getText()); // 또는 inputField6, 11 등
                    currentBreakfastprotein += protein; // 또는 lunch, dinner에 따라 따로 변수
                    updateTotalprotein(totalLabel2);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame, "섭취한 단백질의 양은 숫자로 입력하세요");
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
        btn2.setBackground(Color.ORANGE); // 어두운 배경
        btn2.setBorder(BorderFactory.createBevelBorder(1, new Color(80, 80, 80), new Color(10, 10, 10)));
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


            JFrame newFrame = new JFrame("점심 식단 입력");//창이름


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
                System.out.println("입력된 음식: " + foodName); // 나중엔 DB로 저장 가능
                try {
                    double kcal = Double.parseDouble(inputField6.getText()); // 또는 inputField6, 11 등
                    currentLunchCalories += kcal; // 또는 lunch, dinner에 따라 따로 변수
                    updateTotalCalories(totalLabel);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame, "칼로리는 숫자로 입력하세요");
                }
                try {
                    double fat = Double.parseDouble(inputField9.getText()); // 또는 inputField6, 11 등
                    currentLunchfat += fat; // 또는 lunch, dinner에 따라 따로 변수
                    updateTotalfat(totalLabel3);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame, "섭취한 지방의 양은 숫자로 입력하세요");
                }
                try {
                    double carbohydrate = Double.parseDouble(inputField7.getText()); // 또는 inputField6, 11 등
                    currentLunchcarbohydrate += carbohydrate; // 또는 lunch, dinner에 따라 따로 변수
                    updateTotalcarbohydrate(totalLabel1);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame, "섭취한 탄수화물의 양은 숫자로 입력하세요");

                }
                try {
                    double protein = Double.parseDouble(inputField8.getText()); // 또는 inputField6, 11 등
                    currentLunchprotein += protein; // 또는 lunch, dinner에 따라 따로 변수
                    updateTotalprotein(totalLabel2);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame, "섭취한 단백질의 양은 숫자로 입력하세요");
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
        btn3.setBackground(new Color(30, 30, 30)); // 어두운 배경
        btn3.setForeground(Color.WHITE); // 흰 텍스트
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
            lab11.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
            lab11.setForeground(new Color(30, 30, 30));
            JP11.add(lab11);
            JP11.add(inputField11);


            JPanel JP12 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel lab12 = new JLabel("탄수화물 함량:");
            lab12.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
            lab12.setForeground(new Color(30, 30, 30));
            JP12.add(lab12);
            JP12.add(inputField12);

            JPanel JP13 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel lab13 = new JLabel("단백질 함량:");
            lab13.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
            lab13.setForeground(new Color(30, 30, 30));
            JP13.add(lab13);
            JP13.add(inputField13);

            JPanel JP14 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel lab14 = new JLabel("지방 함량:");
            lab14.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
            lab14.setForeground(new Color(30, 30, 30));
            JP14.add(lab14);
            JP14.add(inputField14);






            JButton saveButton = new JButton("저장");
            saveButton.addActionListener(ev -> {
                String foodName = inputField10.getText();
                System.out.println("입력된 음식: " + foodName); // 나중엔 DB로 저장 가능
                try {
                    double kcal = Double.parseDouble(inputField11.getText()); // 또는 inputField6, 11 등
                    currentDinnerCalories += kcal; // 또는 lunch, dinner에 따라 따로 변수
                    updateTotalCalories(totalLabel);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame3, "칼로리는 숫자로 입력하세요");
                }
                try {
                    double fat = Double.parseDouble(inputField14.getText()); // 또는 inputField6, 11 등
                    currentDinnerfat += fat; // 또는 lunch, dinner에 따라 따로 변수
                    updateTotalfat(totalLabel3);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame3, "섭취한 지방의 양은 숫자로 입력하세요");
                }

                try {
                    double carbohydrate = Double.parseDouble(inputField12.getText()); // 또는 inputField6, 11 등
                    currentDinnercarbohydrate += carbohydrate; // 또는 lunch, dinner에 따라 따로 변수
                    updateTotalcarbohydrate(totalLabel1);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame3, "섭취한 탄수화물의 양은 숫자로 입력하세요");

                }
                try {
                    double protein = Double.parseDouble(inputField13.getText()); // 또는 inputField6, 11 등
                    currentDinnerprotein += protein; // 또는 lunch, dinner에 따라 따로 변수
                    updateTotalprotein(totalLabel2);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame3, "섭취한 단백질의 양은 숫자로 입력하세요");
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
        btn4.setBackground(Color.pink); // 어두운 배경
        btn4.setForeground(Color.GRAY); // 흰 텍스트
        btn4.setBorder(BorderFactory.createBevelBorder(1, new Color(80, 80, 80), new Color(10, 10, 10)));
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


            JFrame newFrame5 = new JFrame("점심 식단 입력");//창이름


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
            JP16.setBackground(new Color(20, 20, 20));
            JLabel lab16 = new JLabel("음식의 칼로리:");
            lab16.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
            lab16.setForeground(new Color(70, 130, 180));
            JP16.add(lab16);
            JP16.add(inputField16);


            JPanel JP17 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JP17.setBackground(new Color(20, 20, 20));
            JLabel lab17 = new JLabel("탄수화물 함량:");
            lab17.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
            lab17.setForeground(new Color(70, 130, 180));
            JP17.add(lab17);
            JP17.add(inputField17);

            JPanel JP18 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JP18.setBackground(new Color(20, 20, 20));
            JLabel lab18 = new JLabel("단백질 함량:");
            lab18.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
            lab18.setForeground(new Color(70, 130, 180));
            JP18.add(lab18);
            JP18.add(inputField18);

            JPanel JP19 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JP19.setBackground(new Color(20, 20, 20));
            JLabel lab19 = new JLabel("지방 함량:");
            lab19.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
            lab19.setForeground(new Color(70, 130, 180));
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
                String foodName = inputField19.getText();
                System.out.println("입력된 음식: " + foodName); // 나중엔 DB로 저장 가능
                try {
                    double kcal = Double.parseDouble(inputField16.getText()); // 또는 inputField6, 11 등
                    currentBreakfastCalories += kcal; // 또는 lunch, dinner에 따라 따로 변수
                    updateTotalCalories(totalLabel);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame5, "칼로리는 숫자로 입력하세요");
                }
                try {
                    double fat = Double.parseDouble(inputField19.getText()); // 또는 inputField6, 11 등
                    currentBreakfastfat += fat; // 또는 lunch, dinner에 따라 따로 변수
                    updateTotalfat(totalLabel3);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame5, "섭취한 지방의 양은 숫자로 입력하세요");
                }

                try {
                    double carbohydrate = Double.parseDouble(inputField17.getText()); // 또는 inputField6, 11 등
                    currentBreakfastcarbohydrate += carbohydrate; // 또는 lunch, dinner에 따라 따로 변수
                    updateTotalcarbohydrate(totalLabel1);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame5, "섭취한 탄수화물의 양은 숫자로 입력하세요");

                }
                try {
                    double protein = Double.parseDouble(inputField18.getText()); // 또는 inputField6, 11 등
                    currentBreakfastprotein += protein; // 또는 lunch, dinner에 따라 따로 변수
                    updateTotalprotein(totalLabel2);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame5, "섭취한 단백질의 양은 숫자로 입력하세요");
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


// 버튼을 감싸는 작은 패널을 반환

        // 버튼 패널을 CENTER에 추가
        panel.add(buttonPanel, BorderLayout.CENTER);

        panel.revalidate();
        panel.repaint();
    }
}
//JScrollpane
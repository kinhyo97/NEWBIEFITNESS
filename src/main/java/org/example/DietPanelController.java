package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import java.awt.*;

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
        wrapper.setBackground(Color.BLACK); // 배경색 유지
        wrapper.add(button);

        return wrapper;
    }


    public static void diet_show(JPanel panel) {
        panel.removeAll();
        panel.setLayout(new BorderLayout()); // 전체 레이아웃을 BorderLayout으로 설정
        panel.setBackground(Color.BLACK);






        JPanel PANEL = new JPanel(new FlowLayout(FlowLayout.CENTER));
        PANEL.setBackground(Color.BLACK);
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        String formattedDate = today.format(formatter);

        JLabel dateLabel = new JLabel(formattedDate);
        dateLabel.setForeground(Color.LIGHT_GRAY);
        dateLabel.setFont(new Font("HY 견고딕", Font.BOLD, 26));
        PANEL.add(dateLabel);
        panel.add(PANEL, BorderLayout.NORTH);


        JPanel IT = new JPanel();
        IT.setLayout(new BoxLayout(IT, BoxLayout.Y_AXIS));
        IT.setBackground(Color.BLACK);

        JPanel PJ6 = new JPanel();
        PJ6.setBackground(Color.BLACK);
        PJ6.setForeground(Color.white);
        PJ6.setLayout(new BoxLayout(PJ6,BoxLayout.Y_AXIS));
        JLabel name = new JLabel("날짜를 입력하세요");
        name.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        name.setForeground(Color.white);
        JTextField input = new JTextField(1);
        input.setMaximumSize(new Dimension(100,30));
        input.setBackground(Color.white);



        PJ6.add(name);
        PJ6.add(input);

        IT.add(PJ6);







        //칼로리 표시용
        JLabel totalLabel = new JLabel("총 섭취 칼로리: 0 kcal");
        totalLabel.setForeground(Color.WHITE);
        totalLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);

        IT.add(totalLabel);
        //지방 표시용
        JLabel totalLabel1 = new JLabel("총 섭취 탄수화물: 0 G");
        totalLabel1.setForeground(Color.WHITE);
        totalLabel1.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        totalLabel1.setHorizontalAlignment(SwingConstants.CENTER);

        IT.add(totalLabel1);

        //탄수화물 표시용
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











        JButton endDayButton = new JButton("오늘 식사 기록 완료");
        endDayButton.setBackground(new Color(30, 30, 30)); // 사이버펑크 색
        endDayButton.setForeground(Color.MAGENTA);
        endDayButton.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        endDayButton.setBorder(BorderFactory.createBevelBorder(1, new Color(80, 80, 80), new Color(10, 10, 10)));
        endDayButton.setFocusPainted(false);
        IT.add(endDayButton);




        panel.add(IT, BorderLayout.SOUTH);



        // 버튼들을 담을 패널 생성
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 50, 30)); // 1행 3열, 좌우 간격 50, 상하 간격 30
        buttonPanel.setBackground(Color.BLACK);

// 버튼 크기 지정
        Dimension buttonSize = new Dimension(200, 200); // 너비 100, 높이 40


// 아침 버튼
        ImageIcon icon = new ImageIcon("src/icons/아침식사.png");

        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(newImg);
        JButton btn = new JButton(scaledIcon);

        btn.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        btn.setPreferredSize(buttonSize);
        btn.setBackground(new Color(30, 30, 30)); // 어두운 배경
        btn.setForeground(Color.MAGENTA); // 흰 텍스트
        btn.setBorder(BorderFactory.createBevelBorder(1, new Color(80, 80, 80), new Color(10, 10, 10)));
        btn.setFocusPainted(false);
        buttonPanel.add(wrapWithPanel(btn));



        btn.addActionListener(e -> {
            JFrame newFrame = new JFrame("아침 식단 입력");//창이름
            newFrame.setSize(350, 500);//크기설정
            newFrame.setLocationRelativeTo(null); // 가운데 정렬
            newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            newFrame.getContentPane().setBackground(Color.BLACK);

            JPanel JP = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JP.setBackground(Color.black);
            JLabel lab = new JLabel("섭취한 음식 이름:");
            JTextField inputField = new JTextField(15);
            JP.add(lab);
            JP.add(inputField);//JPanel JP로 그룹짓기


            JPanel JP1 = new JPanel (new FlowLayout(FlowLayout.LEFT));
            JP1.setBackground(Color.black);
            JLabel lab1 = new JLabel("음식의 칼로리:");
            JTextField inputField1 = new JTextField(15);
            JP1.add(lab1);
            JP1.add(inputField1);


            JPanel JP2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JP2.setBackground(Color.black);
            JLabel lab2 = new JLabel("탄수화물 함량:");
            JTextField inputField2 = new JTextField(15);
            JP2.add(lab2);
            JP2.add(inputField2);

            JPanel JP3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JP3.setBackground(Color.black);
            JLabel lab3 = new JLabel("단백질 함량:");
            JTextField inputField3 = new JTextField(15);
            JP3.add(lab3);
            JP3.add(inputField3);

            JPanel JP4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JP4.setBackground(Color.black);
            JLabel lab4 = new JLabel("지방 함량:");
            JTextField inputField4 = new JTextField(15);
            JP4.add(lab4);
            JP4.add(inputField4);








            JButton saveButton = new JButton("저장");
            saveButton.addActionListener(ev -> {
                String foodName = inputField.getText();
                System.out.println("입력된 음식: " + foodName); // 나중엔 DB로 저장 가능
                try {
                    int kcal = Integer.parseInt(inputField1.getText()); // 또는 inputField6, 11 등
                    currentBreakfastCalories += kcal; // 또는 lunch, dinner에 따라 따로 변수
                    updateTotalCalories(totalLabel);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame, "칼로리는 숫자로 입력하세요");
                }
                try {
                    int fat = Integer.parseInt(inputField4.getText()); // 또는 inputField6, 11 등
                    currentBreakfastfat += fat; // 또는 lunch, dinner에 따라 따로 변수
                    updateTotalfat(totalLabel3);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame, "섭취한 지방의 양은 숫자로 입력하세요");
                }

                try {
                    int carbohydrate = Integer.parseInt(inputField2.getText()); // 또는 inputField6, 11 등
                    currentBreakfastcarbohydrate += carbohydrate; // 또는 lunch, dinner에 따라 따로 변수
                    updateTotalcarbohydrate(totalLabel1);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame, "섭취한 탄수화물의 양은 숫자로 입력하세요");

                }
                try {
                    int protein = Integer.parseInt(inputField3.getText()); // 또는 inputField6, 11 등
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

;           newFrame.add(all);
            newFrame.setVisible(true);
        });

// 점심 버튼
        ImageIcon icon2 = new ImageIcon("src/icons/점심식사.png");

        Image img2 = icon2.getImage();
        Image newImg2 = img2.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon2 = new ImageIcon(newImg2);
        JButton btn2 = new JButton(scaledIcon2);
        btn2.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        btn2.setPreferredSize(buttonSize);
        btn2.setBackground(new Color(30, 30, 30)); // 어두운 배경
        btn2.setForeground(Color.MAGENTA); // 흰 텍스트
        btn2.setBorder(BorderFactory.createBevelBorder(1, new Color(80, 80, 80), new Color(10, 10, 10)));
        btn2.setFocusPainted(false);
        buttonPanel.add(wrapWithPanel(btn2));
        btn2.addActionListener(e -> {

            JFrame newFrame = new JFrame("점심 식단 입력");//창이름
            newFrame.setSize(350, 500);//크기설정
            newFrame.setLocationRelativeTo(null); // 가운데 정렬
            newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel JP5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel lab5 = new JLabel("섭취한 음식 이름:");
            JTextField inputField5 = new JTextField(15);
            JP5.add(lab5);
            JP5.add(inputField5);//JPanel JP로 그룹짓기


            JPanel JP6 = new JPanel (new FlowLayout(FlowLayout.LEFT));
            JLabel lab6 = new JLabel("음식의 칼로리:");
            JTextField inputField6 = new JTextField(15);
            JP6.add(lab6);
            JP6.add(inputField6);


            JPanel JP7 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel lab7 = new JLabel("탄수화물 함량:");
            JTextField inputField7 = new JTextField(15);
            JP7.add(lab7);
            JP7.add(inputField7);

            JPanel JP8 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel lab8 = new JLabel("단백질 함량:");
            JTextField inputField8 = new JTextField(15);
            JP8.add(lab8);
            JP8.add(inputField8);

            JPanel JP9 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel lab9 = new JLabel("지방 함량:");
            JTextField inputField9 = new JTextField(15);
            JP9.add(lab9);
            JP9.add(inputField9);






            JButton saveButton = new JButton("저장");
            saveButton.addActionListener(ev -> {
                String foodName = inputField5.getText();
                System.out.println("입력된 음식: " + foodName); // 나중엔 DB로 저장 가능
                try {
                    int kcal = Integer.parseInt(inputField6.getText()); // 또는 inputField6, 11 등
                    currentLunchCalories += kcal; // 또는 lunch, dinner에 따라 따로 변수
                    updateTotalCalories(totalLabel);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame, "칼로리는 숫자로 입력하세요");
                }
                try {
                    int fat = Integer.parseInt(inputField9.getText()); // 또는 inputField6, 11 등
                    currentLunchfat += fat; // 또는 lunch, dinner에 따라 따로 변수
                    updateTotalfat(totalLabel3);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame, "섭취한 지방의 양은 숫자로 입력하세요");
                }
                try {
                    int carbohydrate = Integer.parseInt(inputField7.getText()); // 또는 inputField6, 11 등
                    currentLunchcarbohydrate += carbohydrate; // 또는 lunch, dinner에 따라 따로 변수
                    updateTotalcarbohydrate(totalLabel1);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame, "섭취한 탄수화물의 양은 숫자로 입력하세요");

                }
                try {
                    int protein = Integer.parseInt(inputField8.getText()); // 또는 inputField6, 11 등
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
        ImageIcon icon3 = new ImageIcon("src/icons/저녁식사.png");

        Image img3 = icon3.getImage();
        Image newImg3 = img3.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon3 = new ImageIcon(newImg3);
        JButton btn3 = new JButton(scaledIcon3);
        btn3.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        btn3.setPreferredSize(buttonSize);
        btn3.setBackground(new Color(30, 30, 30)); // 어두운 배경
        btn3.setForeground(Color.MAGENTA); // 흰 텍스트
        btn3.setBorder(BorderFactory.createBevelBorder(1, new Color(80, 80, 80), new Color(10, 10, 10)));
        btn3.setFocusPainted(false);
        buttonPanel.add(wrapWithPanel(btn3));



        btn3.addActionListener(e -> {
            JFrame newFrame = new JFrame("저녁 식단 입력");//창이름
            newFrame.setSize(350, 500);//크기설정
            newFrame.setLocationRelativeTo(null); // 가운데 정렬
            newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel JP10 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel lab10 = new JLabel("섭취한 음식 이름:");
            JTextField inputField10 = new JTextField(15);
            JP10.add(lab10);
            JP10.add(inputField10);//JPanel JP로 그룹짓기


            JPanel JP11 = new JPanel (new FlowLayout(FlowLayout.LEFT));
            JLabel lab11 = new JLabel("음식의 칼로리:");
            JTextField inputField11 = new JTextField(15);
            JP11.add(lab11);
            JP11.add(inputField11);


            JPanel JP12 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel lab12 = new JLabel("탄수화물 함량:");
            JTextField inputField12 = new JTextField(15);
            JP12.add(lab12);
            JP12.add(inputField12);

            JPanel JP13 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel lab13 = new JLabel("단백질 함량:");
            JTextField inputField13 = new JTextField(15);
            JP13.add(lab13);
            JP13.add(inputField13);

            JPanel JP14 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel lab14 = new JLabel("지방 함량:");
            JTextField inputField14 = new JTextField(15);
            JP14.add(lab14);
            JP14.add(inputField14);






            JButton saveButton = new JButton("저장");
            saveButton.addActionListener(ev -> {
                String foodName = inputField10.getText();
                System.out.println("입력된 음식: " + foodName); // 나중엔 DB로 저장 가능
                try {
                    int kcal = Integer.parseInt(inputField11.getText()); // 또는 inputField6, 11 등
                    currentDinnerCalories += kcal; // 또는 lunch, dinner에 따라 따로 변수
                    updateTotalCalories(totalLabel);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame, "칼로리는 숫자로 입력하세요");
                }
                try {
                    int fat = Integer.parseInt(inputField14.getText()); // 또는 inputField6, 11 등
                    currentDinnerfat += fat; // 또는 lunch, dinner에 따라 따로 변수
                    updateTotalfat(totalLabel3);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame, "섭취한 지방의 양은 숫자로 입력하세요");
                }

                try {
                    int carbohydrate = Integer.parseInt(inputField12.getText()); // 또는 inputField6, 11 등
                    currentDinnercarbohydrate += carbohydrate; // 또는 lunch, dinner에 따라 따로 변수
                    updateTotalcarbohydrate(totalLabel1);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame, "섭취한 탄수화물의 양은 숫자로 입력하세요");

                }
                try {
                    int protein = Integer.parseInt(inputField13.getText()); // 또는 inputField6, 11 등
                    currentDinnerprotein += protein; // 또는 lunch, dinner에 따라 따로 변수
                    updateTotalprotein(totalLabel2);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(newFrame, "섭취한 단백질의 양은 숫자로 입력하세요");
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
            newFrame.add(all);
            newFrame.setVisible(true);
        });

// 버튼을 감싸는 작은 패널을 반환

        // 버튼 패널을 CENTER에 추가
        panel.add(buttonPanel, BorderLayout.CENTER);

        panel.revalidate();
        panel.repaint();
    }
}

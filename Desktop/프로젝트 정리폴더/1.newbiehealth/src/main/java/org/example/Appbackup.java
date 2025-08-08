//package org.example;
//
//import javax.imageio.ImageIO;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.awt.geom.Ellipse2D;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//
//public class Appbackup extends JFrame {
//    //쿼리설정
//    String url = "jdbc:mariadb://localhost:3306/newbiehealth"; // DB 주소와 포트, 스키마
//    String user = "root";       // 사용자명
//    String password = "1234"; // 비밀번호
//    String query = "SELECT * FROM exercise";  // 원하는 테이블 쿼리
//    public static boolean isLogined = false;
//    public static String userKey = null;
//    public static String user_id = null;
//    public static String user_name = null;
//    private JLabel logoLabel;
//    private JLabel profilePic;
//    //쿼리사용 예시
//    /*
//    try {
//        // 3. 드라이버 로드
//        Class.forName("org.mariadb.jdbc.Driver");
//        // 4. 커넥션 열기
//        Connection conn = DriverManager.getConnection(url, user, password);
//        // 5. 쿼리 실행
//        Statement stmt = conn.createStatement();
//        ResultSet rs = stmt.executeQuery(query);
//        // 6. 결과 출력
//        while (rs.next()) {
//            int id = rs.getInt("exercise_id");
//            String name = rs.getString("exercise_name");
//            System.out.println("운동 ID: " + id + ", 이름: " + name);
//        }
//        // 7. 자원 정리
//        rs.close();
//        stmt.close();
//        conn.close();
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//     */
//
//
//
//    private final String[] names = {"HOME", "ROUTINE", "STATISTICS", "DIET", "TIP"};
//    private final String[] iconPaths = {
//            "src/icons/cleanbar_home.png",
//            "src/icons/cleanbar_routine.png",
//            "src/icons/cleanbar_statistics.png",
//            "src/icons/cleanbar_diet.png",
//            "src/icons/cleanbar_tip.png"
//    };
//
//    private final CardLayout cardLayout = new CardLayout();
//    private final JPanel centerPanel = new JPanel(cardLayout);
//
//    // 패널 각각 저장
//    private JPanel homePanel, routinePanel, statisticsPanel, dietPanel, tipPanel;
//
//    public Appbackup() {
//        setTitle("SUGGEST FITNESS");
//        setSize(500, 800);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setLayout(new BorderLayout());
//        getContentPane().setBackground(Color.BLACK);
//
//        add(createTopPanel(), BorderLayout.NORTH);
//        add(createBottomNav(), BorderLayout.SOUTH);
//        add(createCenterPanel(), BorderLayout.CENTER);
//
//        System.out.println("현재 로그인 유저 : "+userKey +" user_id : "+user_id);
//        //new SurveyPage();
//        //home_show();
//
//        setVisible(true);
//
//    }
//
//    private JPanel createTopPanel() {
//        JPanel topPanel = new JPanel(new BorderLayout());
//        topPanel.setBackground(Color.BLACK);
//        topPanel.setPreferredSize(new Dimension(500, 60));
//
//        ImageIcon logoIcon = new ImageIcon("src/icons/newbiehealthlogo.png");
//        Image scaledLogo = logoIcon.getImage().getScaledInstance(120, 40, Image.SCALE_SMOOTH);
//        logoLabel = new JLabel(new ImageIcon(scaledLogo));
//        logoLabel.setOpaque(true);  // 배경색 설정해서 시각 확인
//        logoLabel.setBackground(Color.RED);
//
//// 이벤트 리스너도 이 logoLabel에 붙이기
//        logoLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        logoLabel.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                showProfilePictureDialog(); // ✅ this.logoLabel 접근 가능
//            }
//        });
//
//
//
//
//
//
//
//
//
//        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
//        leftPanel.setBackground(Color.BLACK);
//        leftPanel.add(logoLabel);
//
//        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
//        rightPanel.setBackground(Color.BLACK);
//
//        JLabel welcomeLabel = new JLabel(user_name+"님 환영합니다");
//        welcomeLabel.setForeground(Color.WHITE);
//        welcomeLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
//
//        profilePic = new JLabel();
//        BufferedImage profileImage;
//        try {
//            File profileFile = new File("src/user_profile/" + userKey + ".png");
//
//            // ❗ 없으면 default 이미지로 대체
//            if (!profileFile.exists()) {
//                profileFile = new File("src/user_profile/default.png");
//            }
//
//            profileImage = ImageIO.read(profileFile);
//            Image roundedProfile = makeRoundedProfile(profileImage, 40);
//            profilePic.setIcon(new ImageIcon(roundedProfile));
//
//        } catch (IOException e) {
//            System.out.println("⚠ 프로필 이미지 로딩 실패: " + e.getMessage());
//            // 실패해도 기본값 표시 (회색 원 등)
//            profilePic.setIcon(new ImageIcon(new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB)));
//        }
//
//        profilePic.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        profilePic.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                showProfilePictureDialog();
//            }
//        });
//
//
//
//
//        rightPanel.add(welcomeLabel);
//        rightPanel.add(profilePic);
//
//        topPanel.add(leftPanel, BorderLayout.WEST);
//        topPanel.add(rightPanel, BorderLayout.EAST);
//
//        return topPanel;
//    }
//
//    private JPanel createBottomNav() {
//        JPanel navPanel = new JPanel(new GridLayout(1, 5));
//        navPanel.setBackground(new Color(27, 26, 27));       // RGB 방식
//
//
//        for (int i = 0; i < iconPaths.length; i++) {
//            String label = names[i];
//            String path = iconPaths[i];
//
//            ImageIcon icon = new ImageIcon(path);
//            Image scaled = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
//            ImageIcon scaledIcon = new ImageIcon(scaled);
//
//            JLabel iconLabel = new JLabel(scaledIcon);
//            iconLabel.setHorizontalAlignment(JLabel.CENTER);
//            iconLabel.setVerticalAlignment(JLabel.CENTER);
//            iconLabel.setOpaque(false);
//
//            int index = i;
//            iconLabel.addMouseListener(new MouseAdapter() {
//                @Override
//                public void mouseClicked(MouseEvent e) {
//                    switchCard(names[index]);
//                    switch (names[index]) {
//                        case "HOME" -> home_show();
//                        case "ROUTINE" -> routine_show();
//                        case "STATISTICS" -> statistics_show();
//                        case "DIET" -> diet_show();
//                        case "TIP" -> tip_show();
//                    }
//                }
//            });
//
//            navPanel.add(iconLabel);
//        }
//
//        return navPanel;
//    }
//
//    private JPanel createCenterPanel() {
//        for (String name : names) {
//            JPanel page = new JPanel();
//            page.setBackground(Color.BLACK);
//
//            switch (name) {
//                case "HOME" -> homePanel = page;
//                case "ROUTINE" -> routinePanel = page;
//                case "STATISTICS" -> statisticsPanel = page;
//                case "DIET" -> dietPanel = page;
//                case "TIP" -> tipPanel = page;
//            }
//
//            centerPanel.add(page, name);
//        }
//        return centerPanel;
//    }
//
//    // ✅ 각 패널마다 show 함수 정의
//    public void home_show() {
//        HomePanelController.home_show(homePanel, this);
//    }
//
//    public void routine_show() {
//        RoutinePanelController.routine_show(routinePanel,this);
//    }
//
//    public void statistics_show() {
//        StatisticsPanelController.statistics_show(statisticsPanel,this);
//    }
//
//    public void diet_show() {
//        DietPanelController.diet_show(dietPanel,this);
//    }
//
//    public void tip_show() {
//        TipPanelController.tip_show(tipPanel,this);
//    }
//
//
//    // ✅ 카드 전환 함수 (새로 추가!)
//    public void switchCard(String name) {
//        cardLayout.show(centerPanel, name);
//    }
//
//    // ✅ 동그란 이미지
//    private Image makeRoundedProfile(BufferedImage image, int size) {
//        BufferedImage rounded = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g2 = rounded.createGraphics();
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        Ellipse2D circle = new Ellipse2D.Float(0, 0, size, size);
//        g2.setClip(circle);
//        g2.drawImage(image, 0, 0, size, size, null);
//        g2.dispose();
//        return rounded;
//    }
//
//    private void showProfilePictureDialog() {
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setDialogTitle("프로필 사진 선택");
//
//        int result = fileChooser.showOpenDialog(null);
//        if (result == JFileChooser.APPROVE_OPTION) {
//            File selectedFile = fileChooser.getSelectedFile();
//            String imagePath = selectedFile.getAbsolutePath();
//
//            // 저장할 위치 지정 (userKey 기반 이름으로)
//            File destFile = new File("src/user_profile/" + userKey + ".png");
//
//            try {
//                // 기존 파일이 있다면 삭제
//                if (destFile.exists()) {
//                    destFile.delete();
//                }
//
//                // 선택된 이미지를 PNG로 복사 저장
//                BufferedImage img = ImageIO.read(selectedFile);
//                ImageIO.write(img, "png", destFile);
//
//                // UI에 프로필 이미지 적용 (동그랗게)
//                Image roundedProfile = makeRoundedProfile(img, 40);
//                profilePic.setIcon(new ImageIcon(roundedProfile));
//
//                System.out.println("✅ 프로필 사진 저장 완료: " + destFile.getAbsolutePath());
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                JOptionPane.showMessageDialog(this, "이미지 저장 실패: " + e.getMessage());
//            }
//        }
//    }
//
//
//
//
//
//
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            new loginPage(() -> {
//                // 로그인 성공 시 SurveyPage 실행
//                new SurveyPage(() -> {
//                    // 설문 완료 후 App 실행
//                    try {
//                        new Appbackup();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                });
//            });
//        });
//    }
//
///*원래꺼
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            new loginPage(() -> {
//                try {
//                    new App();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//        });
//    }
//
//    */
//
//
//
//
//
//
//
//
//
//}

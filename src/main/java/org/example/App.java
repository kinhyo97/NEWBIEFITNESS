package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import javax.imageio.ImageIO;
import org.example.loginPage;

import org.example.HomePanelController;
import org.example.RoutinePanelController;
import org.example.StatisticsPanelController;
import org.example.DietPanelController;
import org.example.TipPanelController;

public class App extends JFrame {
    //쿼리설정
    String url = "jdbc:mariadb://localhost:3306/newbiehealth"; // DB 주소와 포트, 스키마
    String user = "root";       // 사용자명
    String password = "1234"; // 비밀번호
    String query = "SELECT * FROM exercise";  // 원하는 테이블 쿼리
    public static boolean isLogined = false;
    public static String userKey = null;
    public static String user_id = null;
    public static String user_name = null;
    //쿼리사용 예시
    /*
    try {
        // 3. 드라이버 로드
        Class.forName("org.mariadb.jdbc.Driver");
        // 4. 커넥션 열기
        Connection conn = DriverManager.getConnection(url, user, password);
        // 5. 쿼리 실행
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        // 6. 결과 출력
        while (rs.next()) {
            int id = rs.getInt("exercise_id");
            String name = rs.getString("exercise_name");
            System.out.println("운동 ID: " + id + ", 이름: " + name);
        }
        // 7. 자원 정리
        rs.close();
        stmt.close();
        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
     */



    private final String[] names = {"HOME", "ROUTINE", "STATISTICS", "DIET", "TIP"};
    private final String[] iconPaths = {
            "src/icons/cleanbar_home.png",
            "src/icons/cleanbar_routine.png",
            "src/icons/cleanbar_statistics.png",
            "src/icons/cleanbar_diet.png",
            "src/icons/cleanbar_tip.png"
    };

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel centerPanel = new JPanel(cardLayout);

    // 패널 각각 저장
    private JPanel homePanel, routinePanel, statisticsPanel, dietPanel, tipPanel;

    public App() {
        setTitle("SUGGEST FITNESS");
        setSize(500, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.BLACK);

        add(createTopPanel(), BorderLayout.NORTH);
        add(createBottomNav(), BorderLayout.SOUTH);
        add(createCenterPanel(), BorderLayout.CENTER);

        System.out.println("현재 로그인 유저 : "+userKey +" user_id : "+user_id);
        home_show();
        setVisible(true);

    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK);
        topPanel.setPreferredSize(new Dimension(500, 60));

        ImageIcon logoIcon = new ImageIcon("src/icons/newbiehealthlogo.png");
        Image scaledLogo = logoIcon.getImage().getScaledInstance(120, 40, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        leftPanel.setBackground(Color.BLACK);
        leftPanel.add(logoLabel);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setBackground(Color.BLACK);

        JLabel welcomeLabel = new JLabel(user_name+"님 환영합니다");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));

        JLabel profilePic = new JLabel();
        try {
            BufferedImage profileImage = ImageIO.read(new File("src/user_profile/"+userKey+".png"));
            Image roundedProfile = makeRoundedProfile(profileImage, 40);
            profilePic.setIcon(new ImageIcon(roundedProfile));
        } catch (IOException e) {
            System.out.println("⚠ 프로필 이미지 로딩 실패: " + e.getMessage());
        }

        rightPanel.add(welcomeLabel);
        rightPanel.add(profilePic);

        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(rightPanel, BorderLayout.EAST);

        return topPanel;
    }

    private JPanel createBottomNav() {
        JPanel navPanel = new JPanel(new GridLayout(1, 5));
        navPanel.setBackground(Color.BLACK);

        for (int i = 0; i < iconPaths.length; i++) {
            String label = names[i];
            String path = iconPaths[i];

            ImageIcon icon = new ImageIcon(path);
            Image scaled = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaled);

            JLabel iconLabel = new JLabel(scaledIcon);
            iconLabel.setHorizontalAlignment(JLabel.CENTER);
            iconLabel.setVerticalAlignment(JLabel.CENTER);
            iconLabel.setOpaque(false);

            int index = i;
            iconLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    switchCard(names[index]);
                    switch (names[index]) {
                        case "HOME" -> home_show();
                        case "ROUTINE" -> routine_show();
                        case "STATISTICS" -> statistics_show();
                        case "DIET" -> diet_show();
                        case "TIP" -> tip_show();
                    }
                }
            });

            navPanel.add(iconLabel);
        }

        return navPanel;
    }

    private JPanel createCenterPanel() {
        for (String name : names) {
            JPanel page = new JPanel();
            page.setBackground(Color.BLACK);

            switch (name) {
                case "HOME" -> homePanel = page;
                case "ROUTINE" -> routinePanel = page;
                case "STATISTICS" -> statisticsPanel = page;
                case "DIET" -> dietPanel = page;
                case "TIP" -> tipPanel = page;
            }

            centerPanel.add(page, name);
        }
        return centerPanel;
    }

    // ✅ 각 패널마다 show 함수 정의
    public void home_show() {
        HomePanelController.home_show(homePanel, this);
    }

    public void routine_show() {
        RoutinePanelController.routine_show(routinePanel,this);
    }

    public void statistics_show() {
        StatisticsPanelController.statistics_show(statisticsPanel,this);
    }

    public void diet_show() {
        DietPanelController.diet_show(dietPanel,this);
    }

    public void tip_show() {
        TipPanelController.tip_show(tipPanel,this);
    }

    // ✅ 카드 전환 함수 (새로 추가!)
    public void switchCard(String name) {
        cardLayout.show(centerPanel, name);
    }

    // ✅ 동그란 이미지
    private Image makeRoundedProfile(BufferedImage image, int size) {
        BufferedImage rounded = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = rounded.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Ellipse2D circle = new Ellipse2D.Float(0, 0, size, size);
        g2.setClip(circle);
        g2.drawImage(image, 0, 0, size, size, null);
        g2.dispose();
        return rounded;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new loginPage(() -> {
                try {
                    new App();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
    }

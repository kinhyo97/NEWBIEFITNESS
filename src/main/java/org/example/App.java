import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.example.HomePanelController;
import org.example.RoutinePanelController;
import org.example.StatisticsPanelController;
import org.example.DietPanelController;
import org.example.TipPanelController;

public class App extends JFrame {

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

        setVisible(true);

        // 시작 시 홈 출력
        home_show();
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK);
        topPanel.setPreferredSize(new Dimension(500, 60));

        // 로고
        ImageIcon logoIcon = new ImageIcon("src/icons/newbiehealthlogo.png");
        Image scaledLogo = logoIcon.getImage().getScaledInstance(120, 40, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        leftPanel.setBackground(Color.BLACK);
        leftPanel.add(logoLabel);

        // 우측 텍스트 + 동그란 이미지
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setBackground(Color.BLACK);

        JLabel welcomeLabel = new JLabel("홍길동님 환영합니다");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));

        JLabel profilePic = new JLabel();
        try {
            BufferedImage profileImage = ImageIO.read(new File("src/icons/profile.png"));
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
                    cardLayout.show(centerPanel, names[index]);

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

            // 각 이름에 따라 변수에 저장
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

    private void home_show() {
        HomePanelController.home_show(homePanel);
    }

    private void routine_show() {
        RoutinePanelController.routine_show(routinePanel);
    }
    private void statistics_show() {
        StatisticsPanelController.statistics_show(statisticsPanel);
    }
    private void diet_show() {
        DietPanelController.diet_show(dietPanel);
    }
    private void tip_show() {
        TipPanelController.tip_show(tipPanel);
    }

    // ✅ 공통으로 텍스트 출력하는 메서드
    private void showTextOnPanel(JPanel panel, String text) {
        if (panel != null) {
            panel.removeAll();
            JLabel label = new JLabel(text);
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Malgun Gothic", Font.BOLD, 24));
            panel.add(label);
            panel.revalidate();
            panel.repaint();
        }
    }

    // ✅ 동그란 프로필 이미지 만들기
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
        SwingUtilities.invokeLater(App::new);
    }
}

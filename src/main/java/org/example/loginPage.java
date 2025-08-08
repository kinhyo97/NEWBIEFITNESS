package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.*;

public class loginPage extends JFrame {
    // DB 설정
    String url = "jdbc:mariadb://localhost:3306/newbiehealth";
    String user = "root";
    String password = "1234";
    String query = "SELECT * FROM exercise";

    public loginPage(Runnable onLoginSuccess) {
        setTitle("SUGGEST FITNESS");
        setSize(500, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(255, 182, 193)); // 연핑크 (LightPink)
        LoginBox loginBox = new LoginBox(onLoginSuccess);  // ✅ 객체 생성
        loginBox.setOpaque(false);
        add(loginBox, BorderLayout.CENTER);  // ✅ 이걸 추가
        setVisible(true);
    }

    //
    class LoginBox extends JPanel {

        public JTextField userField;
        public JPasswordField passField;
        public JButton loginButton;
        public JButton registrationButton;

        public LoginBox(Runnable onLoginSuccess) {
            // 바깥 레이아웃 설정
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(new Color(0, 0, 0));  // 배경 검정

            // 내부 흰색 박스 패널 생성
            RoundedPanel boxPanel = new RoundedPanel(30);
            boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
            boxPanel.setBackground(Color.WHITE);
            boxPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
            boxPanel.setMaximumSize(new Dimension(350, 600));
            boxPanel.setMinimumSize(new Dimension(350, 600));
            this.setPreferredSize(new Dimension(500, 800));
            boxPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            boxPanel.setPreferredSize(new Dimension(400,300));

            // 기존 컴포넌트들 생성
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/icons/newbiehealthlogo.png"));
            Image logoImg = logoIcon.getImage().getScaledInstance(100, 30, Image.SCALE_SMOOTH);
            logoIcon = new ImageIcon(logoImg);
            JLabel logoLabel = new JLabel(logoIcon);
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            userField = new RoundedTextField(25);
            userField.setMaximumSize(new Dimension(250,30));
            userField.setPreferredSize(new Dimension(250,30));
            userField.setMaximumSize(new Dimension(250,30));
            userField.setAlignmentX(Component.CENTER_ALIGNMENT);

            passField = new RoundedPasswordField(25);
            passField.setMaximumSize(new Dimension(250,30));
            passField.setPreferredSize(new Dimension(250,30));
            passField.setMaximumSize(new Dimension(250,30));
            passField.setAlignmentX(Component.CENTER_ALIGNMENT);

            loginButton = new RoundedButton("로그인",25);
            loginButton.setMinimumSize(new Dimension(250, 30));
            loginButton.setPreferredSize(new Dimension(250, 30));
            loginButton.setMaximumSize(new Dimension(250,30));
            loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            registrationButton = new RoundedButton("회원가입",25);
            registrationButton.setMinimumSize(new Dimension(250, 30));
            registrationButton.setPreferredSize(new Dimension(250, 30));
            registrationButton.setMaximumSize(new Dimension(250,30));
            registrationButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            registrationButton.addActionListener(e -> {
                JDialog signupDialog = new SignupDialog();
                signupDialog.setVisible(true);
            });

            loginButton.addActionListener(e -> {
                String inputId = userField.getText();
                String inputPw = new String(passField.getPassword());
                System.out.println("🟡 입력된 ID: " + inputId + ", PW: " + inputPw);

                try {
                    Class.forName("org.mariadb.jdbc.Driver");
                    Connection conn = DriverManager.getConnection(
                            "jdbc:mariadb://localhost:3306/newbiehealth", "root", "1234");

                    String sql = "SELECT * FROM user WHERE user_id = ? AND password = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, inputId);
                    pstmt.setString(2, inputPw);
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        App.isLogined = true;
                        App.userKey = rs.getString("user_key");
                        App.user_id = rs.getString("user_id");
                        App.user_name = rs.getString("name");
                        System.out.println("🔑 로그인한 유저 키: " + App.userKey);


                        try (PreparedStatement checkStmt = conn.prepareStatement(
                                "SELECT survey_completed FROM user WHERE user_key = ?")) {

                            checkStmt.setString(1, App.userKey);
                            try (ResultSet checkRs = checkStmt.executeQuery()) {
                                if (checkRs.next()) {
                                    boolean done = checkRs.getBoolean("survey_completed");

                                    dispose(); // 로그인 창 닫기

                                    if (done) {
                                        new App().home_show(); // 설문 완료 → 바로 홈
                                    } else {
                                        new SurveyPage(() -> new App().home_show()); // 설문 먼저
                                    }
                                }
                            }
                        }



                        /*
                        dispose();            // 로그인 창 닫기
                        onLoginSuccess.run(); // App 실행
                        원래코드
                         */
                    } else {
                        JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호가 틀렸습니다.");
                    }

                    rs.close();
                    pstmt.close();
                    conn.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "DB 연결 오류: " + ex.getMessage());
                }
            });

            JLabel titleLabel = new JLabel("Log in to your account");
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            titleLabel.setForeground(Color.PINK);

// ⬇️ boxPanel에 컴포넌트 추가
            boxPanel.add(Box.createVerticalGlue());     // 상단 여백
            boxPanel.add(titleLabel);
            boxPanel.add(Box.createVerticalStrut(15));
            boxPanel.add(userField);
            boxPanel.add(Box.createVerticalStrut(15));
            boxPanel.add(passField);
            boxPanel.add(Box.createVerticalStrut(15));
            boxPanel.add(loginButton);
            boxPanel.add(Box.createVerticalStrut(10));
            boxPanel.add(registrationButton);
            boxPanel.add(Box.createVerticalStrut(10));
            //boxPanel.add(registerButton); // 🔴 이제 회원가입 버튼도 포함
            boxPanel.add(Box.createVerticalGlue());

// ⬇️ LoginBox 자체에 추가
            add(Box.createVerticalGlue());
            add(boxPanel);
            add(Box.createVerticalGlue());
    }


    //ROUND PANEL CLASS
    class RoundedPanel extends JPanel {
        private int cornerRadius;

        public RoundedPanel(int radius) {
            super();
            this.cornerRadius = radius;
            setOpaque(false); // 꼭 필요함! 배경 투명 유지
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Dimension arcs = new Dimension(cornerRadius, cornerRadius);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcs.width, arcs.height);
            g2.dispose();
        }
    }


    //TEXTFIELD CLASS ROUND
    public class RoundedTextField extends JTextField {
        private int radius;
        private boolean focused = false;

        public RoundedTextField(int radius) {
            super();
            this.radius = radius;
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            setBackground(Color.WHITE);
            // 🔸 포커스 감지 리스너 추가
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    focused = true;
                    repaint();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    focused = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(getBackground());
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();

            super.paintComponent(g); // 텍스트 그리기
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setStroke(new BasicStroke(2));

            // 🔸 포커스 여부에 따라 테두리 색 바꾸기
            if (focused) {
                g2.setColor(new Color(255, 105, 180)); // 핫핑크
            } else {
                g2.setColor(Color.GRAY);
            }

            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
        }
    }


    //password field
    public class RoundedPasswordField extends JPasswordField {
        private int radius;

        public RoundedPasswordField(int radius) {
            super();
            this.radius = radius;
            setOpaque(false); // 배경 투명
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // 안쪽 여백
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(getBackground());
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g); // 비밀번호 텍스트 점 표시 등
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(Color.GRAY); // 테두리 색상
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
        }
    }


    //round button
    public class RoundedButton extends JButton {
        private int radius;

        public RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setFocusPainted(false); // 포커스 테두리 제거
            setContentAreaFilled(false); // 기본 버튼 배경 제거
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // 안쪽 여백

            // 🔴 버튼 배경을 연핑크로 지정
            setBackground(new Color(255, 182, 193)); // LightPink
            setForeground(Color.WHITE); // 텍스트 색 (필요 시 변경)
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(getBackground());
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(getForeground()); // 테두리 색
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
        }
    }



    public static void main(String[] args) {

    }
}}

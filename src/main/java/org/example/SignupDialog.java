package org.example;

import org.example.component.PrettyButton;
import org.example.component.PrettyButton2;
import org.example.component.RoundButton2;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SignupDialog extends JDialog {
    private JTextField userIdField;
    private JPasswordField passwordField;
    public SignupDialog() {



        setTitle("회원가입");
        setSize(400, 430);
        setLocationRelativeTo(null);
        setModal(true);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        add(createLineBox(15));
        add(createCenterLabelBox("회원가입", 20, 15));
        add(createLineBox(10));
        add(straiteLine(3));
        add(createLineBox(20));
        add(createCenterLabelBox("아이디와 비밀번호를 설정하세요", 30, 20));
        add(createLineBox(10));
        add(createCenterLabelBox("비밀번호를 꼭 기억해주세요!", 20, 12));
        add(createCenterLabelBox("비밀번호를 잊어버리실 경우 찾을 수 없습니다.", 20, 12));
        add(createLineBox(30));
        userIdField = new JTextField();
        passwordField = new JPasswordField();
        add(createCenteredTextFieldBox(userIdField, 160, 30));
        add(createLineBox(5));
        add(createCenteredTextFieldBox(passwordField, 160, 30));

        add(createLineBox(40));
        add(createCenterLabelBox("쉽게 알아볼 수 없는 패드워드 사용을 권장합니다.", 20, 12));
        add(createLineBox(10));

        RoundButton2 acceptButton = new RoundButton2("가입하기",30);
        acceptButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        acceptButton.addActionListener(e -> handleSignup());
        add(acceptButton);


    }


    private JPanel createLineBox(int height) {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        //box.setBackground(Color.BLACK);
        box.setPreferredSize(new Dimension(600, height));
        box.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        box.setMinimumSize(new Dimension(600, height));
        //box.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.YELLOW));
        box.setAlignmentX(Component.CENTER_ALIGNMENT);
        return box;
    }

    private JPanel straiteLine(int height) {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(Color.BLACK);
        box.setPreferredSize(new Dimension(600, height));
        box.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        box.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.PINK));
        box.setAlignmentX(Component.CENTER_ALIGNMENT);
        return box;
    }

    private JPanel createCenterLabelBox(String text, int height, int fontSize) {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(Color.WHITE);
        box.setPreferredSize(new Dimension(600, height));
        box.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        //box.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
        box.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.add(Box.createVerticalGlue());

        JLabel label = new JLabel(text);
        label.setFont(new Font("맑은 고딕", Font.BOLD, fontSize));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.add(label);

        box.add(Box.createVerticalGlue());
        return box;
    }


    private JPanel createCenteredTextFieldBox(JTextField field, int width, int height) {
        JPanel parent = new JPanel();
        parent.setLayout(new BoxLayout(parent, BoxLayout.Y_AXIS));
        parent.setOpaque(false);

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.X_AXIS));
        box.setOpaque(false);

        field.setMaximumSize(new Dimension(width, height));
        field.setPreferredSize(new Dimension(width, height));
        field.setHorizontalAlignment(JTextField.CENTER);

        box.add(Box.createHorizontalGlue());
        box.add(field);
        box.add(Box.createHorizontalGlue());

        parent.add(box);
        return parent;
    }


    private void handleSignup() {
        String userId = userIdField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (userId.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "아이디와 비밀번호를 모두 입력하세요.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mariadb://localhost:3306/newbiehealth", "root", "1234")) {

            // ✅ user_id 중복 체크
            String checkSql = "SELECT COUNT(*) FROM user WHERE user_id = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, userId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(this, "이미 존재하는 아이디입니다.");
                    return;
                }
            }

            // ✅ user_key 자동 생성
            String newUserKey = generateNextUserKey(conn); // 예: "U021"

            // ✅ INSERT 실행
            String insertSql = "INSERT INTO user (user_key, user_id, password, created_at) VALUES (?, ?, ?, CURDATE())";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, newUserKey);
                insertStmt.setString(2, userId);
                insertStmt.setString(3, password);
                insertStmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "회원가입에 성공했습니다! 로그인을 해주세요 ");
            dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "회원가입 중 오류가 발생했습니다.");
        }
    }


    private String generateNextUserKey(Connection conn) throws Exception {
        String sql = "SELECT MAX(user_key) FROM user";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String lastKey = rs.getString(1); // e.g., "U019"
                if (lastKey == null) return "U001"; // 아무도 없을 때

                int number = Integer.parseInt(lastKey.substring(1)); // "019" → 19
                return String.format("U%03d", number + 1);           // 20 → "U020"
            }
        }
        return "U001";
    }





}

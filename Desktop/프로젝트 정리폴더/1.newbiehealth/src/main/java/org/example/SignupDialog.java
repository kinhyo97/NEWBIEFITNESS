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

// 회원가입을 위한 JDialog창
public class SignupDialog extends JDialog {
    private JTextField userIdField; //user id를 받을 id field 생성
    private JPasswordField passwordField; // user password를 받은 passwordfield 생성
    public SignupDialog() {
        getContentPane().setBackground(Color.WHITE);
        setTitle("회원가입");
        setSize(400, 450);
        setLocationRelativeTo(null); // 창의 위치를 화면 중앙으로 옮김
        setModal(true); //해당 dialog창이 실행됐을 때 다른 창에서 조작을 할 수 없게 함
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS)); // 팝업창 내부를 박스레이아웃으로 수직으로 배치함
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // 해당 창이 종료될때 그 창만 종료하고 리소스를 정리함.

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
        add(createCenterLabelBox("  id", 20, 12));
        userIdField = new JTextField();
        passwordField = new JPasswordField();
        add(createCenteredTextFieldBox(userIdField, 160, 30));
        add(createLineBox(5));
        add(createCenterLabelBox("password", 20, 12));
        add(createCenteredTextFieldBox(passwordField, 160, 30));
        add(createLineBox(40));
        add(createCenterLabelBox("쉽게 알아볼 수 없는 패드워드 사용을 권장합니다.", 20, 12));
        add(createLineBox(10));

        RoundButton2 acceptButton = new RoundButton2("가입하기",30);
        acceptButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        acceptButton.addActionListener(e -> handleSignup()); //handleSignup 함수를 통해 회원가입 로직 검증 수행
        add(acceptButton);


    }


    private JPanel createLineBox(int height) {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        //box.setBackground(Color.BLACK); // 테스트시 background 컬러를 다르게 설정해서 범위 확인
        box.setPreferredSize(new Dimension(600, height));
        box.setMaximumSize(new Dimension(Integer.MAX_VALUE, height)); //height를 받아서 박스의 크기를 조정
        box.setMinimumSize(new Dimension(600, height));
        //box.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.YELLOW)); // JPanel의 외부 border 조정
        box.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.setOpaque(true);  // 불투명하게 만들어줘야 자기 배경을 그림
        box.setBackground(Color.WHITE);  // 배경을 흰색으로 명시
        return box; // add함수가 JPanel 객체를 받기 때문에 box객체를 return 하도록 설정
    }

    //가로 직선을 긋기위한 JPanel 객체 생성하는 코드
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

    //센터에 Label를 담기위한 코드로써 JPanel안에 JLabel을 담는다
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
        label.setAlignmentX(Component.CENTER_ALIGNMENT); //부모요소를 기준으로 정렬하기 때문에 박스 내부에서 센터로 가게됨.
        box.add(label);

        box.add(Box.createVerticalGlue());
        return box;
    }

    //센터에서 FeildBox로부터 사용자의 입력을 받도록 하는 코드
    private JPanel createCenteredTextFieldBox(JTextField field, int width, int height) {
        JPanel parent = new JPanel();
        parent.setLayout(new BoxLayout(parent, BoxLayout.Y_AXIS));
        parent.setOpaque(true);

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.X_AXIS));
        box.setOpaque(true);
        box.setBackground(Color.WHITE);

        //텍스트필드의 크기를 설정함
        field.setMaximumSize(new Dimension(width, height));
        field.setPreferredSize(new Dimension(width, height));
        field.setHorizontalAlignment(JTextField.CENTER);

        //field를 담는 box패널안에서 중앙으로 배치하기 위해 수직 glue를 통해 양옆으로 밀게됨.
        box.add(Box.createHorizontalGlue());
        box.add(field);
        box.add(Box.createHorizontalGlue());

        parent.add(box);
        return parent;
    }

    //회원가입 로직을 구현하기 위한 코드
    private void handleSignup() {
        String userId = userIdField.getText().trim(); // userfield로부터 데이터를 getText()로 받아 공백 제거
        String password = new String(passwordField.getPassword()).trim(); // password 데이터를 getText()로 받아 공백 제거

        if (userId.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "아이디와 비밀번호를 모두 입력하세요.");
            return; // 회원가입 로직을 구현하는 메서드이기때문에 return을 통해 메서드 자체를 빠져나오게 됨.
        }

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mariadb://localhost:3306/newbiehealth", "root", "1234")) {

            // ✅ user_id 를 select하여 아이디의 중복체크
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
            // generateNextUserKey 메서드를 통해 user_key가 어디까지 생성되어있는지 확인 후 userkey를 발급
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

    //user_key의 마지막 번호를 확인후 다음 유저키를 발급
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

package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicInteger;
import org.example.component.PrettyButton;
import org.example.component.CircularProgressBar;
import org.example.db.DatabaseUtil;

public class HomePanelController {

    public static void home_show(JPanel panel, App app) {
        // Home panel의 기본 설정
        int goalWeight = 0;
        int presentWeight = 0;
        panel.removeAll(); //패널초기화
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK); // 홈패널의 기본색을 black으로 설정

        //프로필에 있는 사용자의 이름 출력 label
        JLabel label = new JLabel(
                (App.user_name != null ? App.user_name : "회원") + "님 어서오세요~"
        );
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        AtomicInteger progressPercent = new AtomicInteger(0);
        JLabel percentLabel = new JLabel("");
        JLabel settingLabel = new JLabel("목표 체중까지 남은 무게 : - kg");
        try (Connection conn = DatabaseUtil.getConnection()) {
            String sql = "SELECT goal_weight, present_weight FROM user_survey WHERE user_key = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, App.userKey);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                goalWeight = rs.getInt("goal_weight");
                presentWeight = rs.getInt("present_weight");

                int percent = 0;
                if (presentWeight > 0) {
                    percent = 100 - (int)(((presentWeight - goalWeight) / (double)presentWeight) * 100);
                    percent = Math.max(0, Math.min(percent, 100)); // 0~100 사이로 제한
                }
                progressPercent.set(percent); // ✅ 값 저장

                percentLabel.setText("");

                int remainKg = goalWeight - presentWeight;
                settingLabel.setText("목표 체중까지 남은 무게 : " + remainKg + "kg");
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        settingLabel.setForeground(Color.CYAN);
        settingLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 18));
        settingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //CicularProgressBar 초기화
        CircularProgressBar circle = new CircularProgressBar();
        circle.setProgress(0);
        circle.setPreferredSize(new Dimension(150, 150));
        circle.setMaximumSize(new Dimension(200, 200));
        circle.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Progressbar Animation을 위한 timer 셋팅
        circle.setProgress(0);  // 초기화
        Timer timer = new Timer(15, null);
        timer.addActionListener(e -> {
            int current = circle.getProgress();
            if (current < progressPercent.get()) { // ✅ get()으로 읽어야 함
                circle.setProgress(current + 1);
            } else {
                timer.stop();
            }
        });
        timer.start();

        // 운동시작버튼 초기화 및 Listener 설정
        PrettyButton prettyButton = new PrettyButton("운동 시작");
        prettyButton.setBounds(30, 100, 200, 50);
        prettyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("운동 시작 버튼 클릭됨!");
                app.switchCard("ROUTINE");     // 💡 카드 전환!
                app.routine_show();             // 💡 내용 갱신
            }
        });

        //체중 입력버튼 초기화 및 메서드 수행
        PrettyButton prettyButton2 = new PrettyButton("체중 입력");
        prettyButton2.setBounds(30, 100, 200, 50);
        prettyButton2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("체중 입력 버튼 클릭됨!");
             //   app.switchCard("ROUTINE");     // 💡 카드 전환!
             //   app.routine_show();             // 💡 내용 갱신
                String weight = JOptionPane.showInputDialog(
                        prettyButton2,                    // 🔥 여기 고쳤음!!
                        "체중을 입력하세요 (kg)",
                        "체중 입력",
                        JOptionPane.PLAIN_MESSAGE
                );

                if (weight != null && !weight.trim().isEmpty()) {
                    System.out.println("입력한 체중: " + weight + "kg");

                    // ✅ DB에 현재 체중 업데이트
                    try (Connection conn = DatabaseUtil.getConnection()) {
                        String sql = "UPDATE user_survey SET present_weight = ? WHERE user_key = ?";
                        PreparedStatement pstmt = conn.prepareStatement(sql);
                        pstmt.setInt(1, Integer.parseInt(weight));
                        pstmt.setString(2, App.userKey);  // 현재 로그인된 유저의 user_key 사용
                        int rows = pstmt.executeUpdate();

                        if (rows > 0) {
                            System.out.println("✅ 현재 체중 DB에 저장 완료");
                        } else {
                            System.out.println("⚠ 저장 실패: user_key 못찾음");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } else {
                    System.out.println("입력 취소 또는 공백");
                }

            }
        });

        //생성한 컴포넌트들을 panel에 배치
        panel.add(Box.createVerticalStrut(30));
        panel.add(label);
        panel.add(Box.createVerticalStrut(20));
        panel.add(percentLabel);
        panel.add(Box.createVerticalStrut(30));
        panel.add(circle);
        panel.add(Box.createVerticalStrut(30));
        panel.add(settingLabel);
        panel.add(Box.createVerticalStrut(50));
        panel.add(prettyButton);
        panel.add(Box.createVerticalStrut(20));
        panel.add(prettyButton2);
        panel.revalidate();
        panel.repaint();
    }
}

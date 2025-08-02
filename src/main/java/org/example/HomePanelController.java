package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.example.component.PrettyButton;
import org.example.component.CircularProgressBar;

public class HomePanelController {

    public static void home_show(JPanel panel, App app) {
        panel.removeAll();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK);

        JLabel label = new JLabel("최윤서님 어서오세요~");
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel percentLabel = new JLabel("목표 달성률: 58%");
        percentLabel.setForeground(Color.PINK);
        percentLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 18));
        percentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel settingLabel = new JLabel("목표 체중까지 남은 무게 : 20kg");
        settingLabel.setForeground(Color.CYAN);
        settingLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 18));
        settingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        CircularProgressBar circle = new CircularProgressBar();
        circle.setProgress(0);
        circle.setPreferredSize(new Dimension(150, 150));
        circle.setMaximumSize(new Dimension(200, 200));
        circle.setAlignmentX(Component.CENTER_ALIGNMENT);

        Timer timer = new Timer(15, null);
        timer.addActionListener(e -> {
            int current = circle.getProgress();
            if (current < 90) {
                circle.setProgress(current + 1);
            } else {
                timer.stop();
            }
        });
        timer.start();

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

        PrettyButton prettyButton2 = new PrettyButton("상태 진단");
        prettyButton.setBounds(30, 100, 200, 50);
        prettyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("운동 시작 버튼 클릭됨!");
                app.switchCard("ROUTINE");     // 💡 카드 전환!
                app.routine_show();             // 💡 내용 갱신
            }
        });





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
